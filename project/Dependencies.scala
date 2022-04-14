import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"
  lazy val cats = "org.typelevel" %% "cats-core" % "2.7.0"
  lazy val `cats-effect` = "org.typelevel" %% "cats-effect" % "3.3.11"
}
