package controllers

import common.AppSuite
import models.Subscription
import play.api.libs.json.JsArray
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class TestHome extends AppSuite {

  "Home controller GET" should {

    "render the home page" in {
      val request = FakeRequest(GET, "/")

      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home).toLowerCase() must include("takiu")
    }
  }

  "Home controller POST /subscriptions" should {

    "create a new subscription" in {
      val fakeSub = Subscription("abc@mail.com")
      val request = FakeRequest(POST, "/subscriptions")
        .withFormUrlEncodedBody("email" -> fakeSub.email)

      val create = route(app, request).get
      Await.ready(create, 10 seconds)

      inject[Home].db withTransaction { implicit conn =>
        val subs = Subscription.select()

        subs must have size 1
        subs.head.email must equal(fakeSub.email)
      }
    }

    "redirect to '/' afterward" in {
      val email = "abc@mail.com"
      val request = FakeRequest(POST, "/subscriptions").withFormUrlEncodedBody("email" -> email)

      val home = route(app, request).get

      redirectLocation(home) mustBe Some("/")
    }
  }

  "Home controller GET /subscriptions" should {

    "returns created subscriptions" in {
      val path = "/subscriptions"
      val fakeSub = Subscription("abc@mail.com")

      val createRequest = FakeRequest(POST, path).withFormUrlEncodedBody(
        "email" -> fakeSub.email
      )
      val getRequest = FakeRequest(GET, path)

      val create = route(app, createRequest).get
      Await.ready(create, 10 seconds)

      val subs = route(app, getRequest).get

      status(subs) mustBe OK
      contentType(subs) mustBe Some("application/json")

      val json = contentAsJson(subs)
      json mustBe a[JsArray]
      json match {
        case JsArray(arr) =>
          arr must have size 1
          arr.head.as[Subscription].email must equal(fakeSub.email)
        case _ => ???
      }
    }
  }
}
