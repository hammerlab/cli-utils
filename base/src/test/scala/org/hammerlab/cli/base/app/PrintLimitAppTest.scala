package org.hammerlab.cli.base.app

import hammerlab.cli._
import hammerlab.lines._
import hammerlab.path.Path

class PrintLimitAppSuite
  extends MainSuite(PrintLimitAppTest) {
  test("print all, test overwrite") {
    val output = tmpPath()

    main(
      "-n", "10",
      output
    )
    ==(
      output.read,
      """1
        |2
        |3
        |4
        |5
        |6
        |7
        |8
        |9
        |10
        |"""
        .stripMargin
    )

    ==(
      intercept[OutPathExists] {
        main(
          "-n", "10",
          output
        )
      },
      OutPathExists(output)
    )

    main(
      "-n", "5",
      "-f",
      output
    )
    ==(
      output.read,
      """1
        |2
        |3
        |4
        |5
        |"""
        .stripMargin
    )
  }

  test("truncated output") {
    check(
      "-n", "5",
      "-l", "3"
    )(
      """1
        |2
        |3
        |…
        |"""
    )
  }
}

object PrintLimitAppTest
  extends Cmd {
  case class Opts(
    @O("n") n: Int,
    @O("l") printLimit: Limit = Unlimited,
    @O("f") overwrite: Boolean = false
  )
  val main =
    Main(
      new App(_)
        with OutPathApp
        with HasPrintLimit {
        import hammerlab.show.showInt

        override def outPath: Option[Path] = Some(Path(args(0)))

        echo(
          Limited(
            1 to opts.n
          )
        )
      }
    )
}
