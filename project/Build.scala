import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import sbt._

object AkkaSandboxBuild extends Build {
    lazy val root = Project(id = "akka-sandbox",
                            base = file(".")).
      enablePlugins(JavaServerAppPackaging)
}