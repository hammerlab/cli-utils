package org.hammerlab.cli.app

import java.io.Closeable

import caseapp.{ CaseApp, Parser, RemainingArgs }
import caseapp.core.Messages
import grizzled.slf4j.Logging
import org.apache.spark.SparkContext
import org.hammerlab.cli.app.Apps.Args
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.hadoop.Configuration
import org.hammerlab.io.{ Printer, SampleSize }
import org.hammerlab.paths.Path
import org.hammerlab.spark.confs
import shapeless.ops.hlist.Selector
import shapeless._

import scala.collection.mutable.ArrayBuffer

trait PathAppArgs {
  def output: OutputArgs
}

/*
abstract class PathApp[Args <: PathAppArgs : Parser : Messages]
  extends App[Args]
    with Closeable {

  @transient implicit var path: Path = _

  def init(options: Args): Unit = {}
  def close(): Unit = {}

  final protected override def _run(options: Args, args: Seq[String]): Unit = {
    if (args.size != 1) {
      throw new IllegalArgumentException(
        s"Exactly one argument (a BAM file path) is required"
      )
    }

    path = Path(args.head)

    init(options)
    run(options)
    close()
  }

  protected def run(options: Args): Unit
}
*/

object Concrete {
/*
  case class App[Opts](override val opts: Opts,
                       override val args: Args)
    extends Apps.App()(opts, args)

  case class PathApp[Opts](override val opts: Opts,
                           override val args: Args)
    extends Apps.PathApp[Opts]

  case class SparkPathApp[Opts <: PathAppArgs, Reg: IsRegistrar](override val opts: Opts,
                                                                 override val args: Args
                                                                )
    extends Apps.SparkPathApp[Opts, Reg]

  case class IndexingApp[Opts : Parser : Messages](override val opts: Opts,
                                                   override val args: Args,
                                                   suffix: String
                                                  )
    extends Apps.IndexingApp[Opts](suffix)(opts, args)
*/

  abstract class CApp[Opts : Parser : Messages, Ap <: Apps.App[Opts]](make: Args[Opts] ⇒ Ap)
    extends CaseApp[Opts] {
    override def run(opts: Opts, args: RemainingArgs): Unit = {
      make(Args(opts, args.remainingArgs))
      println("done!")
    }
  }
/*
  implicit def makeApp[Opts, Ap](implicit gen: Generic.Aux[Ap, Opts :: Args :: HNil]): Ap = {

  }
*/
}

object Apps {
  case class Args[Opts](opts: Opts, args: Seq[String]) {
    def apply(i: Int) = args(i)
  }
  object Args {
    implicit def unwrap(args: Args[_]): Seq[String] = args.args
    implicit def unwrap[Opts](args: Args[Opts]): Opts = args.opts
  }

  abstract class App[Opts](implicit val args: Args[Opts])
    extends Closeable {
    private val deinitializations = ArrayBuffer[() ⇒ Unit]()
    def deinit(fn: ⇒ Unit): Unit = {
      deinitializations += (() ⇒ fn)
    }

    override def close(): Unit =
      deinitializations.foreach(_())
  }

  /**
   * Base-class for [[App]]s that take a first-argument input-[[Path]]
   */
  abstract class PathApp[Opts](implicit args: Args[Opts])
    extends App[Opts]() {
    implicit val path: Path = Path(args(0))
  }

  /**
   * Tag `opts` classes as well as [[App]]s that may parse an output-[[Path]] from options or arguments in different
   * ways
   */
  trait HasOutPath {
    def outPath: Option[Path]
  }

  trait OutPathOpts
    extends HasOutPath {
    def overwrite: Boolean
  }

  /**
   * Interface for [[App]]s that print to an output [[Path]], if one is provided, otherwise to stdout
   */
  trait PrinterApp[Opts]
    extends Closeable
      with HasOutPath {
    self: App[Opts] ⇒

    println("printer app")

    private var _printer: Printer = _
    implicit def printer(implicit has: Select[Opts, OutPathOpts]) = {
      if (_printer == null) {
        val overwrite = has(args).overwrite
        _printer =
          outPath match {
            case Some(path) if path.exists && !overwrite ⇒
              throw new IllegalArgumentException(
                s"Output path $path exists and overwrite (-f) not set"
              )
            case _ ⇒
              Printer(outPath)
          }
      }
      deinit {
        _printer.close()
      }
      _printer
    }

  }

  /**
   * [[App]] superclass where the second argument is the output path to write to; default: stdout
   */
  abstract class PathsApp[Opts](implicit args: Args[Opts])
    extends PathApp[Opts]()
      with PrinterApp[Opts] {
    self: App[Opts] ⇒
    override implicit val outPath =
      if (args.size > 1)
        Some(
          Path(
            args(1)
          )
        )
      else
        None
  }

  abstract class OutPathApp[Opts <: PathAppArgs](implicit args: Args[Opts])
    extends PathApp[Opts]()
      with PrinterApp[Opts] {
    self: App[Opts] ⇒
    override implicit val outPath =
      args
        .output
        .outPath
  }

  trait SparkApp
    extends HasSparkConf
      with Logging {

    self: App[_] ⇒

    @transient private var _sc: SparkContext = _

    implicit def sc: SparkContext = {
      if (_sc == null) {
        info("Creating SparkContext")
        val conf = makeSparkConf
        if (
          conf.get("spark.eventLog.enabled", "true") == "true" &&
            conf.get("spark.eventLog.dir", "") == "" &&
            !Path("/tmp/spark-events").exists) {
          conf.set("spark.eventLog.enabled", "false")
          warn("Disabling event-logging because default destination /tmp/spark-events doesn't exist")
        }
        _sc = new SparkContext(conf)
      }
      _sc
    }

    implicit def conf: Configuration = sc.hadoopConfiguration

    deinit {
      if (_sc != null && !_sc.isStopped) {
        info("Stopping SparkContext")
        _sc.stop()
      }
      _sc = null
    }
  }

  trait PrintLimitApp[Opts]
    extends PrinterApp[Opts] {
    self: App[Opts] ⇒
    private var _printLimit: SampleSize = _
    implicit def printLimit(implicit has: Has[Opts, SampleSize]): SampleSize = {
      if (printLimit == null)
        _printLimit = has(args)

      _printLimit
    }
  }

  abstract class SparkPathApp[Opts <: PathAppArgs, Reg: IsRegistrar](implicit args: Args[Opts])
    extends OutPathApp[Opts]()
      with SparkApp
      with confs.Kryo {
    implicitly[IsRegistrar[Reg]].apply(this)
  }

  abstract class IndexingApp[Opts : Parser : Messages](suffix: String)(implicit args: Args[Opts])
    extends PathApp[Opts]()
      with PrinterApp[Opts] {
    override def outPath: Option[Path] =
      Some(
        path + s".$suffix"
      )
  }
}

trait Has[C, T] {
  def apply(c: C): T
}

object Has {

  def apply[C, T](fn: C ⇒ T): Has[C, T] =
    new Has[C, T] {
      override def apply(c: C) = fn(c)
    }

  implicit def cc[T, S <: T, CC, L <: HList](implicit
                                     gen: Generic.Aux[CC, L],
                                     has: Has[L, S]): Has[CC, T] =
    Has(
      cc ⇒
        has(
          gen.to(cc)
        )
    )

  implicit def select[L <: HList, T, S <: T](implicit sl: Selector[L, S]): Has[L, T] = Has(l ⇒ l.select[S])
}

/*
trait Select[L, U, T >: U] extends DepFn1[L] with Serializable { type Out = T }

object Select {

  def apply[L, U, T >: U](implicit Select: Select[L, U, T]): Select[L, U, T] = Select

  implicit def cc[T, S <: T, CC, L <: HList](implicit
                                             gen: Generic.Aux[CC, L],
                                             select: Select[L, S, T]): Select[CC, S, T] =
    new Select[CC, S, T] {
      override def apply(t: CC) = select(gen.to(t))
    }

  implicit def select[H, R >: H, T <: HList]: Select[H :: T, H, R] =
    new Select[H :: T, H, R] {
      def apply(l : H :: T): R = l.head
    }

  implicit def recurse[H, L <: HList, U, T >: U]
  (implicit st : Select[L, U, T]): Select[H :: L, U, T] =
    new Select[H :: L, U, T] {
      def apply(l : H :: L) = st(l.tail)
    }
}
*/

trait Select[L, +U] extends Serializable {
  def apply(l: L): U
}

object Select {
  def apply[L, U](fn: L ⇒ U) = new Select[L, U] { override def apply(l: L) = fn(l) }

  implicit def cc[T, CC, L <: HList](implicit
                                     gen: Generic.Aux[CC, L],
                                     select: Select[L, T]): Select[CC, T] =
    Select[CC, T](cc ⇒ select(gen.to(cc)))

  implicit def select[H, T <: HList]: Select[H :: T, H] =
    Select[H :: T, H](_.head)

  implicit def recurse[H, L <: HList, U](implicit st : Select[L, U]): Select[H :: L, U] =
    Select[H :: L, U](l ⇒ st(l.tail))
}
