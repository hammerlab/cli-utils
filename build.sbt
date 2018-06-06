
default(
  subgroup("cli"),
  v"1.0.0",
  versions(
       hammerlab.io → "5.1.0",
              paths → "1.5.0",
    shapeless_utils → "1.3.0",
         spark_util → "2.0.4"
  ),
  `2.11`.only
)

lazy val base = project.settings(
  `2.12`.add,
  dep(
    case_app,
    io_utils,
    paths,
    shapeless,
    shapeless_utils
  ),
  publishTestJar  // `MainSuite` is useful in downstream libraries' tests
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
    magic_rdds % "4.2.1"
  ),
  addSparkDeps,
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
