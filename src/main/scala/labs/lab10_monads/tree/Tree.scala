package labs.lab10_monads.tree

import cats.Monad


/**
 * Let’s write a Monad for Tree data type. Verify that the code works on instances of Branch and Leaf, and that the
 * Monad provides Functor‐like behaviour for free. Also verify that having a Monad in scope allows us to use for comprehensions,
 * despite the fact that we haven’t directly implemented flatMap or map on Tree
 */

sealed trait Tree[+A]
object Tree {
  final case class Branch[A](left: Tree[A], right: Tree[A])
    extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  implicit val monad: Monad[Tree] = new Monad[Tree] {
    override def pure[A](x: A): Tree[A] =
      leaf(x)

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
      fa match {
        case Branch(left, right) => branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) => f(value)
      }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = ???
  }
}

object KekTree extends App {
  import Tree._
  val t = Monad[Tree].tailRecM(2)(num => if (num < 1) Leaf(Right("Kek")) else Leaf(Left(num - 1)))
  println(t)
}
