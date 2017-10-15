package org.hammerlab.cli.app.spark

import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.cli.app.{ Args, ArgsOutPathApp, WithPrintLimit }
import org.hammerlab.spark.confs

/**
 * [[HasSparkContext]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
abstract class PathApp[Opts](_args: Args[Opts],
                             reg: KryoRegistrator = null)
  extends ArgsOutPathApp[Opts](_args)
    with HasSparkContext
    with WithPrintLimit[Opts]
    with confs.Kryo {
  Option(reg).foreach(registrar(_))
}
