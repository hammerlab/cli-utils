package org.hammerlab.commands

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.SparkContext
import org.apache.spark.serializer.KryoRegistrator
import org.hammerlab.paths.Path
import org.hammerlab.test.Suite
import org.kohsuke.args4j.Argument

class SparkCommandTest
  extends Suite {
  test("command") {
    val outFile = tmpPath()
    TestCommand.main(Array(outFile.toString()))
    outFile.lines.toList should be(
      List(
        "8mb",
        "org.hammerlab.commands.TestRegistrar"
      )
    )
  }
}

class TestArgs
  extends Args {
  @Argument(required = true)
  var outFile: String = _
}

class TestRegistrar
  extends KryoRegistrator {
  override def registerClasses(kryo: Kryo): Unit = {}
}

object TestCommand
  extends SparkCommand[TestArgs] {
  override def name: String = "test"
  override def description: String = "test command"

  sparkConf(
    "spark.kryoserializer.buffer" → "8mb"
  )


  override def registrar = classOf[TestRegistrar]

  override def run(args: TestArgs, sc: SparkContext): Unit = {
    val conf = sc.getConf
    Path(args.outFile).writeLines(
      List(
        conf.get("spark.kryoserializer.buffer"),
        conf.get("spark.kryo.registrator")
      )
    )
  }
}
