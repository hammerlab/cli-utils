name := "spark-commands"
version := "1.0.5-SNAPSHOT"

providedDeps += spark

deps ++= Seq(
  bdg_utils_cli % "0.2.16",
  slf4j,
  spark_util % "1.2.1"
)
