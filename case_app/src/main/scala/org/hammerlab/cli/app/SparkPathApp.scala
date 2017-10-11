package org.hammerlab.cli.app

import caseapp.Parser
import caseapp.core.Messages
import grizzled.slf4j.Logging
import org.apache.spark.SparkContext
import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.hadoop.Configuration
import org.hammerlab.io.{ Printer, SampleSize }
import org.hammerlab.kryo.Registration
import org.hammerlab.kryo.spark.Registrator
import org.hammerlab.paths.Path
import org.hammerlab.spark.confs.Kryo
import org.hammerlab.spark.{ SparkConfBase, confs }

import scala.reflect.ClassTag

trait SparkPathAppArgs {
  def output: OutputArgs
}

trait HasSparkConf
  extends SparkConfBase
    with confs.Kryo
    with confs.DynamicAllocation
    with confs.EventLog
    with confs.Speculation

trait SparkApp[Args]
  extends HasSparkConf {

  self: App[Args] with Logging ⇒

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

  override def done(): Unit = {
    if (_sc != null && !_sc.isStopped) {
      info("Stopping SparkContext")
      _sc.stop()
    }
    _sc = null
  }
}

/**
 * Type-class representing types that can be passed as the `Reg` type-parameter to [[SparkPathApp]]: all
 * [[KryoRegistrator]] subclasses as well as a default/no-op [[Nothing]] implementation.
 */
trait IsRegistrar[T] {
  def apply(container: confs.Kryo): Unit
}

object IsRegistrar {
  // Default/No-op
  implicit val nothing: IsRegistrar[Nothing] =
    new IsRegistrar[Nothing] {
      override def apply(container: Kryo): Unit = {}
    }

  /**
   * Wrap any [[KryoRegistrator]] for use with [[SparkPathApp]]
   */
  implicit def registrator[T <: KryoRegistrator: ClassTag]: IsRegistrar[T] =
    new IsRegistrar[T] {
      override def apply(container: Kryo): Unit =
        container.registrar(implicitly[ClassTag[T]])
    }
}

/**
 * [[SparkApp]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
abstract class SparkPathApp[Args <: SparkPathAppArgs : Parser : Messages, Reg: IsRegistrar]
  extends PathApp[Args]
    with SparkApp[Args]
    with confs.Kryo {

  implicitly[IsRegistrar[Reg]].apply(this)

  @transient implicit var printer: Printer = _
  @transient implicit var printLimit: SampleSize = _

  override def init(options: Args): Unit = {
    val OutputArgs(printLim, path, overwrite) = options.output

    if (path.exists(_.exists) && !overwrite)
      throw new IllegalArgumentException(
        s"Output path $path exists and overwrite (-f) not set"
      )

    printer = Printer(path)
    printLimit = printLim
  }
}
