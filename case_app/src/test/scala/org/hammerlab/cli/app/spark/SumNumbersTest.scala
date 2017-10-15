package org.hammerlab.cli.app.spark

import caseapp.Recurse
import org.hammerlab.cli.app
import org.hammerlab.cli.app.spark.SumNumbers._
import org.hammerlab.cli.app.{ App, Args, Main, MainSuite, WithPrintLimit }
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.io.Printer._
import org.hammerlab.magic.rdd.SampleRDD._

class SumNumbersTest
  extends MainSuite(Main) {

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
 * Simple [[App]] and [[Main]] that reads some numbers and prints them and their sum.
 *
 * Exercises [[WithPrintLimit]], among other things.
 */
object SumNumbers {

  case class Opts(@Recurse output: OutputArgs)

  case class App(args: Args[Opts])
    extends SparkPathApp[Opts, Nothing](args) {

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

    print(
      sampledInts,
      count,
      s"$count numbers:",
      n ⇒ s"$n of $count numbers:"
    )

    echo(
      "",
      s"Sum: $sum"
    )
  }

  object Main extends app.Main(App)
}
