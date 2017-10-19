package org.hammerlab.cli.app

import scala.collection.mutable.ArrayBuffer

trait Closeable
  extends java.io.Closeable {
  private val deinitializations = ArrayBuffer[() ⇒ Unit]()
  def deinit(fn: ⇒ Unit): Unit = {
    deinitializations += (() ⇒ fn)
  }

  override def close(): Unit =
    deinitializations.foreach(_())
}

object Closeable {
  def apply(): Closeable = new Closeable {}
}
