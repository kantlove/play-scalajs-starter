package macros

import anorm.{Row, SimpleSql}

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object SqlMacros {
  def select[A]: SimpleSql[Row] = macro selectImpl[A]

  def insert[A](a: A): SimpleSql[Row] = macro insertImpl[A]

  def selectImpl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[SimpleSql[Row]] = {
    import c.universe._

    check[A](c)

    c.Expr[SimpleSql[Row]] {
      q"""
          import anorm.SqlStringInterpolation
          SQL"select * from subscriptions"
        """
    }
  }

  def insertImpl[A: c.WeakTypeTag](c: blackbox.Context)(a: c.Expr[A]): c.Expr[SimpleSql[Row]] = {
    import c.universe._

    check[A](c)

    val fieldNames = weakTypeOf[A].members.collect {
      case m: MethodSymbol if m.isCaseAccessor =>
        m.name
    }.toSeq

    val columns = fieldNames.map(_.decodedName).mkString(", ")
    val insertPart = s"insert into subscriptions ($columns) values ("
    val commas = Seq.fill(fieldNames.size - 1)(", ")
    val close = ")"
    val fieldAccesses = fieldNames.map(name => q"${a.tree}.$name")

    c.Expr[SimpleSql[Row]] {
      q"""
          import anorm.SqlStringInterpolation
          StringContext($insertPart, ..$commas, $close).SQL(..$fieldAccesses)
        """
    }
  }

  private def check[A: c.WeakTypeTag](c: blackbox.Context): Unit = {
    import c.universe._

    val sym = weakTypeOf[A].typeSymbol

    // Check if A is a case class
    if (!sym.isClass || !sym.asClass.isCaseClass) {
      c.abort(c.enclosingPosition, s"$sym is not a case class")
    }
  }
}
