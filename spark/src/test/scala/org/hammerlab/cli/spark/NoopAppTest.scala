package org.hammerlab.cli.spark

import org.hammerlab.cli.base.app.Cmd
import org.hammerlab.cli.base.args.PrintLimitArgs

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
 * [[PathApp]] that exercises some error and no-op code paths.
 */
object NoopAppTest
  extends Cmd.With[PrintLimitArgs] {
  val main = Main(
    new PathApp(_) {
      echo("yay")
    }
  )
}
