package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app
import org.hammerlab.cli.app.{ App, Arg, Args }
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.io.Printer.echo
import org.hammerlab.test.Suite

class NoopAppTest
  extends Suite {
  import NoopAppTest._
  test("shutdown without initializing SparkContext") {
    val outPath = tmpPath()
    Main(
      Array[Arg](
        path("numbers"),
        outPath
      )
    )
    outPath.read should be("yay\n")
  }
}

/**
 * [[App]] that exercises some error and no-op code paths.
 */
object NoopAppTest {
  case class App(args: Args[OutputArgs])
    extends PathApp(args) {
    echo("yay")
  }
  object Main extends app.Main(App)
}
