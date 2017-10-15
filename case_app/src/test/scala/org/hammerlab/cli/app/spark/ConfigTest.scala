package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app
import org.hammerlab.cli.app.spark.ConfigTest._
import org.hammerlab.cli.app.{ App, Args, MainSuite }
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.io.Printer.echo
import org.hammerlab.kryo.spark.Registrar

/**
 * [[App]] that sets various configuration options and verifies their propagation.
 */
object ConfigTest {

  /** Dummy registrar that only registers [[Foo]] below */
  class Reg extends Registrar(Foo.getClass)

  object Foo

  case class App(args: Args[OutputArgs])
    extends SparkPathApp[OutputArgs, Reg](args) {

    sparkConf(
      "spark.eventLog.enabled" → "false",
      "spark.hadoop.aaa" → "bbb"
    )

    echo(
      s"spark.hadoop.aaa: ${conf.get("aaa", "")}",
      s"spark.kryo.registrator: ${sc.getConf.get("spark.kryo.registrator", "")}"
    )
  }

  object Main extends app.Main(App)
}

class ConfigTest
  extends MainSuite(Main) {
  test("run") {
    check(
      path("numbers")
    )(
      """spark.hadoop.aaa: bbb
        |spark.kryo.registrator: org.hammerlab.cli.app.ConfigTest$Reg
        |"""
    )
  }
}
