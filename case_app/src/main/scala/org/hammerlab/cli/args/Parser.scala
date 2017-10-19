package org.hammerlab.cli.args

import caseapp.core.Messages

case class Parser[Opts](parser: caseapp.Parser[Opts],
                        messages: Messages[Opts])

object Parser {
  implicit def fromCaseApp[Opts](implicit
                                 parser: caseapp.Parser[Opts],
                                 messages: Messages[Opts]): Parser[Opts] =
    Parser(parser, messages)

  implicit def toCaseAppParser[Opts](parser: Parser[Opts]): caseapp.Parser[Opts] = parser.parser
  implicit def toCaseAppMessages[Opts](parser: Parser[Opts]): Messages[Opts] = parser.messages
}
