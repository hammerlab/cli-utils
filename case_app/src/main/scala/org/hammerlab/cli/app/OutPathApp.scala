package org.hammerlab.cli.app

import org.hammerlab.cli.app.OutPathApp.{ GetOutPath, HasOutPath }
import org.hammerlab.paths.Path
import org.hammerlab.shapeless.record.Find
import shapeless.Witness

/**
 * Mix-in for [[App]]s that parse an output-[[Path]] from options or arguments
 */
trait OutPathApp {
  def outPath: Option[Path]
}

object OutPathApp {
  type HasOutPath[Opts] = Find[Opts, Witness.`'outPath`.T, Option[Path]]
  def GetOutPath[Opts](args: Args[Opts])(implicit getOutPath: HasOutPath[Opts]): Option[Path] = getOutPath(args)

  type HasOverwrite[Opts] = Find[Opts, Witness.`'overwrite`.T, Boolean]
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
 * [[App]] with [[OutPathApp]] that gets its [[OutPathApp.outPath output path]] from its [[Opts]], which must have a
 * field named `outPath` of type [[Option]][[Path]]
 */
abstract class OptsOutPathApp[Opts: HasOutPath](args: Args[Opts])
  extends PathApp[Opts](args)
    with OutPathApp {
  self: App[Opts] ⇒
  override implicit val outPath = GetOutPath(args)
}

