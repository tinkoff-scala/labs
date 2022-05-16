package labs.lab7_error_handling

import cats.data.{Validated, ValidatedNel}
import cats.syntax.apply._

class NumberValidator {
  def validatePositive(in: Int): ValidatedNel[String, Int] =
    Validated.condNel(in > 0, in, "Number must be positive")

  def validateSmall(in: Int): ValidatedNel[String, Int] =
    Validated.condNel(in < 100, in, "Number must be smaller than 100")

  def validateEven(in: Int): ValidatedNel[String, Int] =
    Validated.condNel(Math.abs(in % 2) == 0, in, "Number must be even")

  def validateOdd(in: Int): ValidatedNel[String, Int] =
    Validated.condNel(Math.abs(in % 2) == 1, in, "Number must be odd")
}

object Test3 extends App {
  val validator = new NumberValidator
  def validateEvenSmallPositive(in: Int): ValidatedNel[String, Int] =
    (
      validator.validateEven(in),
      validator.validateSmall(in),
      validator.validatePositive(in)
    ).mapN {
      case (_, _, _) => in
    }

  def validateOddPositive(in: Int): ValidatedNel[String, Int] =
    (
      validator.validateOdd(in),
      validator.validatePositive(in)
      ).mapN {
        case (_, _) => in
      }

  println(validateOddPositive(1))
  println(validateOddPositive(-1))
  println(validateOddPositive(-2))
  println(validateEvenSmallPositive(-2))
  println(validateEvenSmallPositive(1))
  println(validateEvenSmallPositive(101))
}
