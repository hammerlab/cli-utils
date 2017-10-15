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
  object Reg extends Reg

  object Foo

  case class App(args: Args[OutputArgs])
    extends PathApp(args, Reg) {

    sparkConf("spark.hadoop.aaa" → "bbb")

    echo(
      s"spark.hadoop.aaa: ${conf.get("aaa", "")}",
      s"spark.eventLog.enabled: ${sc.getConf.get("spark.eventLog.enabled", "")}",
      s"spark.kryo.registrator: ${sc.getConf.get("spark.kryo.registrator", "")}"
    )
  }

  object Main extends app.Main(App)
}

class ConfigTest
  extends MainSuite(Main) {

  sparkConf(
    "spark.eventLog.enabled" → "true",
    "spark.eventLog.dir" → tmpDir().toString
  )

  test("run") {
    check(
      path("numbers")
    )(
      """spark.hadoop.aaa: bbb
        |spark.eventLog.enabled: true
        |spark.kryo.registrator: org.hammerlab.cli.app.spark.ConfigTest$Reg$
        |"""
    )
  }
}
