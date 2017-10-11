
val defaults = Seq(
  organization := "org.hammerlab.cli",
  deps ++= Seq(
    slf4j,
    spark_util % "2.0.0"
  )
) ++ addSparkDeps

lazy val args4j = project.settings(
  defaults,
  version := "1.1.1-SNAPSHOT",
  deps += bdg_utils_cli % "0.3.0",
  testDeps += Parent.autoImport.args4j
)

lazy val case_app = project.settings(
  defaults,
  name := "case-app",
  version := "1.1.0-SNAPSHOT",
  deps ++= Seq(
    Parent.autoImport.case_app,
    io % "2.0.0",
    paths % "1.3.1"
  )
)

lazy val cli_root =
  rootProject(
    "cli-root",
    args4j,
    case_app
  )
