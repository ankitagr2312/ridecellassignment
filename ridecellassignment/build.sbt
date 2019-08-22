name := "ridecellassignment"

version := "0.1"

scalaVersion := "2.11.2"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3" % "provided"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.0.1"
libraryDependencies += "org.apache.kafka" %% "kafka" % "0.10.0.1"
// https://mvnrepository.com/artifact/org.apache.spark/spark-sql-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.3"



