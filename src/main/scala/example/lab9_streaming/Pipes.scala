package example.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import Movies._

/**
 * In most cases we want to transform our Streams during evaluation
 * Many other streaming libraries have a so called `Source`, `Pipe`, `Sink`
 * Source generates elements of the stream
 * Pipe transforms them somehow
 * Sink consumes elements
 *
 * In fs2 we only pipes `Pipe[F[_], -I, +O] = Stream[F[_], I] => Stream[F[_], O]`
 */

object Pipes extends IOApp {
  val fromMovieToString: Pipe[Pure, Movie, String] =
    (in: Stream[Pure, Movie]) => in.map(movie => s"${movie.name} released in ${movie.year}, has rating ${movie.rating}/100")

  def toConsole[T]: Pipe[IO, T, Unit] =
    in => in.evalMap(IO.println)

  /*
  def through[F2[x] >: F[x], O2](f: Stream[F, O] => Stream[F2, O2]): Stream[F2, O2] = f(this)
   */

  override def run(args: List[String]): IO[ExitCode] = {
    val allMovies = Stream.iterable(all)
    val printedMovies = allMovies through fromMovieToString through toConsole
    printedMovies.compile.drain.as(ExitCode.Success)
  }
}
