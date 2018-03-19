package hammerlab

import org.hammerlab.cli.base._

package object cli {
  type Cmd = app.Cmd

  type                   App[Opts] = app.                  App[Opts]
  type               PathApp[Opts] = app.              PathApp[Opts]
  type           IndexingApp[Opts] = app.          IndexingApp[Opts]
  type        ArgsOutPathApp[Opts] = app.       ArgsOutPathApp[Opts]
  type RequiredArgOutPathApp[Opts] = app.RequiredArgOutPathApp[Opts]

  type Runner[Opts] = app.Runner[Opts]
  type HasPrintLimit = app.HasPrintLimit

  type PrinterArgs = args.PrinterArgs
  type PrintLimitArgs = args.PrintLimitArgs

  type M = caseapp.HelpMessage
  type R = caseapp.Recurse
  type O = caseapp.Name
}
