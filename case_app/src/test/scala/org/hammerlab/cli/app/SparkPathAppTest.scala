package org.hammerlab.cli.app

import caseapp.Recurse
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.kryo.spark.Registrar
import org.hammerlab.test.Suite

class SparkPathAppTest
  extends MainSuite(SumNumsApp) {

  sparkConf("spark.eventLog.enabled" → "false")

  /**
   * Test dummy [[SumNumbersSpark]] app below, which prints the sum of some numbers as well as its
   * [[org.apache.spark.serializer.KryoRegistrator]].
   */
  test("SumNumbersSpark") {
    check(
      path("numbers")
    )(
      """55
        |org.hammerlab.cli.app.Reg
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
  import cats.implicits.catsStdShowForInt
  import org.hammerlab.io.Printer._
  echo(
    sc
      .textFile(path.toString)
      .map(_.toInt)
      .reduce(_ + _),
    sc
      .getConf
      .get(
        "spark.kryo.registrator",
        ""
      )
  )
}

object SumNumsApp extends CApp[SparkArgs, SumNumbersSpark](SumNumbersSpark)

case class NoRegApp(args: Args[SparkArgs])
  extends SparkPathApp[SparkArgs, Nothing](args) {
  // no-op
  import org.hammerlab.io.Printer._
  echo("yay")
}

object NoRegAp extends CApp[SparkArgs, NoRegApp](NoRegApp)

class SparkPathAppErrorTest
  extends Suite {
  test("main") {
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
    }.getMessage should fullyMatch regex("""Output path .* exists and overwrite \(-f\) not set""".r)
  }
}

