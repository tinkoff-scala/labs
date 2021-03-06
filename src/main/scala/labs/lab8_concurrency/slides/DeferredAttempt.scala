package labs.lab8_concurrency.slides

import cats.effect.{Deferred, ExitCode, IO, IOApp}
import cats.implicits._

object DeferredAttempt extends IOApp {
  def start(d: Deferred[IO, Int]): IO[Unit] = {
    def attemptCompletion(n: Int): IO[Unit] = d.complete(n).attempt.void

    List(
      IO.race(attemptCompletion(1), attemptCompletion(2)),
      d.get.flatMap { n => IO(println(show"Result: $n")) }
    ).parSequence.void
  }

  val program: IO[Unit] =
    for {
      d <- Deferred[IO, Int]
      _ <- start(d)
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.as(ExitCode.Success)
}
