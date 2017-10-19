package org.hammerlab.cli.app

import org.hammerlab.cli.app
import org.hammerlab.cli.app.close.Closeable
import org.hammerlab.cli.args.Parser

/**
 * Interface containing an `Opts` type, [[App]]-constructor, and a full command-line application derived from them.
 */
trait Cmd {
  /** Subclasses should define their CLI-options under this moniker */
  type Opts

  /** Subclasses should instantiate an [[Main]] with a [[MakeApp]] */
  def main: Main

  /** Contained [[App]]s will register shutdown callbacks herein */
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
    def apply(args: Arg*): Unit = main(args: _*)
    lazy val main: Runner[Opts] = Runner()
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

