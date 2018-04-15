package models

import java.sql.Connection
import java.time.Instant

import anorm.RowParser

trait BaseModel {
  val id: Long = 0
  val createdAt: Instant = Instant.now()
}

trait Repository[A] {
  def parser: RowParser[A]

  def select()(implicit c: Connection): Seq[A]

  def insert(a: A)(implicit c: Connection): Unit
}