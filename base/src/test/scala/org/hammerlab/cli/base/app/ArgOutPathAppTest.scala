package org.hammerlab.cli.base.app

import org.hammerlab.cli.base.args.PrinterArgs

class ArgsOutPathAppTest
  extends MainSuite(ArgsOutPathTest) {

  test("run") {
    check(
      tmpPath()
    )(
      "output basename: foo\n"
    )
  }

  override def outBasename = "foo"
}

object ArgsOutPathTest
  extends Cmd.With[PrinterArgs] {
  val main = Main(
    new ArgsOutPathApp(_)
      with HasPrinter {
      echo(s"output basename: ${outPath.fold("???")(_.basename)}")
    }
  )
}
