package labs.lab5_variance

sealed trait Animal
case class Cat() extends Animal
case class Dog() extends Animal

abstract class MyList[+T] {

}
case object Nil extends MyList[Nothing]
case class Cons[+T](head: T, tail: MyList[T]) extends MyList[T]

object Example extends App {
  // Чтобы заполнить лист разными животными, сам лист должен быть ковариантным по T
  val list: MyList[Animal] = Cons(Cat(), Cons(Dog(), Nil))
}
