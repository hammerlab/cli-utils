package org.hammerlab.cli.base.app

import hammerlab.path._

/**
 * Simple wrapper over [[String]] and [[Path]] arguments, for [[Runner]]-calling convenience.
 */
case class Arg(override val toString: String)

object Arg {
  implicit def strArg(s: String): Arg = Arg(s)
  implicit def pathArg(path: Path): Arg = Arg(path.toString)
}
