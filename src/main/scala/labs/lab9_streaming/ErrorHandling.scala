package labs.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import Movies._
import cats.effect.std.Random

/**
 * By default, if some side effect can raise error and stream does not handle it
 * the stream will end and throw this error
 */

object ErrorHandling extends IOApp {
  val streamWithError: Stream[IO, Movie] =
    Stream.iterable(all).evalMap(releaseOrThrow)

  def releaseOrThrow(movie: Movie): IO[Movie] =
    for {
      random <- Random.scalaUtilRandom[IO]
      _ <- IO.println(s"Releasing movie ${movie.name}")
      rand <- random.nextInt
      _ <-
        if (rand % 2 == 0) IO.raiseError(new RuntimeException("Broke"))
        else IO.println(s"${movie.name} is released")
    } yield movie


  // Errors can be handled using `handleErrorWith`
  // or `attempt (converts all errors in Left(error), and successful output in Right(...))
  // The stream will be terminated in case of error anyway, but the error will go up the stack.
  override def run(args: List[String]): IO[ExitCode] =
    streamWithError.handleErrorWith(e => Stream.eval(IO.println(e))).flatMap { _ =>
      streamWithError.attempt.evalMap(IO.println)
    }.compile.drain.as(ExitCode.Success)
}

