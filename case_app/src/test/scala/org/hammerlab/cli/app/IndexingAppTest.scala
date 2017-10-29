package org.hammerlab.cli.app

import caseapp.{ ExtraName ⇒ O }
import org.hammerlab.cli.app.IndexingAppTest._
import org.hammerlab.test

/**
 * Simple [[App]], [[Runner]], and [[test.Suite]] for an [[IndexingApp]] that sums some numbers and writes the result to a
 * file.
 */
class IndexingAppTest
  extends test.Suite {
  test("SumNumbers") {
    val in = fileCopy(path("numbers"), tmpPath())
    main(in)
    (in + ".sum").read should be("55\n")
  }

  test("outPath exists - error") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    intercept[IllegalArgumentException] {
      main(
        path("numbers"),
        outPath
      )
    }
    .getMessage should fullyMatch regex("""Output path .* exists and 'overwrite' not set""".r)
  }

  test("outPath exists - overwrite") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    main(
      "-f",
      path("numbers"),
      outPath
    )

    outPath.read should be("55\n")
  }
}

object IndexingAppTest
  extends Cmd {
  case class Opts(@O("f") overwrite: Boolean = false)

  val main = Main(
    new IndexingApp("sum", _) {
      import cats.implicits.catsStdShowForInt
      echo(
        path
          .lines
          .map(_.toInt)
          .sum
      )
    }
  )
}
