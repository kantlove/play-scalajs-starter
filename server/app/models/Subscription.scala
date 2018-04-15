package models

import java.sql.Connection

import anorm.Macro.ColumnNaming
import anorm.{Macro, RowParser}
import macros.SqlMacros


case class Subscription(email: String) extends BaseModel

object Subscription extends Repository[Subscription] {
  override def parser: RowParser[Subscription] =
    Macro.namedParser[Subscription](ColumnNaming.SnakeCase)

  override def select()(implicit c: Connection): Seq[Subscription] = {
    SqlMacros.select[Subscription].as(parser.*)
  }

  override def insert(a: Subscription)(implicit c: Connection): Unit = {
    SqlMacros.insert(a).executeInsert()
  }
}
