package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.{ Cmd, MainSuite }
import org.hammerlab.cli.args.PrintLimitArgs
import org.hammerlab.kryo._

/**
 * Test demonstrating SPARK-22328: Spark's ClosureCleaner fails to serialize superclass fields when the subclass
 * instance is created in an inline function.
 */
class ClosureCleanerTest
  extends MainSuite(ClosureCleanerTest) {
  test("run") {
    check(path("numbers"))("???: 1\n")
  }
}

object ClosureCleanerTest
  extends Cmd {
  type Opts = PrintLimitArgs
  val main = Main(
    new PathApp(_, Reg) {
      echo(
        sc
          .parallelize(1 to 1)
          .map {
            i ⇒ s"${Option(path).map(_.basename).getOrElse("???")}: $i"
          }
          .collect
          .mkString("\n")
      )
    }
  )

  case class Reg() extends spark.Registrar(cls[Range])
}

class ClosureCleanerTest2
  extends MainSuite(ClosureCleanerTest2) {
  test("run") {
    check(path("numbers"))("numbers: 1\n")
  }
}

object ClosureCleanerTest2
  extends Cmd {
  type Opts = PrintLimitArgs
  val main = Main(makeApp)
  def makeApp(args: Args): App =
    new PathApp(args, Reg) {
      echo(
        sc
        .parallelize(1 to 1)
        .map {
          i ⇒ s"${Option(path).map(_.basename).getOrElse("???")}: $i"
        }
        .collect
        .mkString("\n")
      )
    }
  case class Reg() extends spark.Registrar(cls[Range])
}
