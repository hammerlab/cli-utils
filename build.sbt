name := "spark-commands"
version := "1.0.1"

providedDeps += libraries.value('spark)

libraryDependencies ++= Seq(
  libraries.value('slf4j),
  libraries.value('bdg_utils_cli)
)
