package hammerlab

import org.hammerlab.caseapp.{ annotations, parsers }
import org.hammerlab.cli.base._

package object cli
  extends annotations
    with parsers {

  type Cmd = app.Cmd
   val Cmd = app.Cmd

  type                   App[Opts] = app.                  App[Opts]
  type               PathApp[Opts] = app.              PathApp[Opts]
  type           IndexingApp[Opts] = app.          IndexingApp[Opts]
  type        ArgsOutPathApp[Opts] = app.       ArgsOutPathApp[Opts]
  type RequiredArgOutPathApp[Opts] = app.RequiredArgOutPathApp[Opts]

  type Runner[Opts] = app.Runner[Opts]
  type HasPrintLimit = app.HasPrintLimit

  type PrinterArgs = args.PrinterArgs
  type PrintLimitArgs = args.PrintLimitArgs

  object parsers extends parsers
}
