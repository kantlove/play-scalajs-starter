package models

import java.time.Instant

import models.core.{BaseModel, Repository}
import play.api.libs.json.{Json, Reads, Writes}

/**
 * Although `created_at` is already defined in [[models.core.BaseModel]],
 * we still need to explicitly include it in the constructor of each model
 * if we want it to appear in the result of the JSON parser from Play.
 *
 * @see https://www.playframework.com/documentation/2.5.x/ScalaJsonAutomated#Requirements
 */
case class Subscription(email: String, created_at: Instant = Instant.now()) extends BaseModel

object Subscription extends Repository[Subscription] {
  override def tableName: String = "subscriptions"

  /**
   * These implicits allow Play to convert between this class and json automatically.
   *
   * @see https://www.playframework.com/documentation/2.6.x/ScalaJsonAutomated#JSON-automated-mapping
   */
  implicit def jsonReader: Reads[Subscription] = Json.reads[Subscription]
  implicit def jsonWriter: Writes[Subscription] = Json.writes[Subscription]
  // TODO: maybe we can make this a macro?
}
