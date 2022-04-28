package example.lab9_streaming

import cats.effect.std.Queue
import cats.effect.{ExitCode, IO, IOApp}
import fs2._
import Movies._

import scala.concurrent.duration._

object Concurrency extends IOApp {
  val spiderMen = Stream(spiderMan, spiderMan2, spiderMan3)
  val avengersAll = Stream(avengers, avengersUltron, avengersInfinityWar, avengersFinal)


  // We can merge two streams
  val merged = {

    val meteredSpiderMen = spiderMen.covary[IO].evalTap(_ => IO.sleep(200.millis))
    val meteredAvengers = avengersAll.covary[IO].evalTap(_ => IO.sleep(400.millis))

    meteredSpiderMen.merge(meteredAvengers).evalTap(IO.println)
  }


  val queue: IO[Queue[IO, Movie]] = Queue.bounded[IO, Movie](10)

  // Or two streams can be started `concurrently`. In this case the output of the second stream will be discarded
  val concurrentProducerConsumer: Stream[IO, Unit] =
    Stream.eval(queue).flatMap { q =>
      val producer: Stream[IO, Unit] =
        Stream.iterable(all)
          .evalTap(movie => IO.println(s"[${Thread.currentThread().getName}] produced $movie"))
          .evalMap(q.offer)
          .metered(1.second)

      val consumer: Stream[IO, Unit] =
        Stream.fromQueueUnterminated(q)
          .evalMap(movie => IO.println(s"[${Thread.currentThread().getName}] consumed $movie"))

      producer.concurrently(consumer)
    }

  val toConsoleWithThread: Pipe[IO, Movie, Unit] = in =>
    in.evalMap(movie => IO.println(s"[${Thread.currentThread().getName}] $movie"))

  // Or any number of streams can be started in parallel, with or without concurrency limit
  val justConcurrentStreams =
    Stream(
      spiderMen.through(toConsoleWithThread),
      avengersAll.through(toConsoleWithThread)
    ).parJoin(4)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- merged.compile.drain
      _ <- concurrentProducerConsumer.compile.drain
      _ <- justConcurrentStreams.compile.drain
    } yield ExitCode.Success
}
