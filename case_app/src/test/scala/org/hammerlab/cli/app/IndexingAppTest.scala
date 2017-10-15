package org.hammerlab.cli.app

import caseapp.{ ExtraName ⇒ O }
import org.hammerlab.cli.app
import org.hammerlab.cli.app.IndexingAppTest._
import org.hammerlab.test

/**
 * Simple [[App]], [[Main]], and [[test.Suite]] for an [[IndexingApp]] that sums some numbers and writes the result to a
 * file.
 */
class IndexingAppTest
  extends test.Suite {
  test("SumNumbers") {
    val in = fileCopy(path("numbers"), tmpPath())
    Main(
      Array[Arg](in)
    )
    (in + ".sum").read should be("55\n")
  }

  test("outPath exists - error") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    intercept[IllegalArgumentException] {
      Main(
        Array[Arg](
          path("numbers"),
          outPath
        )
      )
    }
    .getMessage should fullyMatch regex("""Output path .* exists and 'overwrite' not set""".r)
  }

  test("outPath exists - overwrite") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    Main(
      Array[Arg](
        "-f",
        path("numbers"),
        outPath
      )
    )

    outPath.read should be("55\n")
  }
}

object IndexingAppTest {

  case class Opts(@O("f") overwrite: Boolean = false)

  case class App(args: Args[Opts])
    extends IndexingApp("sum", args) {
    import cats.implicits.catsStdShowForInt
    import org.hammerlab.io.Printer._
    echo(
      path
        .lines
        .map(_.toInt)
        .sum
    )
  }

  object Main extends app.Main(App)
}
