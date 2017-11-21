
val defaults = Seq(
  organization := "org.hammerlab.cli",
  deps ++= Seq(
    slf4j,
    spark_util % "2.0.1"
  )
) ++ addSparkDeps

import Parent.{ autoImport ⇒ dep }

lazy val args4j = project.settings(
  defaults,
  version := "1.2.0",
  deps += bdg_utils_cli % "0.3.0",
  testDeps += dep.args4j
)

lazy val case_app = project.settings(
  defaults,
  name := "case-app",
  version := "2.1.1",
  deps ++= Seq(
    dep.case_app,
    io % "3.1.0",
    paths % "1.4.0",
    shapeless_utils % "1.1.0"
  ),
  testDeps ++= Seq(
    cats,
    magic_rdds % "4.0.0"
  ),
  publishTestJar  // MainSuite is useful in downstream libraries' tests
)

lazy val cli_root =
  rootProject(
    "cli-root",
    args4j,
    case_app
  )
