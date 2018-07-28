package org.hammerlab.cli.base.app

import caseapp.core.RemainingArgs
import org.hammerlab.cli.base.args.PrinterArgs
import org.hammerlab.test.Suite

class DeinitTest
  extends Suite {
  test("verify") {
    DeinitTest.deinitd = false
    DeinitTest.main(tmpPath())
    ==(DeinitTest.deinitd, true)
  }

  test("main() wrappers") {
    DeinitTest.deinitd = false
    val args = Array(tmpPath().toString)
    DeinitTest.main(args)
    ==(DeinitTest.deinitd, true)

    DeinitTest.deinitd = false
    DeinitTest.main(
      PrinterArgs(),
      RemainingArgs(
        Seq(
          tmpPath().toString
        ),
        Nil
      )
    )
    ==(DeinitTest.deinitd, true)
  }
}

object DeinitTest
  extends Cmd.With[PrinterArgs] {
  var deinitd = false
  val main = Main(
    new IndexingApp("test", _) {
      deinit {
        deinitd = true
      }
    }
  )
}
