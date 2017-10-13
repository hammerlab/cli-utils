package org.hammerlab.cli.app

import java.io.Closeable

import caseapp.core.Messages
import caseapp.{ CaseApp, Parser, RemainingArgs }

import scala.collection.mutable.ArrayBuffer

abstract class CApp[Opts : Parser : Messages, Ap <: App[Opts]](make: Args[Opts] ⇒ Ap)
  extends CaseApp[Opts] {
  override def run(opts: Opts, args: RemainingArgs): Unit =
    make(
      Args(
        opts,
        args.remainingArgs
      )
    )
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
