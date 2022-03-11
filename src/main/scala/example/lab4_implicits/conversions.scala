package example.lab4_implicits

import java.time.Instant

// Этот чисто учебный пример, использовать в проектах подобные конверсии очень нежелательно из-за возрастающей сложности и недетерминированности кода
object conversions {
  object ex1 {
    case class MyLong(i: Long) {
      def pow(exp: Byte): Long = Math.pow(i.toDouble, exp.toDouble).toLong
    }

    case class MyDouble(double: Double) extends AnyVal {
      def floor2: Long = double.toLong
      def round2: Long = double.round
    }

    implicit def double2MyDouble(d: Double): MyDouble =
      MyDouble(d)

    implicit def long2MyLong(l: Long): MyLong =
      MyLong(l)

    // How can we achieve chain calls like this 2.2D.floor.pow(3) using implicit conversions
    2.2D.floor2.pow(3)
  }

  object ex2 {
    implicit def stringToInstant(str: String): Instant =
      Instant.parse(str)

    val instantFromString: Instant = "2022-03-09T18:04:38.829Z"
  }
}