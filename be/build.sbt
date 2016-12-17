name := "be"

version := "1.0"

scalaVersion := "2.12.1"

/* Dependencies */

val jsoup = "org.jsoup" % "jsoup" % "1.10.1"


lazy val commonSettings = Seq(
    version := "1.0",
    organization := "com.example",
    scalaVersion := "2.12.1",
    assemblyOutputPath in assembly := file("target/darktxns.jar"),
    test in assembly := {},
    libraryDependencies += jsoup
)

lazy val be = (project in file(".")).
    settings(commonSettings: _*).
    settings(
        mainClass in assembly := Some("com.darktxns.Main")
    )
