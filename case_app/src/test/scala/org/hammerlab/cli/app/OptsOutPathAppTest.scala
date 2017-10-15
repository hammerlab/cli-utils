package org.hammerlab.cli.app

import org.hammerlab.cli.app
import org.hammerlab.paths.Path

class OptsOutPathAppTest
  extends MainSuite(OptsOutPath.Main) {

  test("run") {
    check(
      tmpPath()
    )(
      "yay\n"
    )
  }

  override def defaultOpts(outPath: Path) = Seq("--out-path", outPath)
}

object OptsOutPath {
  case class Opts(outPath: Option[Path],
                  overwrite: Boolean = false)

  case class App(args: Args[Opts])
    extends OptsOutPathApp(args)
      with WithPrinter[Opts] {
    import org.hammerlab.io.Printer._
    echo("yay")
  }

  object Main extends app.Main(App)
}
