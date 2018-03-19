
build(
  subgroup("cli"),
  v"1.0.0",
  versions(
           io_utils → "5.0.0",
              paths → "1.5.0",
    shapeless_utils → "1.2.0",
         spark_util → "2.0.4"
  )
)

lazy val base = project.settings(
  scalaVersion := scala211Version.value,
  dep(
    case_app,
    io_utils,
    paths,
    shapeless,
    shapeless_utils
  )
)

lazy val spark = project.settings(
  dep(
    case_app,
    paths,
    slf4j,
    spark_util
  ),
  testDeps ++= Seq(
    cats,
    magic_rdds % "4.2.0"
  ),
  addSparkDeps,
  publishTestJar  // MainSuite is useful in downstream libraries' tests
).dependsOn(
  base andTest
)

lazy val root =
  rootProject(
    "cli-root",
    base,
    spark
  )

github.repo("spark-commands")
