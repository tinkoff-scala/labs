package example.lab8_concurrency.slides

import cats.implicits._
import cats.effect._
import cats.effect.std.CountDownLatch
import cats.effect.IOApp
import cats.effect.std.Console

object CountDownLatchApp extends IOApp{
  override def run(args: List[String]): IO[ExitCode] =
    for {
      c <- CountDownLatch[IO](2)
      f <- (c.await >> Console[IO].println("Countdown latch unblocked")).start
      _ <- Console[IO].println("Decreasing latch by one")
      _ <- c.release
      _ <- Console[IO].println("Before latch is unblocked")
      _ <- c.release
      _ <- f.join
    } yield ExitCode.Success
}
