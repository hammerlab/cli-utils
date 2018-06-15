package org.hammerlab.cli.base.app

import org.hammerlab.cli.base.close.Closeable
import org.hammerlab.paths.Path

/**
 * Base-class for [[App]]s that take a first-argument input-[[Path]]
 */
abstract class PathApp[Opts](args: Args[Opts])(implicit c: Closeable)
  extends App[Opts](args) {
  implicit val path: Path = Path(args(0))
}
