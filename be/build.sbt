name := "be"

version := "1.0"

scalaVersion := "2.12.1"

/* Dependencies */

val jsoup = "org.jsoup" % "jsoup" % "1.10.1"
val commonsIo = "commons-io" % "commons-io" % "2.5"
val commonsCompressor = "org.apache.commons" % "commons-compress" % "1.12"


lazy val commonSettings = Seq(
    version := "1.0",
    organization := "com.example",
    scalaVersion := "2.12.1",
    assemblyOutputPath in assembly := file("target/darktxns.jar"),
    test in assembly := {},


    libraryDependencies += jsoup,
    libraryDependencies += commonsIo,
    libraryDependencies += commonsCompressor
)

lazy val be = (project in file(".")).
    settings(commonSettings: _*).
    settings(
        mainClass in assembly := Some("com.darktxns.Main")
    )
