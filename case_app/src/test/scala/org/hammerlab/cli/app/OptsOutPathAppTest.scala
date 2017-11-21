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

object OptsOutPath extends Cmd {
  case class Opts(outPath: Option[Path],
                  overwrite: Boolean = false)

  val main = Main(
    new OptsOutPathApp(_)
      with HasPrinter {
      echo("yay")
    }
  )
}
