package org.hammerlab.cli.app

trait CloseableProxy {
  def container: Closeable
  def deinit(fn: ⇒ Unit): Unit = container.deinit(fn)
}
