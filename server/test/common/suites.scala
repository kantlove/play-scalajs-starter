package common

import org.scalatest.TestData
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Injecting

import scala.language.postfixOps

/**
 * Base class for testing anything running on server side.
 * Trait [[play.api.test.Injecting]] allows creating an instance of anything
 * on the fly (see code example)
 * You can also mock out a whole application including requests, plugins etc.
 *
 * Example usage:
 * {{{
 *   class TestHome extends TestSuite {
 *     "Home controller GET" should {
 *       "render the home page" in {
 *         val request = FakeRequest(GET, "/")
 *
 *         val home = route(app, request).get
 *
 *         status(home) mustBe OK
 *         contentType(home) mustBe Some("text/html")
 *         contentAsString(home).toLowerCase() must include("takiu")
 *       }
 *     }
 *   }
 * }}}
 *
 * @see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
abstract class AppSuite extends BaseSuite with GuiceOneAppPerTest with Injecting {

  /**
   * Override `newAppForTest` for a new Application with custom parameters.
   *
   * The difference is the absence of 'DB_CLOSE_DELAY=-1' which let the database close
   * after each test.
   *
   * @see https://www.playframework.com/documentation/2.6.x/Developing-with-the-H2-Database#Prevent-in-memory-DB-reset
   */
  implicit override def newAppForTest(testData: TestData): Application = {
    new GuiceApplicationBuilder()
      .configure(Map("db.default.url" -> "jdbc:h2:mem:play;MODE=PostgreSQL;DATABASE_TO_UPPER=FALSE"))
      .build()
  }
}

/**
 * A base class for tests that don't need instances of application, controllers etc.
 */
abstract class BaseSuite extends PlaySpec

