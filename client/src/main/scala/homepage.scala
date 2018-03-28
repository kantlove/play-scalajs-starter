package homepage

import org.scalajs.jquery.jQuery
import scalajs._
import scala.concurrent.duration._

object HomePage {
  def main(args: Array[String]): Unit = {
    val text = "made by Play & ScalaJS"
    for(i <- 0 to text.length) {
      js.timers.setTimeout(1.second) {
        jQuery("h3").text(text.substring(0, i))
      }
    }
  }
}
