package example.lab9_streaming

import fs2.{Pure, Stream}
import Movies._

/*
As the official page of the fs2 stream library reports, its main features are:

- Functional
- Effectful
- Concurrent
- I/O (networking, files) computations in constant memory
- Stateful transformations
- Resource safety and effect evaluation

Despite all the above features, **the primary type defined by the library is only one, `Stream[F[_], O]`.
This type represents a stream that can pull values of type `O` using an effect of type `F`**.

 */

object PureStream extends App {
  // Create a stream
  val filmsStream: Stream[Pure,  Movie] = Stream(
    spiderMan,
    spiderMan2,
    spiderMan3,
    ironMan,
    ironMan2,
    ironMan3,
    avengers,
    avengersUltron,
    avengersInfinityWar,
    avengersFinal
  )

  // Also, stream can emit only 1 element or can be created from iterable
  val spiderMan1Stream: Stream[Pure, Movie] = Stream.emit(spiderMan)
  val filmsStreamFromIterable: Stream[Pure, Movie] = Stream.iterable(all)

  // Since that streams have no effect, they can be easily converted to sequence
  val listFromStream: List[Movie] = filmsStreamFromIterable.toList
  println(s"List from stream: $listFromStream")

  // Infinite stream
  val infiniteStream: Stream[Pure, Movie] = filmsStreamFromIterable.repeat
  println(s"Infinite stream: ${infiniteStream.take(15).toList}")
}
