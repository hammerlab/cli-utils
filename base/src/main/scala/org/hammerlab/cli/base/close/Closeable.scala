package org.hammerlab.cli.base.close

import scala.collection.mutable.ArrayBuffer

/**
 * Mix-in allowing registration of shutdown/de-initialization callbacks via [[Closeable.deinit]]
 */
trait Closeable
  extends java.io.Closeable
    with Serializable {
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
