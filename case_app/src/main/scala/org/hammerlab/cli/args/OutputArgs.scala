package org.hammerlab.cli.args

import caseapp.{ ValueDescription, ExtraName ⇒ O, HelpMessage ⇒ M }
import org.hammerlab.io.SampleSize
import org.hammerlab.paths.Path

case class OutputArgs(
  @O("l")
  @ValueDescription("num=1000000")
  @M("When collecting samples of records/results for displaying to the user, limit to this many to avoid overloading the driver")
  printLimit: SampleSize = SampleSize(1000000),

  @O("f")
  @M("Whether to overwrite the output file, if it already exists")
  overwrite: Boolean = false
)
