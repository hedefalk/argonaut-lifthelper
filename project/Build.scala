import sbt._
import sbt.Keys._

object CrmBuild extends Build {


  override lazy val settings = super.settings ++ Seq(
    organization := "se.hedefalk.argonautlift",
    name := "argonaut-resthelper",
    scalaVersion := "2.11.7",
    version := "0.1",
    
    scalacOptions in Compile ++= Seq(
      "-encoding", "UTF-8",
      "-feature",
      "-deprecation",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-Xlint",
      "-Yinline-warnings",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Xfuture"
    ),
    
    publishTo := Some("Wooden Stake Releases" at "https://repo.woodenstake.se/content/repositories/releases/")
  )
  
  lazy val root = Project("root", file(".")) settings (
    libraryDependencies ++= List(
      "io.argonaut" %% "argonaut" % "6.1-M4" % "provided" ,
      "net.liftweb" %% "lift-webkit" % "3.0-M8" % "provided" 
    )
  )
  

}
