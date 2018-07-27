
default(
  subgroup("cli"),
  v"1.0.1",
  versions(
       hammerlab.io → "5.1.1",
              paths → "1.5.0",
    shapeless_utils → "1.3.0",
         spark_util → "3.0.0"
  ),
  `2.11`.only
)

lazy val base = project.settings(
  `2.12`.add,
  dep(
    case_app,
    hammerlab.io,
    paths,
    shapeless,
    shapeless_utils
  ),
  travisCoverageScalaVersion := Some(`2.11`.version.value),
  emptyDocJar,  // compiling tests for docs causes compiler stack-overflow in scala 2.12 😔
  publishTestJar  // `MainSuite` is useful in downstream libraries' tests
)

import Spark.autoImport.{ spark ⇒ sprk }

lazy val spark = project.settings(
  dep(
    case_app,
    paths,
    slf4j,
    spark_util
  ),
  sprk,
  testDeps ++= Seq(
    cats,
    magic_rdds % "4.2.3"
  ),
  publishTestJar  // `MainSuite` is useful in downstream libraries' tests
).dependsOn(
  base andTest
)

lazy val `cli-root` =
  root(
    base,
    spark
  )

github.repo("cli-utils")
