package example.lab8_concurrency.slides

import cats.effect.{ExitCode, IO, IOApp, Ref}
import cats.implicits._

object SafeCounter extends IOApp {
  var counter = 1

  def increaseRecursivelyAndPrint(add: Int): IO[Unit] = {
    if (add <= 0) IO.unit
    else {
      IO.delay {
        counter = counter + 1
        println(counter)
      }.flatMap(_ => increaseRecursivelyAndPrint(add - 1))
    }
  }

  def increaseRefRecursivelyAndPrint(counter: Ref[IO, Int], add: Int): IO[Unit] = {
    if (add <= 0) IO.unit
    else
      counter
        .updateAndGet(_ + 1)
        .flatMap(value => IO.delay(println(value)))
        .flatMap(_ => increaseRefRecursivelyAndPrint(counter, add - 1))
  }

  def runImpure(): IO[Unit] =
    for {
      first <- increaseRecursivelyAndPrint(10000).start
      second <- increaseRecursivelyAndPrint(10000).start
      third <- increaseRecursivelyAndPrint(10000).start
      _ <- (first.join, second.join, third.join).tupled
      _ <- IO.delay(println(s"final value $counter"))
    } yield ()

  def runPure(): IO[Unit] =
    for {
      ref <- Ref[IO].of(0)
      first <- increaseRefRecursivelyAndPrint(ref, 10000).start
      second <- increaseRefRecursivelyAndPrint(ref, 10000).start
      third <- increaseRefRecursivelyAndPrint(ref, 10000).start
      _ <- (first.join, second.join, third.join).tupled
      finalValue <- ref.get
      _ <- IO.delay(println(s"final value $finalValue"))
    } yield ()

  override def run(args: List[String]): IO[ExitCode] = {
    runImpure().as(ExitCode.Success)
    //    runPure().as(ExitCode.Success)
  }
}
