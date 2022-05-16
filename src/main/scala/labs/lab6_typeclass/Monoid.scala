package labs.lab6_typeclass

import Semigroup._

trait Monoid[A] extends Semigroup[A] {
    def unit: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid

  def combineAll[A](l: List[A])(implicit monoid: Monoid[A]): A = l.foldLeft(Monoid[A].unit)(_ |+| _)
}

// 2. Monoid
// 2.1. Implement Monoid which provides `empty` value (like startingElement in previous example) and extends Semigroup

// 2.2. Implement Monoid for Long, String

// 2.3. Implement combineAll(list: List[A]) for all lists

// combineAll(List(1, 2, 3)) == 6

// 2.4. Implement Monoid for Option[A]

// combineAll(List(Some(1), None, Some(3))) == Some(4)
// combineAll(List(None, None)) == None
// combineAll(List()) == None

// 2.5. Implement Monoid for Function1 (for result of the function)

// combineAll(List((a: String) => a.length, (a: String) => a.toInt))        === (a: String) => (a.length + a.toInt)
// combineAll(List((a: String) => a.length, (a: String) => a.toInt))("123") === 126

object Kek {
  implicit def function1Monoid[I, O](implicit m: Monoid[O]): Monoid[I => O] = new Monoid[I => O] {
    override def unit: I => O = _ => m.unit

    override def combine(x: I => O, y: I => O): I => O =
      i => x(i) |+| y(i)
  }
}