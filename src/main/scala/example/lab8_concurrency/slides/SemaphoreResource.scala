package example.lab8_concurrency.slides

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.{Console, Semaphore}
import cats.implicits._

import scala.concurrent.duration._

object SemaphoreResource extends IOApp {
  class PreciousResource(name: String, s: Semaphore[IO]) {
    def use: IO[Unit] =
      for {
        x <- s.available
        _ <- Console[IO].println(s"$name >> Availability: $x")
        _ <- s.acquire
        y <- s.available
        _ <- Console[IO].println(s"$name >> Started | Availability: $y")
        _ <- s.release.delayBy(3.seconds)
        z <- s.available
        _ <- Console[IO].println(s"$name >> Done | Availability: $z")
      } yield ()
  }

  val program: IO[Unit] =
    for {
      s  <- Semaphore[IO](1)
      r1 = new PreciousResource("R1", s)
      r2 = new PreciousResource("R2", s)
      r3 = new PreciousResource("R3", s)
      _  <- List(r1.use, r2.use, r3.use).parSequence.void
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.as(ExitCode.Success)
}
