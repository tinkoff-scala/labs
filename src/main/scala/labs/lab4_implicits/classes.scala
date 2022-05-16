package labs.lab4_implicits

object classes {
  object ex1 {
    def pow(base: Int, exponent: Int): Int = Math.pow(base.toDouble, exponent.toDouble).toInt

    //e.g. `concat(123, 456) == 123456`.
    def concat(a: Int, b: Int): Int = s"$a$b".toInt


    def mean(list: List[Int]): Double = list.sum / list.size

    // Что общего у всех этих методов? Они все работают с типом Int
  }

  object ex2 {
    // Как вариант, можно их объединить в один объект для инкапсуляции логики
    object RichInt {
      def pow(base: Int, exponent: Int): Int = Math.pow(base.toDouble, exponent.toDouble).toInt

      //e.g. `concat(123, 456) == 123456`.
      def concat(a: Int, b: Int): Int = s"$a$b".toInt


      def mean(list: List[Int]): Double = list.sum / list.size
    }

    // Но постоянно вызывать RichInt.$methodName кажется излишним. Хотелось бы применять эти методы сразу на числе/листе чисел
  }

  object ex3 {
    // implicits iteration
    implicit class RichIntOps(val i: Int) extends AnyVal {
      def pow(exp: Int): Int = ???
      def concat(b: Int): Int = s"$i$b".toInt
    }

    implicit class RichListIntOps(val l: List[Int]) extends AnyVal {
      def mean: Double = l.sum / l.size
    }
  }
}
