package labs.lab11_cats_effects.async

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.{Async, Resource, Temporal}
import cats.effect.std.Console
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.option._

import java.util.concurrent.CompletableFuture
import scala.concurrent.duration._

case class Request(url: String)

case class Response[Body](code: Int, body: Either[Throwable, Body])

// We want to create an asynchronous http client. Its calls to `send` can be cancelled, by stopping the connection

class HttpConnection[F[_]: Console: Temporal] {
  def open: F[HttpConnection[F]] =
    Temporal[F].sleep(1.second) >> Console[F].println("Http connection established").as(this)
  def close: F[Unit] = Temporal[F].sleep(1.second) >> Console[F].println("Http connection is closed")
}

trait HttpClient[F[_], Out] {
  def send(url: String): F[Response[Out]]
}

class HttpClientImpl[F[_]: Async](connection: HttpConnection[F], javaClient: HttpClient[CompletableFuture, String])
  extends HttpClient[F, String] {
  override def send(url: String): F[Response[String]] = {
    Async[F].bracket[HttpConnection[F], Response[String]](connection.open) {
      conn => Async[F].async { cb =>
        Async[F].delay {
          val request = javaClient.send(url)
          try {
            cb(Right(request.get()))
          } catch {
            case e: Throwable => cb(Left(e))
          }

          Async[F].delay(request.cancel(true)).void.some
        }
      }
    }(conn => conn.close)
//    Resource.make[F, HttpConnection[F]](connection.open)(conn => conn.close).use {
//      conn => Async[F].async { cb =>
//        val request = javaClient.send(url)
//        try {
//          cb(Right(request.get()))
//        } catch {
//          case e: Throwable => cb(Left(e))
//        }
//
//        Async[F].pure(Async[F].delay(request.cancel(true)).void.some)
//      }
//    }
  }
}

object T extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val javaClient: HttpClient[CompletableFuture, String] = ???
    val conn = new HttpConnection[IO]
    val client = new HttpClientImpl[IO](conn, javaClient)

    client.send("url").start.flatMap(f => f.cancel).as(ExitCode.Success)
  }
}
