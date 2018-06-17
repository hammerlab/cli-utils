package org.hammerlab.cli.spark

import hammerlab.cli._
import hammerlab.indent.spaces
import hammerlab.path._
import hammerlab.print._
import hammerlab.show._
import org.hammerlab.kryo

class PathOptTest
  extends MainSuite(PathOptTest) {
  test("run") {
    val out = tmpPath()
    appContainer.main(
      "-n", "100",
      "-o", out
    )
    ==(
      out.read,
      "5050\n"
    )
  }
}

/**
 * Test-[[Cmd]] similar to [[SumNumbersTest]], but taking its output [[Path]] as an option instead of as a positional
 * argument
 */
object PathOptTest
  extends Cmd {
  case class Opts(
    @O("n") n: Int,
    @O("o") out: Path
  )

  case class Reg() extends kryo.spark.Registrar(classOf[Range])

  val main = Main(
    new spark.App(_, Reg) {
      val out = opts.out
      out.mkdirs
      implicit val printer = Printer(out)
      val sum = sc.parallelize(1 to opts.n).reduce(_ + _)
      echo(sum)
    }
  )
}
