package org.hammerlab.commands

import java.nio.file.spi.FileSystemProvider

import scala.collection.JavaConverters._

object FileSystems {
  def load(): Unit = {
    /** Hack to pick up [[FileSystemProvider]] implementations; see https://issues.scala-lang.org/browse/SI-10247. */
    val scl = classOf[ClassLoader].getDeclaredField("scl")
    scl.setAccessible(true)
    val prevClassLoader = ClassLoader.getSystemClassLoader
    scl.set(null, Thread.currentThread().getContextClassLoader)

    println(
      s"Loaded filesystems for schemes: ${FileSystemProvider.installedProviders().asScala.map(_.getScheme).mkString(",")}"
    )

    scl.set(null, prevClassLoader)
  }

}
