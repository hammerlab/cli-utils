package org.hammerlab.cli.app

import caseapp.{ CaseApp, Parser, RemainingArgs }
import caseapp.core.Messages

abstract class Main[Opts : Parser : Messages, Ap <: App[Opts]](make: Args[Opts] ⇒ Ap)
  extends CaseApp[Opts] {

  def apply(args: Array[Arg]): Unit = main(args.map(_.toString))

  override def run(opts: Opts, args: RemainingArgs): Unit =
    make(
      Args(
        opts,
        args.remainingArgs
      )
    )
    .close()
}
