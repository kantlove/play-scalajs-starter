package models.core

import macros.CanGetFields

object SQLBuilder {
  def select[A: CanGetFields](tableName: String): String = {
    val fields = implicitly[CanGetFields[A]].getDeclaredFields

    println(fields)

    val columns = fields.mkString(", ")

    s"select $columns from $tableName"
  }

  def insert[A: CanGetFields](tableName: String): String = {
    val fields = implicitly[CanGetFields[A]].getDeclaredFields

    val columns = fields.mkString(", ")
    val commas = Seq.fill(fields.length)('?').mkString(", ")

    s"""
       |insert into $tableName ($columns)
       |values ($commas)
     """.stripMargin
  }
}
