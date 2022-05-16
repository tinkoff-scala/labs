package labs.lab9_streaming

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.Random
import fs2._
import Movies._

class DatabaseConnection(val connection: Int) {
  def acquire: IO[Unit] =
    IO.println(s"Acquiring connection to database: $connection")

  def release: IO[Unit] =
    IO.println(s"Released connection: $connection")
}

/**
 * Stream can also manage some `resource`: opening it on stream start before elements emit
 * and closing it at the end of the stream.
 * To create a stream as resource one should use `Stream.bracket(acquire: F[R])(release: R => F[Unit])`
 */

object ResourceManagement extends IOApp {

  val saveToDb: Pipe[IO, (DatabaseConnection, Movie), Unit] = in => {
    def savedOrError(conn: DatabaseConnection, movie: Movie) =
      for {
        random <- Random.scalaUtilRandom[IO]
        saved <- random.nextIntBounded(3)
        _ <-
          if (saved != 1) IO.println(s"Movie $movie is saved to db using connection ${conn.connection}")
          else IO.raiseError(new RuntimeException(s"Error during saving $movie"))
      } yield ()

    in.evalMap{
      case (conn, movie) => savedOrError(conn, movie)
    }
  }

  def savingStream(conn: DatabaseConnection): Stream[IO, Unit] =
    Stream.bracket(conn.acquire.as(conn))(_.release)
      .flatMap { conn =>
        Stream.iterable(all).map(movie => (conn, movie))
      }
      .through(saveToDb)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      rand <- Random.scalaUtilRandom[IO]
      conn <- rand.nextIntBounded(10).map(new DatabaseConnection(_))
      _ <- savingStream(conn).compile.drain
    } yield ExitCode.Success
}
