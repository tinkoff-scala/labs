package labs.lab8_concurrency.practice

import cats.effect.kernel.Outcome.Succeeded
import cats.effect.std.{CyclicBarrier, Random}
import cats.effect.{Deferred, ExitCode, FiberIO, IO, IOApp}
import cats.implicits._

import scala.concurrent.duration._

case class Runner private (name: String, timeToRun: FiniteDuration) {
  def joinRace(raceLine: CyclicBarrier[IO]): IO[FiberIO[String]] =
    (for {
      _ <- IO.println(s"$name joined race")
      _ <- raceLine.await
      _ <- IO.sleep(timeToRun)
      _ <- IO.println(s"$name has finished in $timeToRun")
    } yield name).start
}

object Runner {
  val fastestRunTime = 3
  val slowestRunTime = 10

  def apply(name: String)(implicit random: Random[IO]): IO[Runner] =
    random.betweenInt(fastestRunTime, slowestRunTime).map(runTime => new Runner(name, runTime.seconds))
}

object Race extends IOApp {
  def notifyRaceStart(raceLine: CyclicBarrier[IO]): IO[Unit] =
    raceLine.await.flatMap(_ => IO.println("Race has started"))

  override def run(args: List[String]): IO[ExitCode] = {
    val participants: List[String] = List("John", "Mike", "Husein")
    Random.scalaUtilRandom[IO].flatMap { implicit random =>
      for {
        raceLine <- CyclicBarrier[IO](participants.size + 1)
        winnerDef <- Deferred[IO, String]
        runners <- participants.traverse(Runner(_))
        runnersFibers <- runners.traverse(_.joinRace(raceLine))
        _ <- notifyRaceStart(raceLine)
        _ <- runnersFibers.parTraverse(_.join.flatMap {
          case Succeeded(fa) => fa.flatMap(winnerDef.complete).void
          case _ => IO.unit
        })
        winner <- winnerDef.get
        _ <- IO.println(s"$winner has won")
      } yield ExitCode.Success
    }
  }
}
