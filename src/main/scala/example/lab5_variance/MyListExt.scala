package example.lab5_variance

/**
abstract class MyListExt[+T] {
  def contains(elem: T): Boolean = this match { // "Covariant type T occurs in contravariant position in type T of value elem". Это ошибка возникает из-за того, что функции в скале контравариантны по инпуту. Нужно ввести type bound чтобы обойти эту ошибку
    case ConsExt(x, _) if x == elem => true
    case ConsExt(x, xs) => xs.contains(x)
    case NilExt => false
  }
}
 */

abstract class MyListExt[+T] {
  def contains[B >: T](elem: B): Boolean = this match {
    case ConsExt(x, _) if x == elem => true
    case ConsExt(x, xs) => xs.contains(x)
    case NilExt => false
  }
}

case object NilExt extends MyListExt[Nothing]
case class ConsExt[+T](head: T, tail: MyListExt[T]) extends MyListExt[T]

object Ex2 extends App {
  val cat = Cat()
  val list: MyListExt[Animal] = ConsExt(cat, ConsExt(Dog(), NilExt))
  println(list.contains(cat))
}

