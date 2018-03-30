package org.hammerlab.caseapp

import caseapp.core.Error.{ MalformedValue, SeveralErrors }
import caseapp.core.argparser.ArgParser
import hammerlab.Suite
import hammerlab.cli._
import org.hammerlab.cmp.CanEq
import org.hammerlab.cmp.CanEq.instance

class ParsersTest
  extends Suite {

  implicit def arrs[T, U](implicit
                          ce: CanEq[T, U]): CanEq.Aux[Array[T], Array[U], (Int, Option[ce.Error])] = {
    val iters: CanEq.Aux[Iterator[T], Iterator[U], (Int, Option[ce.Error])] = CanEq.iterators[T, U](ce)
    instance[Array[T], Array[U], (Int, Option[ce.Error])](
      (s1, s2) ⇒
        iters(
          s1.iterator,
          s2.iterator
        )
    )
  }
  test("array") {
    val parser = implicitly[ArgParser[Array[Double]]]
    ===(
      parser(None, "1,2.3,4.56"),
      Right(Array(1, 2.3, 4.56))
    )
    ===(
      parser(None, "1,2.a"),
      Left(
        MalformedValue("double float", "2.a")
      )
    )
    ===(
      parser(None, "1,2.a,3.4,d"),
      Left(
        SeveralErrors(
          MalformedValue("double float", "2.a"),
          Seq(
            MalformedValue("double float", "d"  )
          )
        )
      )
    )
  }
}
