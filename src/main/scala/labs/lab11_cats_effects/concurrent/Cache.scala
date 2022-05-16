package labs.lab11_cats_effects.concurrent

import cats.effect.{Ref, Temporal}
import cats.effect.kernel.Concurrent
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.effect.implicits._
import labs.lab11_cats_effects.concurrent.RefCache.MillisToLive

import scala.concurrent.duration.FiniteDuration

trait Cache[F[_], K, V] {
  def get(key: K): F[Option[V]]

  def put(key: K, value: V, ttl: Option[FiniteDuration] = None): F[Unit]

  // just for fun
  def parPutMultiple(entities: Seq[(K, V, Option[FiniteDuration])]): F[Unit]
}

class RefCache[F[_]: Concurrent, K, V] private(
  state: Ref[F, Map[K, (MillisToLive, V)]],
  defaultTtl: FiniteDuration,
  parLimit: Int = 10
) extends Cache[F, K, V] {
  override def get(key: K): F[Option[V]] =
    state.get.map { mapCache =>
      mapCache.get(key).map{
        case (_ , value) => value
      }
    }

  override def put(key: K, value: V, ttl: Option[FiniteDuration] = None): F[Unit] = {
    val actualTtl = ttl.getOrElse(defaultTtl)
    state.update { mapCache =>
      mapCache + (key -> (actualTtl.toMillis, value))
    }
  }

  override def parPutMultiple(entities: Seq[(K, V, Option[FiniteDuration])]): F[Unit] =
    Concurrent[F].parTraverseN(parLimit)(entities) {
      case (key, value, ttlOpt) => put(key, value, ttlOpt)
    }.void
}

object RefCache {
  type MillisToLive = Long

  def of[F[_]: Temporal, K, V](defaultTtl: FiniteDuration,
                                 checkPeriod: FiniteDuration): F[RefCache[F, K, V]] = {
    def mapUpdate(state: Ref[F, Map[K, (MillisToLive, V)]]) =
      state.update {
        mapCache =>
          mapCache.foldLeft(Map.empty[K, (MillisToLive, V)]) {
            case (acc, (key, (ttl, value))) =>
              if (checkPeriod.toMillis >= ttl) acc
              else {
                acc + (key -> (ttl - checkPeriod.toMillis, value))
              }
          }
      }

    for {
      state <- Concurrent[F].ref(Map.empty[K, (MillisToLive, V)])
      cache = new RefCache[F, K, V](state, defaultTtl)
      _ <- (Temporal[F].sleep(checkPeriod) >> mapUpdate(state)).foreverM.start
    } yield cache
  }
}
