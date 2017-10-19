package org.hammerlab.cli.app.close

import org.hammerlab.cli.app.App

/**
 * Mix-in supporting registering [[CloseableProxy.deinit deinit]] callbacks that delegate to a [[Closeable]].
 *
 * Used on [[App]]s, which may throw exceptions during their initialization / constructor, resulting in the loss of a
 * reference to them with which to invoke any callbacks they'd registered.
 */
trait CloseableProxy {
  def container: Closeable
  def deinit(fn: ⇒ Unit): Unit = container.deinit(fn)
}
