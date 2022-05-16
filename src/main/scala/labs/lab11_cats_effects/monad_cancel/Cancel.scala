package labs.lab11_cats_effects.monad_cancel

import cats.effect.kernel.Temporal
import cats.syntax.functor._

import scala.concurrent.TimeoutException
import scala.concurrent.duration.FiniteDuration

object Cancel {
  def cancelOnTimeout[F[_]: Temporal, A](timeout: FiniteDuration)(fa: F[A]): F[Either[TimeoutException, A]] =
    Temporal[F].race(Temporal[F].sleep(timeout).as(new TimeoutException()), fa)
}
