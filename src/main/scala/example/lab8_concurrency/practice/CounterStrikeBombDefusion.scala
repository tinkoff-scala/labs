package example.lab8_concurrency.practice

import cats.effect.std.Random
import cats.effect.{Deferred, ExitCode, IO, IOApp, Temporal}

import scala.concurrent.duration._

object CounterStrikeBombDefusion extends IOApp {

  val bombMinTimer = 3
  val bombMaxTimer = 8

  def plantBomb(finalState: Deferred[IO, String])(implicit random: Random[IO]): IO[Unit] =
    random.betweenInt(bombMinTimer, bombMaxTimer).flatMap { timer =>
      for {
        _ <- IO.println(s"Bomb will explode in $timer seconds")
        _ <- IO.sleep(timer.seconds)
        _ <- finalState.complete("Bomb has exploded")
      } yield ()
    }

  def defuseBomb(finalState: Deferred[IO, String])(implicit random: Random[IO]): IO[Unit] =
    random.betweenInt(bombMinTimer, bombMaxTimer).flatMap { timer =>
      for {
        _ <- IO.println(s"Bomb will be defused in $timer seconds")
        _ <- IO.sleep(timer.seconds)
        _ <- finalState.complete("Bomb has been defused")
      } yield ()
    }

  override def run(args: List[String]): IO[ExitCode] = {
    Random.scalaUtilRandom[IO].flatMap { implicit random =>
      for {
        finalStateDef <- Deferred[IO, String]
        _ <- plantBomb(finalStateDef).start
        _ <- defuseBomb(finalStateDef).start
        finalState <- finalStateDef.get
        _ <- IO.println(finalState)
      } yield ExitCode.Success
    }
  }
}
