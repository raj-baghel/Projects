name := "spark"

version := "0.1"

scalaVersion := "2.11.12"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-hive
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.3.0" % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-hivecontext-compatibility
libraryDependencies += "org.apache.spark" %% "spark-hivecontext-compatibility" % "2.0.0-preview"
