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
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "com.typesafe.akka" % "akka-kernel_2.11" % "2.3.9"
)

mainClass in Compile := Some("akka.kernel.Main")