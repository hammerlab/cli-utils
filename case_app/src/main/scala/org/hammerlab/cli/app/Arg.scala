package org.hammerlab.cli.app

import org.hammerlab.paths.Path

/**
 * Simple wrapper over [[String]] and [[Path]] arguments, for [[Runner]]-calling convenience.
 */
case class Arg(override val toString: String)

object Arg {
  implicit def strArg(s: String): Arg = Arg(s)
  implicit def pathArg(path: Path): Arg = Arg(path.toString)
}
