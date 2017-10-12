package org.hammerlab.cli.app

import caseapp.Recurse
import org.hammerlab.cli.app.Concrete.CApp
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.kryo.spark.Registrar
import org.hammerlab.test.Suite

class SparkPathAppTest
  extends MainSuite(SumNumsApp) {

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

import Apps._

case class SparkArgs(@Recurse output: OutputArgs)
  extends PathAppArgs

object Foo

class Reg extends Registrar(Foo.getClass)

case class SumNumbersSpark(implicit override val args: Args[SparkArgs])
  extends SparkPathApp[SparkArgs, Reg]() {
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

object SumNumsApp extends CApp[SparkArgs, SumNumbersSpark](SumNumbersSpark()(_))

case class NoRegApp(implicit override val args: Args[SparkArgs])
  extends Apps.SparkPathApp[SparkArgs, Nothing]() {
  // no-op
  import org.hammerlab.io.Printer._
  echo("woo")
}

object NoRegAp extends CApp[SparkArgs, NoRegApp](NoRegApp()(_))

class SparkPathAppErrorTest extends Suite {
  test("main") {
    val outPath = tmpPath()
    outPath.write("abc")

    // Try to run against a path that already exists
    intercept[IllegalArgumentException] {
      NoRegAp.main(
        Array(
          "-o", outPath.toString,
          path("numbers").toString
        )
      )
    }.getMessage should fullyMatch regex("""Output path .* exists and overwrite \(-f\) not set""".r)
  }
}

