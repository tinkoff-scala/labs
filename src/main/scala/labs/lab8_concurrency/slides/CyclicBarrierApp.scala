package labs.lab8_concurrency.slides

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import cats.effect.std.{CyclicBarrier, Console}
import scala.concurrent.duration._

object CyclicBarrierApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      b <- CyclicBarrier[IO](2)
      f1 <- {
        Console[IO].println("fast fiber before barrier") >>
          b.await >>
          Console[IO].println("fast fiber after barrier")
      }.start
      f2 <- (IO.sleep(1.second) >>
        Console[IO].println("slow fiber before barrier") >>
        IO.sleep(1.second) >>
        b.await >>
        Console[IO].println("slow fiber after barrier")
        ).start
      _ <- (f1.join, f2.join).tupled
    } yield ExitCode.Success
}
