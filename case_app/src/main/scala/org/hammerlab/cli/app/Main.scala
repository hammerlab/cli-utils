package org.hammerlab.cli.app

import caseapp.{ CaseApp, RemainingArgs }
import org.hammerlab.cli.args.Parser

case class Main[Opts](make: Args[Opts] ⇒ App[Opts])(
    implicit
    c: Closeable,
    parser: Parser[Opts]
)
  extends CaseApp[Opts]()(parser, parser.messages) {

  def apply(args: Arg*): Unit = main(args.map(_.toString).toArray)

  override def run(opts: Opts, args: RemainingArgs): Unit =
    try {
      val app =
        make(
          Args(
            opts,
            args.remainingArgs
          )
        )

      app.run()
    } finally {
      c.close()
    }
}
