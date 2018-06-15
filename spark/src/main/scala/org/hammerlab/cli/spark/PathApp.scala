package org.hammerlab.cli.spark

import Registrar.noop
import org.hammerlab.cli.base.app.{ Args, ArgsOutPathApp, HasPrintLimit }
import org.hammerlab.cli.base.close.Closeable
import org.hammerlab.spark.confs

/**
 * [[HasSparkContext]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
abstract class PathApp[Opts](_args: Args[Opts],
                             reg: Registrar = noop)(
    implicit c: Closeable
)
  extends ArgsOutPathApp[Opts](_args)
    with HasSparkContext
    with HasPrintLimit
    with confs.Kryo {
  reg.apply(this)
}
