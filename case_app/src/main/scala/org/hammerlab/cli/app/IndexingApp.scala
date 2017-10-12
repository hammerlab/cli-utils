package org.hammerlab.cli.app

import caseapp.Parser
import caseapp.core.Messages
import org.hammerlab.io.Printer
import org.hammerlab.paths.Path

trait OutPathArgs {
  def out: Option[Path]
}

/**
 * Interface for apps that take a [[Path]] and "index" it in some way, generating an output file that is by default
 * named by appending an extension to the input path.
 *
 * @param defaultSuffix if [[OutPathArgs.out]] is empty, construct an output path by appending this string to the argument
 *                      value [[PathApp.path]].
 */
/*
abstract class IndexingApp[Args <: OutPathArgs : Parser : Messages](defaultSuffix: String)
  extends PathApp[Args] {
  override def init(args: Args): Unit = {
    new PrinterApp(args) {
      override def outPath(args: Args) =
        args
          .out
          .getOrElse(
            path + defaultSuffix
          )

    }
  }

  override def close(): Unit =
    printer.close()
}
*/
