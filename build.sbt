name := """urbanmedia-back"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "io.swagger" %% "swagger-play2" % "1.5.3",
  "org.webjars" %% "webjars-play" % "2.5.0-4",
  "org.webjars" % "swagger-ui" % "2.2.0"
)
