package org.hammerlab.cli.app

import java.io.Closeable

import org.hammerlab.cli.app.OutPathApp.HasOverwrite
import org.hammerlab.io.{ Printer, SampleSize }
import org.hammerlab.paths.Path
import org.hammerlab.shapeless.record.Find
import shapeless.Witness

/**
 * Interface for [[App]]s that print to an output [[Path]], if one is provided, otherwise to stdout
 */
trait WithPrinter[Opts]
  extends Closeable
    with OutPathApp {
  self: App[Opts] ⇒

  private var _printer: Printer = _

  /**
   * Lazily construct and cache a [[Printer]] in the presence of evidence that [[Opts]] has an `overwrite: Boolean`
   * field.
   *
   * Interpretable as a work-around for traits' inability to take evidence parameters
   */
  implicit def printer(implicit getOverwrite: HasOverwrite[Opts]) = {
    if (_printer == null) {
      val overwrite = getOverwrite(_args)
      _printer =
        outPath match {
          case Some(path)
            if path.exists &&
              !overwrite ⇒
            throw new IllegalArgumentException(
              s"Output path $path exists and 'overwrite' not set"
            )
          case _ ⇒
            Printer(outPath)
        }
    }
    deinit {
      _printer.close()
    }
    _printer
  }
}


/**
 * Mix-in for [[WithPrinter]] [[App]]s that may set an optional cap on how many items should be output from potentially
 * large collections (e.g. [[org.apache.spark.rdd.RDD]]s).
 */
trait WithPrintLimit[Opts]
  extends WithPrinter[Opts] {
  self: App[Opts] ⇒
  private var _printLimit: SampleSize = _
  implicit def printLimit(implicit select: Find[Opts, Witness.`'printLimit`.T, SampleSize]): SampleSize = {
    if (_printLimit == null)
      _printLimit = select(_args)

    _printLimit
  }
}
