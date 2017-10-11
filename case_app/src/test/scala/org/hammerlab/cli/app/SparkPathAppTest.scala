package org.hammerlab.cli.app

import caseapp.Recurse
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.kryo.spark.Registrar

class SparkPathAppTest
  extends MainSuite(SumNumbersSpark) {

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
  extends SparkPathAppArgs

object Foo

class Reg extends Registrar(Foo.getClass)

object SumNumbersSpark
  extends SparkPathApp[SparkArgs, Reg] {
  override protected def run(options: SparkArgs): Unit = {
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
}
