package org.hammerlab.cli.app

import caseapp.core.Messages
import caseapp.{ CaseApp, Parser, RemainingArgs }
import grizzled.slf4j.Logging
import org.hammerlab.io.Printer
import org.hammerlab.paths.Path

import scala.collection.mutable.ArrayBuffer

/*
abstract class App[Opts : Parser : Messages]
  extends CaseApp[Opts]
    with Logging {

  final override def run(opts: Opts, args: RemainingArgs): Unit =
    args match {
      case RemainingArgs(args, Nil) ⇒
        run(
          opts,
          args
        )
      case RemainingArgs(_, unparsed) ⇒
        throw new IllegalArgumentException(
          s"Unparsed arguments: ${unparsed.mkString(" ")}"
        )
    }

  def done(): Unit = {}

  final def run(opts: Opts, args: Seq[String]): Unit =
    try {
      _run(opts, args)
    } finally {
      done()
    }

  protected def _run(opts: Opts, args: Seq[String]): Unit
}
*/

/*
abstract class ArgsApp[Opts]()(
    implicit
    opts: Opts,
    args: Seq[String]
) {

  private val initializations = ArrayBuffer[() ⇒ Unit]()
  def init(fn: ⇒ Unit): Unit = {
    initializations += (() ⇒ fn)
  }

  def run(): Unit

  private val deinitializations = ArrayBuffer[() ⇒ Unit]()
  def deinit(fn: ⇒ Unit): Unit = {
    deinitializations += (() ⇒ fn)
  }
}

trait MakeApp[Impl <: ArgsApp[Opts], Opts] {
  def apply(opts: Opts, args: Seq[String]): Impl
  def make: App[Opts] =
    new App[Opts] {
      override protected def _run(opts: Opts,
                                  args: Seq[String]): Unit = {
        apply(opts, args).run()
      }
    }
}

abstract class PrinterApp[Opts](outPath: Path)(
    implicit
    opts: Opts,
    args: Seq[String]
)
  extends ArgsApp[Opts]() {

  @transient implicit var printer = Printer(outPath)

  deinit {
    printer.close()
  }
}
*/
