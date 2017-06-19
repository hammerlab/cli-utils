name := "spark-commands"
version := "1.0.4"

providedDeps += spark.value

deps ++= Seq(
  libs.value('bdg_utils_cli),
  libs.value('slf4j)
)
