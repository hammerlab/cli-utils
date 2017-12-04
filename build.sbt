
import Parent.{ autoImport ⇒ p }

val defaults = Seq(
  organization := "org.hammerlab.cli",
  dep(
    slf4j,
    spark_util % "2.0.1"
  )
) ++ addSparkDeps

lazy val args4j = project.settings(
  defaults,
  r"1.2.0",
  dep(
    bdg_utils_cli % "0.3.0",
    p.args4j tests
  )
)

lazy val case_app = project.settings(
  defaults,
  name := "case-app",
  r"2.2.0",
  dep(
    p.case_app,
    io              % "4.0.0",
    paths           % "1.4.0",
    shapeless_utils % "1.1.0"
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
