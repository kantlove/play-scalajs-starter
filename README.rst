Play Framework and Scala JS
===========================

This is a full-stack web project with back-end using Play and front-end using Scala JS.

Comments are included everywhere to explain the code. But if that is not enough, you can refer to these sources:

=========================================== =========================================================== 
 Play ScalaJS template                       https://github.com/vmunier/play-scalajs.g8                 
 SBT plugin for ScalaJS and any web server   https://github.com/vmunier/sbt-web-scalajs                 
 ScalaJS Tutorial                            https://www.scala-js.org/tutorial/basic                    
 Play Framework project structure            https://www.playframework.com/documentation/2.6.x/Anatomy  
=========================================== =========================================================== 


How to run
----------

You must install SBT first. Then run:

.. code:: shell

  # compile
  sbt compile

  # or compile and run
  sbt server/run

To watch file changes and run automatically, you have to open sbt console first:

.. code:: shell
    
  # open SBT console
  sbt
  # prefix '~' to the command to watch file change
  > ~run
