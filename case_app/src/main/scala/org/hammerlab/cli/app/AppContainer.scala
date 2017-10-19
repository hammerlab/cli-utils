package org.hammerlab.cli.app

import org.hammerlab.cli.args.Parser

trait AppContainer {
  implicit val closeable = Closeable()

  type Opts
  type MakeApp = Args[Opts] ⇒ App[Opts]

  def main: AppMain

  abstract class AppMain(implicit parser: Parser[Opts]) {
    def apply(args: Arg*): Unit = main(args: _*)
    lazy val main: Main[Opts] = Main(mkApp)
    def mkApp: MakeApp
  }

  object AppMain {
    def apply(_mkApp: MakeApp)(
        implicit parser: Parser[Opts]
    ): AppMain =
      new AppMain {
        override val mkApp = _mkApp
      }
  }
}

/**
 * Shorthand for declaring an [[AppContainer]] with an existing [[AppContainer.Opts Opts]] type (normally, each
 * [[AppContainer]] contains the declaration of its [[AppContainer.Opts Opts]]-type)
 */
trait Container[_Opts]
  extends AppContainer {
  type Opts = _Opts
}

