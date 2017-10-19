package org.hammerlab.cli.app

import org.hammerlab.paths.Path

class OptsOutPathAppTest
  extends MainSuite(OptsOutPath) {

  test("run") {
    check(
      tmpPath()
    )(
      "yay\n"
    )
  }

  override def defaultOpts(outPath: Path) = Seq("--out-path", outPath)
}

object OptsOutPath extends AppContainer {
  case class Opts(outPath: Option[Path],
                  overwrite: Boolean = false)

  val main = AppMain(
    new OptsOutPathApp(_)
      with HasPrinter {
      import org.hammerlab.io.Printer._
      echo("yay")
    }
  )
}
