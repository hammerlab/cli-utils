package org.hammerlab.cli.app

import caseapp.CaseApp
import org.hammerlab.paths.Path
import org.hammerlab.spark.test.suite
import org.hammerlab.test.matchers.files.FileMatcher.fileMatch
import org.hammerlab.test.matchers.lines.Line
import org.hammerlab.test.resources.File
import org.hammerlab.test.{ firstLinesMatch, linesMatch }
import org.scalatest.matchers.Matcher

abstract class MainSuite(app: CaseApp[_])
  extends suite.MainSuite {

  def defaultOpts(outPath: Path): Seq[Arg] = Nil
  def extraOpts: Seq[Arg] = Nil

  def defaultArgs(outPath: Path): Seq[Arg] = Nil
  def extraArgs(outPath: Path): Seq[Arg] = Seq(outPath)

  implicit def argsArray(args: Seq[Arg]): Array[String] =
    args.map(_.toString).toArray

  def checkFirstLines(args: Arg*)(lines: Line*): Unit =
    shouldMatch(
      args,
      _.read,
      firstLinesMatch(lines: _*)
    )

  def checkAllLines(args: Arg*)(lines: Line*): Unit =
    shouldMatch(
      args,
      _.read,
      linesMatch(lines: _*)
    )

  def checkFile(args: Arg*)(expectedFile: File): Unit =
    shouldMatch(
      args,
      identity,
      fileMatch(expectedFile)
    )

  def check(args: Arg*)(expected: String): Unit =
    shouldMatch(
      args,
      _.read,
      be(expected.stripMargin)
    )

  def shouldMatch[T](args: Seq[Arg], actual: Path ⇒ T, matcher: Matcher[T]): Unit = {
    val outPath = tmpPath()

    app.main(
      defaultOpts(outPath) ++
      extraOpts ++
      defaultArgs(outPath) ++
      args ++
      extraArgs(outPath)
    )

    actual(outPath) should matcher
  }
}
