package org.hammerlab.cli.app

import hammerlab.print._
import org.hammerlab.cli.app.HasPrintLimit.PrintLimit
import org.hammerlab.shapeless.record.Find
import shapeless.{ Witness ⇒ W }

/**
 * Mix-in for [[HasPrinter]] [[App]]s that may set an optional cap on how many items should be output from potentially
 * large collections (e.g. [[org.apache.spark.rdd.RDD]]s).
 */
trait HasPrintLimit
  extends HasPrinter {
  self: App[_] ⇒
  private var _printLimit: SampleSize = _
  implicit def printLimit[Opts](implicit
                                args: Args[Opts],
                                select: PrintLimit[Opts]): SampleSize = {
    if (_printLimit == null)
      _printLimit = select(args)

    _printLimit
  }
}

object HasPrintLimit {
  type PrintLimit[Opts] = Find[Opts, W.`'printLimit`.T, SampleSize]
}
