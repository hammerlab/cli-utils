package org.hammerlab.cli.app

import caseapp.{ CaseApp, RemainingArgs }
import org.hammerlab.cli.app.Cmd.MakeApp
import org.hammerlab.cli.app.close.Closeable
import org.hammerlab.cli.args.Parser

/**
 * Wrap a [[MakeApp]] function in a full command-line-callable main() / [[CaseApp]]
 */
case class Runner[Opts: MakeApp](
    implicit
    c: Closeable,
    parse: Parser[Opts]
)
  extends CaseApp[Opts]()(parse, parse.messages) {

  def apply(args: Arg*): Unit = main(args.map(_.toString).toArray)

  override def run(opts: Opts, args: RemainingArgs): Unit = apply(opts, args)

  def apply(opts: Opts, args: RemainingArgs): Unit =
    try {
      val app =
        MakeApp(
          Args(
            opts,
            args.remainingArgs
          )
        )

      /** [[App]]s may do all their work in their constructor, leaving this empty */
      app.run()
    } finally {
      c.close()
    }
}
