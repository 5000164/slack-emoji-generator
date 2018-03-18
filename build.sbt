enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

name := "Text Emoji Generator"

version := "1.0.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

scalaJSUseMainModuleInitializer := true

libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.2.0"
libraryDependencies += "com.github.japgolly.scalacss" %%% "core" % "0.5.5"
libraryDependencies += "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.5"

npmDependencies in Compile ++= Seq(
  "react" -> "16.2.0",
  "react-dom" -> "16.2.0")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
