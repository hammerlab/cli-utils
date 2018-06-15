package org.hammerlab.cli.spark

import org.hammerlab.cli.base.app.{ Cmd, MainSuiteI }
import org.hammerlab.spark.test.suite

abstract class MainSuite(protected val appContainer: Cmd)
  extends suite.MainSuite
    with MainSuiteI
