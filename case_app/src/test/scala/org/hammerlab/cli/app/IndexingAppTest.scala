package org.hammerlab.cli.app

import caseapp.{ AppName, ExtraName ⇒ O }
import org.hammerlab.paths.Path
import org.hammerlab.test.Suite

class IndexingAppTest
  extends Suite {
  test("SumNumbers") {
    val in = fileCopy(path("numbers"), tmpPath())
    SumNumbersApp.main(
      Array(
        in.toString()
      )
    )
    (in + ".sum").read should be("55\n")
  }
}

case class Opts(@O("o") out: Option[Path] = None,
                overwrite: Boolean = false)

case class SumNumbers(args: Args[Opts])
  extends IndexingApp("sum", args) {
  import org.hammerlab.io.Printer._
  import cats.implicits.catsStdShowForInt
  echo(
    path
      .lines
      .map(_.toInt)
      .sum
  )
}

/** Add up numbers from an input file, write result to a sibling file with extension '.sum' */
object SumNumbersApp extends CApp[Opts, SumNumbers](SumNumbers)
