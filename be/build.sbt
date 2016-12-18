name := "be"

version := "1.0"

scalaVersion := "2.12.1"

/* Dependencies */

val json4sNative = "org.json4s" %% "json4s-native" % "3.5.0"
val jsoup = "org.jsoup" % "jsoup" % "1.10.1"
val commonsIo = "commons-io" % "commons-io" % "2.5"
val s3 = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.68"


lazy val commonSettings = Seq(
    version := "1.0",
    organization := "com.example",
    scalaVersion := "2.12.1",
    assemblyOutputPath in assembly := file("target/darktxns.jar"),
    test in assembly := {},


    libraryDependencies += json4sNative,
    libraryDependencies += jsoup,
    libraryDependencies += commonsIo,
    libraryDependencies += s3
)

lazy val be = (project in file(".")).
    settings(commonSettings: _*).
    settings(
        mainClass in assembly := Some("com.darktxns.Main")
    )
