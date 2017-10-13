package org.hammerlab.cli.app

import org.hammerlab.paths.Path
import org.hammerlab.shapeless.Find
import shapeless.Witness

/**
 * Tag `opts` classes as well as [[App]]s that may parse an output-[[Path]] from options or arguments in different
 * ways
 */
trait OutPathApp {
  def outPath: Option[Path]
}

object OutPathApp {
  type HasOutPath[Opts] = Find.Aux[Opts, Witness.`'outPath`.T, Option[Path]]
  def GetOutPath[Opts](args: Args[Opts])(implicit getOutPath: HasOutPath[Opts]): Option[Path] = getOutPath(args)

  type HasOverwrite[Opts] = Find.Aux[Opts, Witness.`'overwrite`.T, Boolean]
}

