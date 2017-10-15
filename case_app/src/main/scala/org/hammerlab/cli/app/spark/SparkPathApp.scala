package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.{ Args, ArgsOutPathApp, IsRegistrar, WithPrintLimit }
import org.hammerlab.spark.confs

/**
 * [[SparkApp]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
abstract class SparkPathApp[Opts, Reg: IsRegistrar](_args: Args[Opts])
  extends ArgsOutPathApp[Opts](_args)
    with SparkApp
    with WithPrintLimit[Opts]
    with confs.Kryo {
  implicitly[IsRegistrar[Reg]].apply(this)
}
