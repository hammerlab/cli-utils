package org.hammerlab.cli.base.app

import org.hammerlab.paths.Path
import org.hammerlab.test.matchers.lines.Line
import org.hammerlab.test.resources.File
import org.scalatest.matchers.Matcher

trait MainSuiteI {
  self: hammerlab.test.Suite ⇒

  protected val appContainer: Cmd
  protected val main = appContainer.main

  def outBasename: String = ""

  // options that come before caller-supplied arguments
  def defaultOpts(outPath: Path): Seq[Arg] = Nil

  // options that come after caller-supplied arguments
  def extraArgs(outPath: Path): Seq[Arg] = Seq(outPath)

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

  def run(args: Seq[Arg]): Path = {
    val outPath =
      if (outBasename.nonEmpty)
        tmpPath(outBasename)
      else
        tmpPath()

    appContainer.main.apply(
      defaultOpts(outPath) ++
      args ++
      extraArgs(outPath): _*
    )

    outPath
  }

  def shouldMatch[T](args: Seq[Arg], actual: Path ⇒ T, matcher: Matcher[T]): Unit = {
    val outPath = run(args)
    actual(outPath) should matcher
  }
}

abstract class MainSuite(protected val appContainer: Cmd)
  extends hammerlab.test.Suite
    with MainSuiteI
