/**
  * To compile run: `sbt compile`
  * To run the server: `sbt server/run`
  *
  * @note using `sbt run` doesn't work
  */

val commonSettings = Seq(
  organization := "com.takiu",
  scalaVersion := "2.12.4",

  /**
   * flag "-unchecked" will tell us about potential problem due to type erasure
   *
   * @see https://stackoverflow.com/q/1094173/3778765
   */
  scalacOptions in Compile ++= Seq("-unchecked")
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
     * flag "-Ypartial-unification" improves type inference, is a requirement in any scala-FP project
     *
     * @see https://issues.scala-lang.org/browse/SI-2712
     *      https://gist.github.com/djspiewak/7a81a395c461fd3a09a6941d4cd040f2
     */
    scalacOptions in Compile ++= Seq("-Ypartial-unification"),

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
      evolutions,
      jdbc,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
      // this library transfer code generated by ScalaJS to be used in Play's templates
      "com.vmunier" %% "scalajs-scripts" % "1.1.1",
      // in-memory database
      "com.h2database" % "h2" % "1.4.192",
      "org.playframework.anorm" %% "anorm" % "2.6.1"
    )
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJVM, macros)

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
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
      "fr.hmil" %%% "roshttp" % "2.1.0"
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

lazy val macros = (project in file("macros"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.4",
      "org.tpolecat" %% "doobie-core" % "0.5.2"
    )
  )
