package labs.lab7_error_handling

import scala.annotation.tailrec

class CalculatorEither {
  def sum[A: Numeric](a: A, b: A): A =
    Numeric[A].plus(a, b)

  def divide[A: Numeric](a: A, b: A): Either[ArithmeticException, Double] =
    Either.cond(
      Numeric[A].zero != b,
      Numeric[A].toLong(a) / Numeric[A].toLong(b),
      new ArithmeticException("b is zero")
    )

  // can only raise to a positive power
  def weakPower[A: Numeric](a: A, pow: Int): Either[IllegalArgumentException, A] = {
    @tailrec
    def rec(in: A, acc: A, times: Int): A =
      if (times == 0) acc
      else rec(in, Numeric[A].times(acc, in), times - 1)

    Either.cond(
      pow > 0,
      rec(a, Numeric[A].one, pow),
      new IllegalArgumentException("exponent should be positive")
    )

  }
}

object Test2 extends App {
  val calc = new CalculatorEither

  val sum1 = calc.sum(1, 1)
  val div2 = calc.divide(sum1, 2)
  val div0 = div2.flatMap(calc.divide(_, 1))
  val res1 = div0.flatMap(calc.weakPower(_, 0))
  println(res1)
}
