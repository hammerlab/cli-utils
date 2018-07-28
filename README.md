# cli-utils

[![Build Status](https://travis-ci.org/hammerlab/cli-utils.svg?branch=master)](https://travis-ci.org/hammerlab/spark-commands)
[![codecov](https://codecov.io/gh/hammerlab/cli-utils/branch/master/graph/badge.svg)](https://codecov.io/gh/hammerlab/cli-utils)

Interfaces for creating CLI-runnable and testable commands/apps, with [Spark](http://spark.apache.org/)- and non-Spark-focused modules.

## base
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab.cli/base_2.12.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Chammerlab.cli%20base)

Wrappers and extensions to [case-app]:

```scala
libraryDependencies += "org.hammerlab.cli" %% "base" % "1.0.1"
```

```scala
import hammerlab.cli._
```

See [examples in tests](base/src/test/scala/org/hammerlab/cli/base/app/IndexingAppTest.scala).

## spark
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab.cli/spark_2.11.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Chammerlab.cli%20spark)


```scala
libraryDependencies += "org.hammerlab.cli" %% "spark" % "1.0.1"
```

```scala
import hammerlab.cli.spark._
```

See [examples in tests](spark/src/test/scala/org/hammerlab/cli/spark/SumNumbersTest.scala).


[case-app]: https://github.com/alexarchambault/case-app
