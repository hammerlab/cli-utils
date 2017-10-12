package org.hammerlab.cli.app

import java.io.Closeable

import caseapp.{ CaseApp, Parser, RemainingArgs }
import caseapp.core.Messages
import grizzled.slf4j.Logging
import org.apache.spark.SparkContext
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.hadoop.Configuration
import org.hammerlab.io.{ Printer, SampleSize }
import org.hammerlab.paths.Path
import org.hammerlab.spark.confs

import scala.collection.mutable.ArrayBuffer

trait PathAppArgs {
  def output: OutputArgs
}

abstract class CApp[Opts : Parser : Messages, Ap <: App[Opts]](make: Args[Opts] ⇒ Ap)
  extends CaseApp[Opts] {
  override def run(opts: Opts, args: RemainingArgs): Unit = {
    make(Args(opts, args.remainingArgs))
    println("done!")
  }
}

case class Args[Opts](opts: Opts, args: Seq[String]) {
  def apply(i: Int) = args(i)
}
object Args {
  implicit def unwrap(args: Args[_]): Seq[String] = args.args
  implicit def unwrap[Opts](args: Args[Opts]): Opts = args.opts
}

abstract class App[Opts](protected val _args: Args[Opts])
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
abstract class PathApp[Opts](args: Args[Opts])
  extends App[Opts](args) {
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

  private var _printer: Printer = _
  implicit def printer(implicit select: Select[Opts, OutPathOpts]) = {
    if (_printer == null) {
      val overwrite = select(_args).overwrite
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
abstract class PathsApp[Opts](args: Args[Opts])
  extends PathApp[Opts](args)
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

abstract class OutPathApp[Opts <: PathAppArgs](args: Args[Opts])
  extends PathApp[Opts](args)
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
  implicit def printLimit(implicit select: Select[Opts, SampleSize]): SampleSize = {
    if (printLimit == null)
      _printLimit = select(_args)

    _printLimit
  }
}

abstract class SparkPathApp[Opts <: PathAppArgs, Reg: IsRegistrar](_args: Args[Opts])
  extends OutPathApp[Opts](_args)
    with SparkApp
    with confs.Kryo {
  implicitly[IsRegistrar[Reg]].apply(this)
}

abstract class IndexingApp[Opts : Parser : Messages](suffix: String, args: Args[Opts])
  extends PathApp[Opts](args)
    with PrinterApp[Opts] {
  override def outPath: Option[Path] =
    Some(
      path + s".$suffix"
    )
}

