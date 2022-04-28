package example.lab9_streaming

import fs2.{Chunk, Stream, Pure}
import Movies._

/*
 Chunk is an indexed collection. It is used inside streams to model processing of not single element, but some small collection of them.
 Stream can be created from chunk or stream output can be grouped in chunks using stream.chunks
 */

object Chunks {
  val spiderMenChunk = Chunk(spiderMan, spiderMan2, spiderMan3)
  val ironMenChunks = Chunk(ironMan, ironMan2, ironMan3)
  val avengersChunk = Chunk(avengers, avengersUltron, avengersInfinityWar, avengersFinal)

  val allMoviesStream: Stream[Pure, Movie] = Stream.chunk(
    spiderMenChunk ++ ironMenChunks ++ avengersChunk
  )

  val allMoviesStreamChunked: Stream[Pure, Chunk[Movie]] = Stream.chunk(
    spiderMenChunk ++ ironMenChunks ++ avengersChunk
  ).chunkLimit(3)
}
