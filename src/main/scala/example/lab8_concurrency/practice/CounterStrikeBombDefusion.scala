package example.lab8_concurrency.practice

import cats.effect.std.Random
import cats.effect.{ExitCode, IO, IOApp}

object CounterStrikeBombDefusion extends IOApp {

  val bombMinTimer = 3
  val bombMaxTimer = 8

  def plantBomb(implicit random: Random[IO]): IO[Unit] =
    random.betweenInt(bombMinTimer, bombMaxTimer).flatMap { timer =>
      ???
    }

  def defuseBomb(implicit random: Random[IO]): IO[Unit] =
    random.betweenInt(bombMinTimer, bombMaxTimer).flatMap { timer =>
      ???
    }

  override def run(args: List[String]): IO[ExitCode] = {
    Random.scalaUtilRandom[IO].flatMap { implicit random =>
      ???
    }
  }
}
