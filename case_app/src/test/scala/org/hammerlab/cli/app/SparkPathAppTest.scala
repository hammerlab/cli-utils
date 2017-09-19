package org.hammerlab.cli.app

import caseapp.Recurse
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.spark.test.suite.MainSuite
import org.hammerlab.test.Suite

class SparkPathAppTest
  extends MainSuite {
  test("SumNumbersSpark") {
    val out = tmpPath()
    SumNumbersSpark.main(
      Array(
        path("numbers").toString,
        "-o", out.toString
      )
    )
    out.read should be("55\n")
  }
}

case class SparkArgs(@Recurse output: OutputArgs)
  extends SparkPathAppArgs

object SumNumbersSpark
  extends SparkPathApp[SparkArgs] {
  override protected def run(options: SparkArgs): Unit = {
    import org.hammerlab.io.Printer._
    import cats.implicits.catsStdShowForInt
    echo(
      sc
        .textFile(path.toString)
        .map(_.toInt)
        .reduce(_ + _)
    )
  }
}
