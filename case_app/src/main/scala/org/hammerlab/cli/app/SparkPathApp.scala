package org.hammerlab.cli.app

import caseapp.Parser
import caseapp.core.Messages
import org.hammerlab.cli.args.OutputArgs
import org.hammerlab.io.{ Printer, SampleSize }
import org.hammerlab.spark.confs

/**
 * [[SparkApp]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
/*
abstract class SparkPathApp[Args <: PathAppArgs : Parser : Messages, Reg: IsRegistrar]
  extends PathApp[Args]
    with SparkApp[Args]
    with confs.Kryo {

  implicitly[IsRegistrar[Reg]].apply(this)

  @transient implicit var printer: Printer = _
  @transient implicit var printLimit: SampleSize = _

  override def init(options: Args): Unit = {
    val OutputArgs(printLim, path, overwrite) = options.output

    if (path.exists(_.exists) && !overwrite)
      throw new IllegalArgumentException(
        s"Output path $path exists and overwrite (-f) not set"
      )

    printer = Printer(path)
    printLimit = printLim
  }
}
*/
