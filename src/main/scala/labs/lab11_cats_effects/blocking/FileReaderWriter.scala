package labs.lab11_cats_effects.blocking

import cats.effect.std.Semaphore

import scala.io.{BufferedSource, Source}
import java.io.{FileWriter, BufferedWriter}

// Use standard scala blocking api Source.fromFile() and java.io.FileWriter, reading can be cancelled, writing cannot be
// Before reading reader should acquire semaphore (safely)

trait FileReaderWriter[F[_]] {
  def readFile(path: String): F[Seq[String]]

  def writeFile(path: String, content: Seq[String]): F[Unit]
}

class FileConcurrentReaderSingleWriter[F[_]](semaphore: Semaphore[F]) extends FileReaderWriter[F] {
  override def readFile(path: String): F[Seq[String]] = ???

  override def writeFile(path: String, content: Seq[String]): F[Unit] = ???

  /*
    def writeFile(filename: String, lines: Seq[String]): Unit = {
      val file = new File(filename)
      val bw = new BufferedWriter(new FileWriter(file))
      for (line <- lines) {
          bw.write(line)
      }
      bw.close()
    }
   */
}
