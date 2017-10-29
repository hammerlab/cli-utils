package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.Cmd
import org.hammerlab.cli.app.{ App, MainSuite }
import org.hammerlab.cli.args.PrintLimitArgs
import org.hammerlab.kryo.spark

/**
 * [[App]] that sets various configuration options and verifies their propagation.
 */
object ConfigTest
  extends Cmd.With[PrintLimitArgs] {

  /** Dummy registrar that only registers [[Foo]] below */
  case class Reg() extends spark.Registrar(Foo.getClass)

  object Foo

  val main = Main(
    new PathApp(_, Reg) {

      // Pass a config through to Hadoop
      sparkConf("spark.hadoop.aaa" → "bbb")

      echo(
        s"spark.hadoop.aaa: ${conf.get("aaa", "")}",
        s"spark.eventLog.enabled: ${sc.getConf.get("spark.eventLog.enabled", "")}",
        s"spark.kryo.registrator: ${sc.getConf.get("spark.kryo.registrator", "")}"
      )
    }
  )
}

class ConfigTest
  extends MainSuite(ConfigTest) {

  sparkConf(
    "spark.eventLog.enabled" → "true"
  )

  test("run") {
    check(
      path("numbers")
    )(
      """spark.hadoop.aaa: bbb
        |spark.eventLog.enabled: false
        |spark.kryo.registrator: org.hammerlab.cli.app.spark.ConfigTest$Reg
        |"""
    )
  }
}
