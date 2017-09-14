package org.hammerlab.cli.args4j

import org.apache.spark.SparkContext
import org.bdgenomics.utils.cli.Args4jBase

trait Args
  extends Args4jBase {
  def validate(sc: SparkContext): Unit = {}
}
