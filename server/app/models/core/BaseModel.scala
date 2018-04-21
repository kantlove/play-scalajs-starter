package models.core

import java.time.Instant

trait BaseModel {
  val id: Long = 0
  val created_at: Instant
}
