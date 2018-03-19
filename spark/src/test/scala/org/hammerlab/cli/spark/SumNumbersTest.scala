package org.hammerlab.cli.spark

import caseapp.Recurse
import hammerlab.lines.limit._
import magic_rdds._
import org.hammerlab.cli.base.app.{ App, Cmd, HasPrintLimit, Runner }
import org.hammerlab.cli.base.args.PrintLimitArgs

class SumNumbersTest
  extends MainSuite(SumNumbers) {

  /**
   * Test dummy [[SumNumbers]] app below, which prints the sum of some numbers as well as its
   * [[org.apache.spark.serializer.KryoRegistrator]].
   */
  test("print all") {
    check(
      path("numbers")
    )(
      """10 numbers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	6
        |	7
        |	8
        |	9
        |	10
        |
        |Sum: 55
        |"""
    )
  }

  test("print limit 5") {
    check(
      "-l", "5",
      path("numbers")
    )(
      """5 of 10 numbers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	…
        |
        |Sum: 55
        |"""
    )
  }
}

/**
 * Simple [[App]] and [[Runner]] that reads some numbers and prints them and their sum.
 *
 * Exercises [[HasPrintLimit]], among other things.
 */
object SumNumbers extends Cmd {

  case class Opts(@Recurse output: PrintLimitArgs)

  val main = Main(
    new PathApp(_) {

      val rdd =
        sc
          .textFile(path.toString)
          .map(_.toInt)

      import cats.implicits._

      val (sum, count) =
        rdd
          .map(_ → 1L)
          .reduce(_ |+| _)

      val sampledInts = rdd.sample(count)

      echo(
        Limited(
          sampledInts,
          count,
          s"$count numbers:",
          s"$limit of $count numbers:"
        )
      )

      echo(
        "",
        s"Sum: $sum"
      )
    }
  )
}
