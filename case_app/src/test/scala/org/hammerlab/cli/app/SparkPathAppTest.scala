package org.hammerlab.cli.app

import caseapp.Recurse
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.io.Printer._
import org.hammerlab.kryo.spark.Registrar
import org.hammerlab.magic.rdd.SampleRDD._
import org.hammerlab.test.Suite

class SparkPathAppTest
  extends MainSuite(SumNumsApp) {

  sparkConf("spark.eventLog.enabled" → "false")

  /**
   * Test dummy [[SumNumbersSpark]] app below, which prints the sum of some numbers as well as its
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
        |
        |Kryo registrator: org.hammerlab.cli.app.Reg
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
        |
        |Kryo registrator: org.hammerlab.cli.app.Reg
        |"""
    )
  }
}


case class SparkArgs(@Recurse output: OutputArgs)

object Foo

/** Dummy registrar that only registers [[Foo]] above */
class Reg extends Registrar(Foo.getClass)

case class SumNumbersSpark(args: Args[SparkArgs])
  extends SparkPathApp[SparkArgs, Reg](args) {

  val rdd =
    sc
      .textFile(path.toString)
      .map(_.toInt)

  import cats.implicits.{ catsKernelStdGroupForInt, catsKernelStdGroupForLong, catsStdShowForInt }
  import cats.syntax.all._
  import org.hammerlab.types.Monoid._

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

  val registrator =
    sc
      .getConf
      .get(
        "spark.kryo.registrator",
        ""
      )

  echo(
    "",
    s"Sum: $sum",
    "",
    s"Kryo registrator: $registrator"
  )
}

object SumNumsApp extends CApp[SparkArgs, SumNumbersSpark](SumNumbersSpark)

case class NoRegApp(args: Args[SparkArgs])
  extends SparkPathApp[SparkArgs, Nothing](args) {
  // no-op
  echo("yay")
}

object NoRegAp extends CApp[SparkArgs, NoRegApp](NoRegApp)

class SparkPathAppErrorTest
  extends Suite {
  test("outPath exists") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    intercept[IllegalArgumentException] {
      NoRegAp.main(
        Array(
          path("numbers").toString,
          outPath.toString
        )
      )
    }
    .getMessage should fullyMatch regex("""Output path .* exists and overwrite \(-f\) not set""".r)
  }

  test("shutdown without initializing SparkContext") {
    val outPath = tmpPath()
    NoRegAp.main(
      Array(
        path("numbers").toString,
        outPath.toString
      )
    )
    outPath.read should be("yay\n")
  }
}

