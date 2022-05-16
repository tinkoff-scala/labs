package labs.lab10_monads.retry

import cats.MonadError
import cats.syntax.applicativeError._
import cats.effect.syntax.temporal._
import cats.effect.kernel.Temporal

import scala.concurrent.duration.FiniteDuration

object Retry {
  // method calls a function until the function `f` successfully completes
  def retry[F[_]: MonadError[*[_], Throwable], A](fa: F[A], count: Int): F[A] =
    if (count > 1) fa.handleErrorWith(_ => retry(fa, count - 1))
    else fa

  def retryWithTimeout[F[_]: Temporal, A](timeout: FiniteDuration, count: Int)(fa: F[A]): F[A] =
    if (count > 1) fa.handleErrorWith(_ => retryWithTimeout(timeout, count - 1)(fa).delayBy(timeout))
    else fa

  def retryWithTimeoutAndBackoff[F[_]: Temporal, A](timeout: FiniteDuration, backoff: Double, count: Int)(
    fa: F[A]
  ): F[A] =
    if (count > 1)
      fa.handleErrorWith(_ =>
        retryWithTimeoutAndBackoff(
          (timeout * backoff).asInstanceOf[FiniteDuration],
          backoff,
          count - 1)
        (fa)
          .delayBy((timeout * backoff).asInstanceOf[FiniteDuration])
      )
    else fa
}
