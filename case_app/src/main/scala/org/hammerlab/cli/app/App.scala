package org.hammerlab.cli.app

import java.io.Closeable

import scala.collection.mutable.ArrayBuffer

abstract class App[Opts](protected val _args: Args[Opts])
  extends Closeable {

  implicit protected val _opts: Opts = _args
  implicit protected val _iargs: Args[Opts] = _args

  private val deinitializations = ArrayBuffer[() ⇒ Unit]()
  def deinit(fn: ⇒ Unit): Unit = {
    deinitializations += (() ⇒ fn)
  }

  override def close(): Unit =
    deinitializations.foreach(_())
}
