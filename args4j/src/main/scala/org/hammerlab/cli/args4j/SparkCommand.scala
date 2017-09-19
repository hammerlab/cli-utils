package org.hammerlab.cli.args4j

import org.apache.spark.SparkContext
import org.hammerlab.spark.{ SparkConfBase, confs }

abstract class SparkCommand[T <: Args: Manifest]
  extends Command[T]
    with SparkConfBase
    with confs.Kryo {

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
