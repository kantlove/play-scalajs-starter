package client

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.body.URLEncodedBody
import monix.execution.Scheduler.Implicits.global
import org.scalajs.jquery.jQuery
import shared.Messages

import scala.concurrent.duration._
import scala.scalajs.js // TODO: what's this?

object HomePage {
  def main(args: Array[String]): Unit = {
    setupUI()
  }

  def setupUI(): Unit = {
    updateText(Messages.title)

    setupHandlers()
  }

  private def updateText(given: String, i: Int = 0): Unit = {
    js.timers.setTimeout(100.millis) {
      val prefix = given.substring(0, i)
      jQuery(".page-subtitle").text(prefix)

      if(i <= given.length) updateText(given, i + 1)
    }
  }

  private def setupHandlers() = {
    jQuery("#btn-subscribe").click(subscribe _)
  }

  private def subscribe() = {
    val email = jQuery("#input-email").value().toString.trim
    val data = URLEncodedBody(
      "email" -> email
    )
    val url = "http://localhost:9000/sub" // TODO: find a way to use relative path

    println(s"Subscribing with email: $email")

    HttpRequest(url).post(data)
  }
}
