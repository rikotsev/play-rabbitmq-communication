name := """AMQP-Transport-Module"""
organization := "com.tetracom"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)

libraryDependencies ++= Seq(
	guice,
	ws,
	"com.rabbitmq" % "amqp-client" % "5.5.0",
	"com.typesafe.akka" %% "akka-actor" % "2.5.17"
)
