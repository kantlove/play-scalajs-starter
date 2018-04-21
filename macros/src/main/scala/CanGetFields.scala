package macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
 * The implementation here follows the "materialization of type class" pattern.
 * Basically, whenever an implicit instance of `CanGetFields[A]` is required,
 * the macro will be used by the compiler to generate that instance.
 *
 * @see https://docs.scala-lang.org/overviews/macros/implicits.html#implicit-materializers
 *      https://stackoverflow.com/a/20766153/3778765
 */
trait CanGetFields[A] {
  /**
   * Returns all fields declared in the constructor of this case class.
   */
  def getDeclaredFields: Seq[String]
}

object CanGetFields {
  implicit def materializeCanGetFields[A]: CanGetFields[A] = macro impl[A]

  def impl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[CanGetFields[A]] = {
    import c.universe._

    assertIsCaseClass[A](c)

    val declaredNames = weakTypeOf[A].decls.sorted.collect {
      case m: MethodSymbol if m.isCaseAccessor => m.name.decodedName.toString.trim
    }

    val declaredNamesExpr = c.Expr[List[String]](q"$declaredNames")

    reify {
      new CanGetFields[A] {
        override def getDeclaredFields: Seq[String] = declaredNamesExpr.splice
      }
    }
  }

  /**
   * If `A` is not a case class, stop this macro expansion immediately.
   */
  private def assertIsCaseClass[A: c.WeakTypeTag](c: blackbox.Context): Unit = {
    import c.universe._

    val sym = weakTypeOf[A].typeSymbol

    // Check if A is a case class
    if (!sym.isClass || !sym.asClass.isCaseClass) {
      c.abort(c.enclosingPosition, s"$sym is not a case class")
    }
  }
}
