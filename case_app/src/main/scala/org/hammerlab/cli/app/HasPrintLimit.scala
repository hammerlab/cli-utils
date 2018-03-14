package org.hammerlab.cli.app

import hammerlab.lines.limit._
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
  private var _limit: Limit = _
  implicit def limit[Opts](implicit
                           args: Args[Opts],
                           select: PrintLimit[Opts]): Limit = {
    if (_limit == null)
      _limit = select(args)

    _limit
  }
}

object HasPrintLimit {
  type PrintLimit[Opts] = Find[Opts, W.`'printLimit`.T, Limit]
}
