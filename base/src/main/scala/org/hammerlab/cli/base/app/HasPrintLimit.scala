package org.hammerlab.cli.base.app

import hammerlab.lines.limit._
import org.hammerlab.cli.base.app.HasPrintLimit.PrintLimit
import org.hammerlab.shapeless.record.Find
import shapeless.{ Witness ⇒ W }

/**
 * Mix-in for [[HasPrinter]] [[App]]s that may set an optional cap on how many items should be output from potentially
 * large collections
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
