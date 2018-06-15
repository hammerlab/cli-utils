package org.hammerlab.cli.base.args

import caseapp.ValueDescription
import hammerlab.cli._
import hammerlab.lines.limit._

case class PrinterArgs(
    @O("f")
    @M("Whether to overwrite the output file, if it already exists")
    overwrite: Boolean = false
)

case class PrintLimitArgs(
    @R printerArgs: PrinterArgs,

    @O("l")
    @ValueDescription("num=1000000")
    @M("When collecting samples of records/results for displaying to the user, limit to this many to avoid overloading the driver")
    printLimit: Limit = 1000000
)
