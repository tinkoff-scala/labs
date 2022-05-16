package labs.lab10_monads.monads

import cats.Monad
import cats.syntax.option._
import cats.syntax.either._

import scala.annotation.tailrec

/** Left identity: calling pure and transforming the result with func is the same as calling func Right identity:
  * passing pure to flatMap is the same as doing nothing Associativity: flatMapping over two functions f and g is the
  * same as flatMapping over f and then flatMapping over g flatMap(a => f(a).flatMap(g)) === flatMap(a =>
  * f(a)).flatMap(b => g(b))
  */
trait KekMonad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def tailRecM[A, B](a: A)(fn: A => F[Either[A, B]]): F[B]

  def pure[A](value: A): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B] /// implement using pure and flatMap
}

object Monads {
  implicit val optionMonad: KekMonad[Option] = new KekMonad[Option] {
    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa.flatMap(f)

    override def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
      fn(a) match {
        case Some(Left(a))  => tailRecM(a)(fn)
        case Some(Right(b)) => b.some
        case None           => none
      }

    override def pure[A](value: A): Option[A] = value.some

    override def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      flatMap(fa)(a => pure(f(a)))
  }

  implicit def eitherMonad[E]: KekMonad[Either[E, *]] = new KekMonad[Either[E, *]] {
    override def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] =
      fa.flatMap(f)

    override def tailRecM[A, B](a: A)(fn: A => Either[E, Either[A, B]]): Either[E, B] =
      fn(a) match {
        case Left(value: E)     => value.asLeft[B]
        case Right(Left(a: A))  => tailRecM(a)(fn)
        case Right(Right(b: B)) => b.asRight[E]
      }

    override def pure[A](value: A): Either[E, A] =
      Right(value)

    override def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] =
      fa.map(f)
  }

  implicit val listMonad: KekMonad[List] = new KekMonad[List] {
    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
      fa.flatMap(f)


    override def tailRecM[A, B](a: A)(fn: A => List[Either[A, B]]): List[B] = {
      @tailrec
      def tailRecMAcc(accEither: List[Either[A, B]], accRes: List[B]): List[B] =
        accEither match {
          case Left(a) :: tail =>
            tailRecMAcc(fn(a) ++ tail, accRes)
          case Right(b) :: tail => tailRecMAcc(tail, accRes :+ b)
          case Nil => accRes
        }

      tailRecMAcc(fn(a), Nil)
    }

    override def pure[A](value: A): List[A] =
      List(value)

    override def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa.map(f)
  }
}

object Kek extends App {
  import Monads._
  val k = listMonad.tailRecM(10)(num =>
    if (num < 1) List(Right("Kek"), Right("Kek2")) else List(Left(num - 1), Left(num - 2))
  )
}
