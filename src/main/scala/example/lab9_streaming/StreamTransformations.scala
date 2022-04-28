package example.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import Movies._

/**
 * Stream can be treated as a scala collection and it has collection-like operations
 */
object StreamTransformations extends IOApp {

  // Simple transformations
  val spiderMen: Stream[Pure, Movie] = Stream(spiderMan, spiderMan2, spiderMan3)
  val ironMen: Stream[Pure, Movie] = Stream(ironMan, ironMan2, ironMan3)

  val streamsConcat = spiderMen ++ ironMen

  val transformedStream: Stream[IO, Unit] = streamsConcat.flatMap(movie => Stream.eval(IO.println(movie)))
  val lessBoilerplateTransform: Stream[IO, Unit] = streamsConcat.evalMap(IO.println(_))
  val tappedStream: Stream[IO, Movie] = streamsConcat.evalTap(IO.println)

  // Folded stream
  val filmsByYear: Stream[Pure, Map[Int, List[Movie]]] =
    streamsConcat.fold(Map.empty[Int, List[Movie]]) {
      case (acc, movie) =>
        acc + (movie.year -> (movie :: acc.getOrElse(movie.year, Nil)))
    }


  // Pay attention to types of variables
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.println("First transform")
      first <- transformedStream.compile.toList
      _ <- IO.println("Second transform")
      second <- lessBoilerplateTransform.compile.toList
      _ <- IO.println("Third transform")
      movies <- tappedStream.compile.toList
      _ <- IO.println(s"Movies from third stream: $movies")
      byYear = filmsByYear.toList.head
      _ <- IO.println(byYear)
    } yield ExitCode.Success
}
