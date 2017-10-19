package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.{ App, Container, MainSuite }
import org.hammerlab.cli.args.PrintLimitArgs
import org.hammerlab.io.Printer.echo

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
object NoopAppTest extends Container[PrintLimitArgs] {
  val main = AppMain(
    new PathApp(_) {
      echo("yay")
    }
  )
}

