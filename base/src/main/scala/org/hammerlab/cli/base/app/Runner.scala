package org.hammerlab.cli.base.app

import caseapp.{ CaseApp, RemainingArgs }
import org.hammerlab.cli.base.app.Cmd.MakeApp
import org.hammerlab.cli.base.args.Parser
import org.hammerlab.cli.base.close.Closeable

/**
 * Wrap a [[MakeApp]] function in a full command-line-callable main() / [[CaseApp]]
 */
case class Runner[Opts: MakeApp](
  implicit
  c: Closeable,
  parse: Parser[Opts]
)
extends CaseApp[Opts]()(
  // these should both get picked up implicitly, but they're not
  parse, parse
) {

  def apply(args: Arg*): Unit = main(args.map(_.toString).toArray)

  override def run(opts: Opts, args: RemainingArgs): Unit = apply(opts, args)

  def apply(opts: Opts, args: RemainingArgs): Unit =
    try {
      val app =
        MakeApp(
          Args(
            opts,
            args.remaining
          )
        )

      /** [[App]]s may do all their work in their constructor, leaving this empty */
      app.run()
    } finally {
      c.close()
    }
}
