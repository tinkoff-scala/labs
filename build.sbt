import Dependencies._

ThisBuild / scalaVersion     := "2.13.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "labs",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      cats,
      `cats-effect`,
      fs2,
      "com.sksamuel.avro4s" %% "avro4s-core" % "3.0.6",
      "com.sksamuel.avro4s" %% "avro4s-kafka" % "3.0.6"
    ),
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
  )
