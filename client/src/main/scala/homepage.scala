import org.scalajs.jquery.jQuery
import shared.Messages

import scala.concurrent.duration._
import scala.scalajs.js

object HomePage {
  def main(args: Array[String]): Unit = {
    updateText(Messages.title)
  }

  private def updateText(given: String, i: Int = 0): Unit = {
    js.timers.setTimeout(100.millis) {
      val prefix = given.substring(0, i)
      jQuery("h3").text(prefix)

      if(i <= given.length) updateText(given, i + 1)
    }
  }
}
