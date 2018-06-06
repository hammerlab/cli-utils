package org.hammerlab.caseapp

import caseapp.core.Error.{ MalformedValue, SeveralErrors }
import caseapp.core.argparser.ArgParser
import hammerlab.Suite
import hammerlab.cli._

class ParsersTest
  extends Suite {

  test("array") {
    val parser = implicitly[ArgParser[Array[Double]]]

    ==(
      parser(None, "1,2.3,4.56"),
      Right(Array(1, 2.3, 4.56))
    )

    ==(
      parser(None, "1,2.3,4.56"),
      Right(Array(1, 2.3, 4.56))
    )
    ==(
      parser(None, "1,2.a"),
      Left(
        MalformedValue("double float", "2.a")
      )
    )
    ==(
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
