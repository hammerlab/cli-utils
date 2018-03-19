package org.hammerlab.cli.base.args

import caseapp.core.help.Help

case class Parser[Opts](parser: caseapp.Parser[Opts],
                        help: Help[Opts])

object Parser {
  implicit def fromCaseApp[Opts](implicit
                                 parser: caseapp.Parser[Opts],
                                 help: Help[Opts]): Parser[Opts] =
    Parser(parser, help)

  implicit def toCaseAppParser[Opts](parser: Parser[Opts]): caseapp.Parser[Opts] = parser.parser
  implicit def toCaseAppMessages[Opts](parser: Parser[Opts]): Help[Opts] = parser.help
}
