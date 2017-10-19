package org.hammerlab.cli.app

import org.hammerlab.cli.args.PrinterArgs
import org.hammerlab.test.Suite

class DeinitTest
  extends Suite {
  test("verify") {
    DeinitTest.deinitd should be(false)
    DeinitTest.main(tmpPath())
    DeinitTest.deinitd should be(true)
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
