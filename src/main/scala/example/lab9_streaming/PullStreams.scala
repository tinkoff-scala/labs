package example.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import Movies._

/**
 * Streams in fs2 are defined as a _pull_ type streams, meaning that Stream computes next element just in time
 * Under the hood `Stream[F, O]` is implemented using `Pull[F[_], O, R]`
 * `Pull[F[_], O, R]` represents a program that can pull output values of type `O` while computing result `R` using effect `F`.
 * `R` represents the information available after the emission of the element of type `O` that is used to emit next value.
 * That means that recursive calls is used in `Pull` model
 */

object PullStreams extends IOApp {

  val spiderManPull: Pull[Pure, Movie, Unit] =
    Pull.output1(spiderMan)

  val spiderManStream: Stream[Pure, Movie] = spiderManPull.stream

  val spiderMenPull: Pull[Pure, Movie, Unit] =
    spiderManPull.flatMap(_ => Pull.output1(spiderMan2)).flatMap(_ => Pull.output1(spiderMan3))

  val backToPullAgain = spiderManStream.pull.echo

  // We can use pulls to create filters (imagine they dont exist in Streams)
  def filterMovie(cond: Movie => Boolean): Pipe[IO, Movie, Movie] = in => {
    def go(s: Stream[IO, Movie]): Pull[IO, Movie, Unit] = {
      s.pull.uncons1.flatMap {
        case Some((head, tail)) =>
          if (cond(head)) Pull.output1(head) >> go(tail)
          else go(tail)
        case None => Pull.done
      }
    }

    go(in).stream
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val allStreams = Stream.iterable(all)
    val onlySpiderMen = filterMovie(_.name.toLowerCase.contains("spider"))

    allStreams.through(onlySpiderMen)
      .evalMap(IO.println)
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
