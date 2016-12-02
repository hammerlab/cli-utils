name := "spark-commands"
version := "1.0.0"

providedDeps += libraries.value('spark)

libraryDependencies ++= Seq(
  "org.clapper" %% "grizzled-slf4j" % "1.0.3",
  "org.bdgenomics.utils" %% "utils-cli" % "0.2.10"
)
