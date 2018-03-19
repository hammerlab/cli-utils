package org.hammerlab.cli.base.app

import org.hammerlab.cli.base.args.Parser
import org.hammerlab.cli.base.close.Closeable
import org.hammerlab.paths.Path

/**
 * Interface for apps that take a [[Path]] and "index" it in some way, generating an output file that is by default
 * named by appending an extension to the input path.
 *
 * @param suffix if [[OutPathApp.outPath]] is empty, construct an output path by appending this string – prefixed with a
 *               "." –  to the argument [[PathApp.path input path]].
 */
case class IndexingApp[Opts : Parser](suffix: String,
                                      args: Args[Opts])(
    implicit c: Closeable
)
  extends PathApp[Opts](args)
    with OutPathApp
    with HasPrinter {
  override def outPath: Option[Path] =
    Some(
      if (args.length > 1)
        Path(args(1))
      else
        path + s".$suffix"
    )
}
