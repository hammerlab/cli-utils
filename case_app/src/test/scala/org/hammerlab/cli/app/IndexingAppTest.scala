package org.hammerlab.cli.app

import caseapp.{ AppName, ExtraName ⇒ O }
import org.hammerlab.paths.Path
import org.hammerlab.test.Suite

class IndexingAppTest
  extends Suite {

  test("sample app") {
    val in = tmpPath()
    in.writeLines(1 to 10 map(_.toString))
    SumNumbers.main(
      Array(
        in.toString()
      )
    )
    (in + ".sum").read should be("55\n")
  }

}

@AppName("Add up numbers from an input file, write result to a sibling file with extension '.sum'")
case class Args(@O("o") out: Option[Path] = None)
  extends OutPathArgs

object SumNumbers
  extends IndexingApp[Args](".sum") {
  override protected def run(options: Args): Unit = {

    import org.hammerlab.io.Printer._
    import cats.implicits.catsStdShowForInt

    echo(
      path
        .lines
        .map(_.toInt)
        .sum
    )
  }
}
