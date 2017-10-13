package org.hammerlab.cli.app

import caseapp.Parser
import caseapp.core.Messages
import org.hammerlab.paths.Path

/**
 * Interface for apps that take a [[Path]] and "index" it in some way, generating an output file that is by default
 * named by appending an extension to the input path.
 *
 * @param suffix if [[OutPathApp.outPath]] is empty, construct an output path by appending this string – prefixed with a
 *               "." –  to the argument [[PathApp.path input path]].
 */
abstract class IndexingApp[Opts : Parser : Messages](suffix: String, args: Args[Opts])
  extends PathApp[Opts](args)
    with OutPathApp
    with WithPrinter[Opts] {
  override def outPath: Option[Path] =
    Some(
      path + s".$suffix"
    )
}
