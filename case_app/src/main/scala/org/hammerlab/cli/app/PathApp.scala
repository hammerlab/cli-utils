package org.hammerlab.cli.app

import org.hammerlab.cli.app.OutPathApp._
import org.hammerlab.paths.Path

/**
 * Base-class for [[App]]s that take a first-argument input-[[Path]]
 */
abstract class PathApp[Opts](args: Args[Opts])(implicit c: Closeable)
  extends App[Opts](args) {
  implicit val path: Path = Path(args(0))
}
