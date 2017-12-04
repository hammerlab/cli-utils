package org.hammerlab.cli.app

import hammerlab.indent._
import hammerlab.path._
import hammerlab.print._
import org.hammerlab.cli.app.OutPathApp.HasOverwrite

/**
 * Interface for [[App]]s that print to an output [[Path]], if one is provided, otherwise to stdout
 */
trait HasPrinter
  extends OutPathApp
    with CanPrint {

  self: App[_] ⇒

  protected implicit val _indent = tab

  /**
   * Lazily construct and cache a [[Printer]] in the presence of evidence that [[Opts]] has an `overwrite: Boolean`
   * field.
   *
   * Interpretable as a work-around for traits' inability to take evidence parameters
   */
  implicit def printer[Opts](implicit
                             args: Args[Opts],
                             getOverwrite: HasOverwrite[Opts]) = {
    if (_printer == null) {
      val overwrite = getOverwrite(args)
      _printer =
        outPath match {
          case Some(path)
            if path.exists &&
              !overwrite ⇒
            throw new IllegalArgumentException(
              s"Output path $path exists and 'overwrite' not set"
            )
          case _ ⇒
            Printer(outPath)(_indent)
        }

      deinit {
        _printer.close()
      }
    }

    _printer
  }
  @transient private var _printer: Printer = _
}
