package example.lab7_error_handling

import scala.util.Try
import scala.util.Success
import scala.util.Failure

class CalculatorTry {
  def sum[A: Numeric](a: A, b: A): A =
    Numeric[A].plus(a, b)

  def divide[A: Numeric](a: A, b: A): Try[Double] =
    Try(Numeric[A].toLong(a) / Numeric[A].toLong(b))
}

object Test1 extends App {
  val calc = new CalculatorTry

  val sum1 = calc.sum(1L, 1L)
  val div2 = calc.divide(sum1, 2L)
  //val div0 = div2.flatMap(calc.divide(_, 0L))
  val res1 = div2.map(calc.sum(_, 3L))
  println(res1)
}
