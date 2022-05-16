package labs.lab6_typeclass
import Semigroup._

// 1. Semigroup
// 1.1. Implement all parts of the typeclass definition
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
// 1.2. Implement Semigroup for Long, String

// 1.3. Implement combineAll(list: List[A]) for non-empty lists

// combineAll(List(1, 2, 3)) == 6

// 1.4. Implement combineAll(list: List[A], startingElement: A) for all lists

// combineAll(List(1, 2, 3), 0) == 6
// combineAll(List(), 1) == 1

object Semigroup {
  def apply[A](implicit semigroup: Semigroup[A]): Semigroup[A] = semigroup

  implicit class SemigroupOps[A](val a: A) extends AnyVal {
    def |+|(other: A)(implicit semigroup: Semigroup[A]): A =
      semigroup.combine(a, other)
  }

  implicit val longSemigroup: Semigroup[Long] = (x: Long, y: Long) => x + y
  implicit val stringSemigroup: Semigroup[String] = (x: String, y: String) => x + y

  def combineAll[A: Semigroup](a: List[A]): A = a.reduceLeft(_ |+| _)
  def combineAll[A: Semigroup](a: List[A], unit: A): A = a.foldLeft(unit)(_ |+| _)
}

// 1.4. Implement Semigroup for Map[K, V] that combines its values

object MapSemigroup {
  implicit def optionSemigroup[A](implicit semigroup: Semigroup[A]): Semigroup[Option[A]] = new Semigroup[Option[A]] {
    ///(first: Option[A], second: Option[A]) => (first, second) match {case ...}
    //{case ...}

    override def combine(x: Option[A], y: Option[A]): Option[A] =
      (x, y) match {
        case (Some(val1), Some(val2)) => Some(val1 |+| val2)
        case _                        => None
      }
  }

  implicit def mapSemigroup[K, V](implicit semigroup: Semigroup[V]): Semigroup[Map[K, V]] =
    (first: Map[K, V], second: Map[K, V]) => {
      second.foldLeft(first) { case (acc, (k, v)) =>
        (acc.get(k) |+| Some(v)).fold(acc + (k -> v))(acc.updated(k, _))
      }
    }
}

object SmTest extends App {
  import Semigroup._

  val list = List(1L, 2L, 3L)
  val list2 = List(1L, 2L, 3L)

  println(combineAll(list))
  println(combineAll(list, 2L))
}
