package org.hammerlab.cli.base.app

import caseapp.RemainingArgs
import org.hammerlab.cli.base.app
import org.hammerlab.cli.base.args.Parser
import org.hammerlab.cli.base.close.Closeable

/**
 * Interface containing an `Opts` type, [[App]]-constructor, and a full command-line application derived from them.
 */
trait Cmd {
  /** Subclasses should define their CLI-options under this moniker */
  type Opts

  type Args = app.Args[Opts]
  type App = app.App[Opts]

  /** Subclasses should instantiate an [[Main]] with a [[MakeApp]] */
  def main: Main

  def main(args: Array[String]): Unit = main.main(args)

  /** Contained [[App]]s can register shutdown callbacks here */
  implicit val closeable = Closeable()

  /**
   * Shorthand for turning an [[Args]] instance (parsed by [[caseapp.CaseApp]] / [[Runner]] machinery) into an [[App]]
   * that can be run.
   */
  type MakeApp = Cmd.MakeApp[Opts]

  /**
   * Wrap a function turning an [[Args]] into an [[App]], providing a [[Runner]] instance suitable for calling from the
   * command-line
   */
  abstract class Main(implicit parser: Parser[Opts]) {
    def apply(args: Arg*): Unit = runner(args: _*)
    def apply(opts: Opts, args: RemainingArgs): Unit = runner(opts, args)
    lazy val runner: Runner[Opts] = Runner()
    implicit def mkApp: MakeApp
  }

  /**
   * Convenience-constructor
   */
  object Main {
    def apply(_mkApp: MakeApp)(
      implicit parser: Parser[Opts]
    ): Main =
      new Main {
        override val mkApp = _mkApp
      }

    implicit def toRunner(main: Main): Runner[Opts] = main.runner
  }
}

object Cmd {
  type MakeApp[Opts] = Args[Opts] ⇒ App[Opts]
  def MakeApp[Opts](args: Args[Opts])(implicit makeApp: MakeApp[Opts]): App[Opts] = makeApp(args)

  /**
   * Shorthand for declaring a [[Cmd]] with an existing [[Cmd.Opts Opts]] type (normally; each [[Cmd]] contains the
   * declaration of its [[Cmd.Opts Opts]]-type)
   */
  trait With[_Opts]
    extends app.Cmd {
    type Opts = _Opts
  }
}
