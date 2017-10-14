package org.hammerlab.cli.app

import java.io.Closeable

import caseapp.core.Messages
import caseapp.{ CaseApp, Parser, RemainingArgs }
import org.hammerlab.paths.Path

import scala.collection.mutable.ArrayBuffer

case class Arg(override val toString: String)

object Arg {
  implicit def strArg(s: String): Arg = Arg(s)
  implicit def pathArg(path: Path): Arg = Arg(path.toString)
}

abstract class CApp[Opts : Parser : Messages, Ap <: App[Opts]](make: Args[Opts] ⇒ Ap)
  extends CaseApp[Opts] {

  def main(args: Array[Arg]): Unit = main(args.map(_.toString))

  override def run(opts: Opts, args: RemainingArgs): Unit =
    make(
      Args(
        opts,
        args.remainingArgs
      )
    )
    .close()
}

abstract class App[Opts](protected val _args: Args[Opts])
  extends Closeable {

  private val deinitializations = ArrayBuffer[() ⇒ Unit]()
  def deinit(fn: ⇒ Unit): Unit = {
    deinitializations += (() ⇒ fn)
  }

  override def close(): Unit =
    deinitializations.foreach(_())
}
