package org.hammerlab.cli.base.app

import org.hammerlab.cli.base.args.PrinterArgs
import org.hammerlab.test.Suite

class DeinitTest
  extends Suite {
  test("verify") {
    ==(DeinitTest.deinitd, false)
    DeinitTest.main(tmpPath())
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
