package org.hammerlab.cli.app

import org.hammerlab.cli.args.PrinterArgs
import org.hammerlab.io.Printer._

class RequiredArgOutPathAppTest
  extends MainSuite(RequiredArgOutPathTest) {

  test("run") {
    check(
      tmpPath()
    )(
      "output basename: foo\n"
    )
  }

  test("missing out path") {
    intercept[IllegalArgumentException] {
      main(tmpPath())
    }
    .getMessage should be("Expected at least two arguments (input and output paths)")
  }

  override def outBasename = "foo"
}

object RequiredArgOutPathTest
  extends Container[PrinterArgs] {
  val main = AppMain(
    new RequiredArgOutPathApp(_)
      with HasPrinter {
      echo(s"output basename: ${out.basename}")
    }
  )
}
