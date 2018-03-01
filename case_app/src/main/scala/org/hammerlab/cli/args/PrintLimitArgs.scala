package org.hammerlab.cli.args

import caseapp.{ Recurse, ValueDescription, HelpMessage ⇒ M, Name ⇒ O }
import hammerlab.lines.limit._

case class PrinterArgs(
    @O("f")
    @M("Whether to overwrite the output file, if it already exists")
    overwrite: Boolean = false
)

case class PrintLimitArgs(
    @Recurse printerArgs: PrinterArgs,

    @O("l")
    @ValueDescription("num=1000000")
    @M("When collecting samples of records/results for displaying to the user, limit to this many to avoid overloading the driver")
    printLimit: Limit = 1000000
)
