import sbt.Keys._
import sbt._

name := "akka-sandbox"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "io.dropwizard.metrics" % "metrics-core" % "3.1.0",
  "nl.grons" %% "metrics-scala" % "3.3.0_a2.3",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "com.typesafe.akka" % "akka-kernel_2.11" % "2.3.9",
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.9",
  "org.apache.activemq" % "activemq-client" % "5.10.1"
)

mainClass in Compile := Some("akka.kernel.Main")