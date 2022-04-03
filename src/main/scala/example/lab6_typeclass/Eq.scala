package example.lab6_typeclass

trait Eq[T] {
  def eqv(x: T, y: T): Boolean
}

object Eq {
  // implement summoner and extension method ===
  def apply[A](implicit eq: Eq[A]): Eq[A] = eq

  implicit class EqOps[A](val a: A) extends AnyVal {
    def ===(other: A)(implicit eq: Eq[A]): Boolean = eq.eqv(a, other)
  }

  implicit val eqInt: Eq[Int] = new Eq[Int] {
    override def eqv(x: Int, y: Int): Boolean = x == y
  }

  val a: Int = 1
  val b: Int = 2
  a === b
}

case class Movie(name: String)
object Movie {
  implicit val movieEq: Eq[Movie] = (a: Movie, b: Movie) => a.name == b.name
}

object EqApp extends App {
  import Eq._

  val starWars4 = Movie("Star Wars 4")
  val starWars3 = Movie("Star Wars 3")

  println(starWars3 === starWars4)
  println(starWars4 === starWars4)
}
