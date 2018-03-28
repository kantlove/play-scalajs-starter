val commonSettings = Seq(
  organization := "com.takiu",
  scalaVersion := "2.12.4"
)

/**
  * Server settings
  */
lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    name := "homepage-server",
    version := "0.1.0",

    /**
      * lists the ScalaJS projects whose output is used by the server
      * @see https://github.com/vmunier/sbt-web-scalajs#how-it-works
      */
    scalaJSProjects := Seq(client),

    /**
      * build ScalaJS code according to production environment
      * @see https://github.com/vmunier/sbt-web-scalajs#settings-and-tasks
      */
    pipelineStages in Assets := Seq(scalaJSPipeline),

    // trigger scalaJSPipeline when compile
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,

    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
    )
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJVM)

/**
  * Client (front-end) settings
  * @see https://www.scala-js.org/tutorial/basic
  */
lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    name := "homepage-client",
    version := "0.1.0",

    // this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    libraryDependencies ++= Seq(
      // '%%%' means this library is for ScalaJS
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)

/**
  * Shared code that is compiled for both ScalaJS and Play (using JVM)
  * @see https://www.scala-js.org/doc/project/cross-build.html
  */
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings)
lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js
