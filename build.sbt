name := "spark-commands"
version := "1.1.0-SNAPSHOT"

providedDeps += spark

deps ++= Seq(
  bdg_utils_cli % "0.3.0",
  slf4j,
  spark_util % "1.2.1"
)
