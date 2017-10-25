package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.Cmd
import org.hammerlab.cli.app.{ App, MainSuite }
import org.hammerlab.cli.args.PrintLimitArgs

class NoopAppTest
  extends MainSuite(NoopAppTest) {
  test("shutdown without initializing SparkContext") {
    check(
      path("numbers")
    )(
      "yay\n"
    )
  }
}

/**
 * [[App]] that exercises some error and no-op code paths.
 */
object NoopAppTest
  extends Cmd.With[PrintLimitArgs] {
  val main = Main(
    new PathApp(_) {
      echo("yay")
    }
  )
}

