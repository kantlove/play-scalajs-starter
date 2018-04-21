package models.core

import java.sql.Connection

import cats.effect.IO
import cats.implicits._
import doobie.free.KleisliInterpreter
import doobie.free.connection.ConnectionIO
import doobie.util.composite.Composite
import doobie.util.fragment.Fragment
import doobie.util.update.Update
import macros.CanGetFields

trait Repository[A <: BaseModel] {

  def tableName: String

  def select()(implicit conn: Connection, comp: Composite[A], cgf: CanGetFields[A]): Seq[A] = {
    val selectSQL = SQLBuilder.select(tableName)
    val selectQuery = Fragment.const(selectSQL).query[A].to[Seq]

    execute(selectQuery)
  }

  def insert(as: A*)(implicit conn: Connection, comp: Composite[A], cgf: CanGetFields[A]): Int = {
    val insertSQL = SQLBuilder.insert(tableName)
    val insertQuery = Update[A](insertSQL).updateMany(as.toList)

    execute(insertQuery)
  }

  private def execute[B](query: ConnectionIO[B])(implicit conn: Connection): B = {
    // To convert from ConnectionIO[B] to Kleisli[IO, Connection, B]
    // which is basically Connection => IO[B]
    val converter = KleisliInterpreter[IO].ConnectionInterpreter
    query.foldMap(converter).run(conn).unsafeRunSync()
  }
}
