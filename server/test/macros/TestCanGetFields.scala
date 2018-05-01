package macros

import common.BaseSuite

class TestCanGetFields extends BaseSuite {

  "Macro getDeclaredFields" should {

    "work with case class" in {
      case class A(a: Int)

      "implicitly[CanGetFields[A]].getDeclaredFields" must compile
    }

    "not work with class" in {
      class A(a: Int)

      "implicitly[CanGetFields[A]].getDeclaredFields" mustNot compile
    }

    "not work with trait" in {
      trait A { val a: Int }

      "implicitly[CanGetFields[A]].getDeclaredFields" mustNot compile
    }

    "return correct declared fields" in {
      case class Student(name: String, age: Int)

      val expect = Seq("name", "age")

      implicitly[CanGetFields[Student]].getDeclaredFields must contain theSameElementsInOrderAs expect
    }

    "not return inherited fields" in {
      trait A { val a: Int = 0 }
      case class B(b: Int) extends A

      val expect = Seq("b")

      implicitly[CanGetFields[B]].getDeclaredFields must contain theSameElementsInOrderAs expect
    }
  }
}

