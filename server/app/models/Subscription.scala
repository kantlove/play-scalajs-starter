package models

import java.time.Instant

import models.core.{BaseModel, Repository}

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
}
