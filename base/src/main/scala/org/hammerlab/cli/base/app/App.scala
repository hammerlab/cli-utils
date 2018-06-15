package org.hammerlab.cli.base.app

import org.hammerlab.cli.base.close._

abstract class App[Opts](protected val _args: Args[Opts])(
  implicit val container: Closeable
)
  extends CloseableProxy
     with Serializable {

  implicit protected val opts = _args.opts
  implicit protected val _iargs: Args[Opts] = _args

  /**
   * Optionally wrap functionality in this method, if e.g. this [[App]] is expected to be serialized and some
   * things shouldn't run on a deserialized version.
   *
   * For example: code referencing a [[org.apache.spark.SparkContext]] shouldn't re-run in an [[App]] instance
   * that's been sent to a Spark executor as part of a task closure).
   *
   * An example of logic to *not* wrap in [[run]] is implicit fields that are to be automatically in-scope in
   * subclasses.
   */
  def run(): Unit = {}
}
