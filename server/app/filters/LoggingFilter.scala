package filters

import akka.stream.Materializer
import javax.inject.Inject
import play.api.Logger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * A Filter that log the processing time of each request.
 * Filter is much like Middleware in NodeJs.
 *
 * @see https://www.playframework.com/documentation/2.6.x/ScalaHttpFilters#A-simple-logging-filter
 */
class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader) map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      val status = result.header.status

      Logger.info(s"${requestHeader.method} ${requestHeader.uri} - $status (${requestTime}ms)")

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
