
import Parent.{ autoImport ⇒ p }

build(
  organization := "org.hammerlab.cli"
)

versions(
  spark_util → "2.0.1"
)

lazy val args4j = project.settings(
  r"1.2.0",
  dep(
    bdg_utils_cli % "0.3.0",
    p.args4j tests,
    slf4j,
    spark_util
  ),
  addSparkDeps
)

lazy val case_app = project.settings(
  name := "case-app",
  r"2.2.0",
  addSparkDeps,
  dep(
    p.case_app,
    io_utils        % "4.0.0",
    paths           % "1.4.0",
    shapeless_utils % "1.1.0",
    slf4j,
    spark_util
  ),
  testDeps ++= Seq(
    cats,
    magic_rdds % "4.1.0"
  ),
  publishTestJar  // MainSuite is useful in downstream libraries' tests
)

lazy val cli_root =
  rootProject(
    "cli-root",
    args4j,
    case_app
  )

github.repo("spark-commands")
