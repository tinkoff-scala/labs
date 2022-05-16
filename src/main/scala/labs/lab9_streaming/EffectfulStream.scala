package labs.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import fs2.{Pure, Stream, Compiler}
import Movies._

object EffectfulStream extends IOApp {
  val pureStream: Stream[Pure, Movie] = Stream.iterable(all)

  /*
   We can convert pure stream to effectful one using `covary[F2[_]]`.
   It is possible, since Stream[+F[_], +O] is covariant in its F[_] and Pure[A] <: Nothing - we can cast it to any F[_]
   Pure[_] <: IO[_] => Stream[Pure, O] <: Stream[IO, O]
   */
  val ioStreamFromPure: Stream[IO, Movie] = pureStream.covary[IO]

  /*
  It is not mandatory to use IO, we can replace it with higher-kinded F[_]
   */

  def abstractEffStreamFromPure[F[_]]: Stream[F, Movie] = pureStream.covary[F]

  //def effectfulStreamToList(): List[Movie] = abstractEffStreamFromPure[IO].toList

  // We can create stream from effect: read from console
  val streamFromEffect: Stream[IO, String] = Stream.eval(IO.readLine)



  override def run(args: List[String]): IO[ExitCode] =
    streamFromEffect.compile.toList.flatMap(lines => IO.println(lines.mkString(""))).as(ExitCode.Success)
}
