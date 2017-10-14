package org.hammerlab.cli.app

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

