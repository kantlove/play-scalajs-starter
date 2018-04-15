package controllers

import javax.inject._
import models.Subscription
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.Database
import play.api.libs.json.Json
import play.api.mvc._
import shared.Messages

import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's index page.
 */
@Singleton
class Home @Inject()(db: Database, parsers: PlayBodyParsers, cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page. An Action is like a
   * handler in NodeJs. The request parameter is the received request.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action {
    Logger.info("Welcome!")
    Logger.info(Messages.title)

    Ok(views.html.home.index())
  }

  def subscriptions() = Action {
    db withTransaction { implicit conn =>
      // Insert some test data
      val fakeSub = Subscription("xyz@mail.com")
      Subscription.insert(fakeSub)

      val subs = Subscription.select()

      implicit val jsonWriter = Json.writes[Subscription]
      val json = Json.toJson(subs)

      Ok(json)
    }
  }

  case class SubscriptionForm(email: String)

  private val subscriptionForm = Form {
    mapping(
      "email" -> email
    )(SubscriptionForm.apply)(SubscriptionForm.unapply)
  }

  def subscribe() = Action(parsers.form(subscriptionForm)) { implicit request: Request[SubscriptionForm] =>
    Logger.info(request.body.toString)
    val email = request.body.email

    Try {
      db withTransaction { implicit conn =>
        Subscription.insert(Subscription(email))
      }
    } match {
      case Success(_) =>
        Logger.info(s"Subscribed $email")
        Redirect(routes.Home.index())

      case Failure(e) =>
        Logger.error(e.getMessage)
        InternalServerError("Please try again later")
    }
  }
}

// TODO: add live chat. A good service: https://www.tawk.to
// TODO: add google analytic
// TODO: ensure responsiveness
// TODO: add tests
