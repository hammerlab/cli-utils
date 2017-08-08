package org.hammerlab.commands

import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.{ SparkConf, SparkContext }
import org.hammerlab.spark.{ Conf, SparkConfBase }

import scala.collection.mutable

abstract class SparkCommand[T <: Args: Manifest]
  extends Command[T]
    with SparkConfBase {

  override def run(args: T): Unit = {
    val sc = createSparkContext()
    try {
      args.validate(sc)
      run(args, sc)
    } finally {
      sc.stop()
    }
  }

  def run(args: T, sc: SparkContext): Unit

  private val defaultConfs = mutable.HashMap[String, String]()
  def setDefaultConf(key: String, value: String): Unit = {
    defaultConfs.update(key, value)
  }

  def registrar: Class[_ <: KryoRegistrator] = null

  sparkConf(
    "spark.master" → "local[*]",
    "spark.serializer" → "org.apache.spark.serializer.KryoSerializer",
    "spark.kryoserializer.buffer" → "4mb",
    "spark.kryo.referenceTracking" → "true",
    "spark.kryo.registrationRequired" → "true"
  )

  Option(registrar).foreach(
    clz ⇒
      sparkConf(
        "spark.kryo.registrator" → clz.getCanonicalName
      )
  )

  /**
   * Return a spark context.
   *
   * Typically, most properties are set through config file / cmd-line.
   * @return
   */
  private def createSparkContext(): SparkContext = {
    val conf = makeSparkConf

    conf.getOption("spark.app.name") match {
      case Some(cmdLineName) => conf.setAppName(s"$cmdLineName: $name")
      case _ => conf.setAppName(name)
    }

    new SparkContext(conf)
  }
}
