package models.core

import common.BaseSuite

class TestSQLBuilder extends BaseSuite {

  "SQL builder" should {

    "generate correct select SQL for a simple model" in {
      case class A(a: Int)

      val table = "table_of_a"
      val expect = s"select a from $table"

      SQLBuilder.select[A](table) must equal (expect)
    }

    "generate correct select SQL for a complex model" in {
      case class Student(name: String, age: Int, score: Double)

      val table = "students"
      val expect = s"select name, age, score from $table"

      SQLBuilder.select[Student](table) must equal (expect)
    }

    "generate correct insert SQL for a simple model" in {
      case class A(a: Int)

      val table = "table_of_a"
      val expect = s"insert into $table (a) values (?)"

      SQLBuilder.insert[A](table) must equal (expect)
    }

    "generate correct insert SQL for a complex model" in {
      case class Student(name: String, age: Int, score: Double)

      val table = "students"
      val expect = s"insert into $table (name, age, score) values (?, ?, ?)"

      SQLBuilder.insert[Student](table) must equal (expect)
    }
  }
}
