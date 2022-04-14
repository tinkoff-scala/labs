package example.lab8_concurrency.practice

import cats.effect.std.{Console, CyclicBarrier, Random}
import cats.effect.{ExitCode, FiberIO, IO, IOApp, Temporal}
import cats.implicits._

import scala.concurrent.duration._

case class Runner private (name: String, timeToRun: FiniteDuration) {
  def joinRace(): IO[FiberIO[Unit]] =
    ???
}

object Runner {
  val fastestRunTime = 3
  val slowestRunTime = 10

  def apply(name: String)(implicit random: Random[IO]): IO[Runner] =
    random.betweenInt(fastestRunTime, slowestRunTime).map(runTime => new Runner(name, runTime.seconds))
}

object Race extends IOApp {
  def notifyRaceStart(): IO[FiberIO[Unit]] =
    ???

  override def run(args: List[String]): IO[ExitCode] = {
    val participants: List[String] = List("John", "Mike", "Husein")
    Random.scalaUtilRandom[IO].flatMap { implicit random =>
      ???
    }
  }
}
