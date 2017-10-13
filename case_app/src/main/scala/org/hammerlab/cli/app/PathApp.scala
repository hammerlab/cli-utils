package org.hammerlab.cli.app

import org.hammerlab.cli.app.OutPathApp._
import org.hammerlab.paths.Path

/**
 * Base-class for [[App]]s that take a first-argument input-[[Path]]
 */
abstract class PathApp[Opts](args: Args[Opts])
  extends App[Opts](args) {
  implicit val path: Path = Path(args(0))
}

/**
 * [[App]] superclass where the second argument is the output path to write to; default: stdout
 */
abstract class ArgsOutPathApp[Opts](args: Args[Opts])
  extends PathApp[Opts](args)
    with OutPathApp {
  self: App[Opts] ⇒
  override implicit val outPath =
    if (args.size > 1)
      Some(
        Path(
          args(1)
        )
      )
    else
      None
}

/**
 * [[App]] with [[OutPathApp]] that gets its [[OutPathApp.outPath output path]] from its [[Opts]]
 */
abstract class OptsOutPathApp[Opts: HasOutPath](args: Args[Opts])
  extends PathApp[Opts](args)
    with OutPathApp {
  self: App[Opts] ⇒
  override implicit val outPath = GetOutPath(args)
}
