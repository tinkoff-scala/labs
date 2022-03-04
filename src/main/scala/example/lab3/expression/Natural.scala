package example.lab3.expression

sealed trait Natural
object Natural {
  case class Z() extends Natural
  case class N(natural: Natural) extends Natural

  def evaluate(natural: Natural): Int = natural match {
    case Z() => 0
    case N(natural) => 1 + evaluate(natural)
  }
}

sealed trait Expression
object Expression {
  case class Add(n1: Natural, n2: Natural) extends Expression
  case class Multiply(n1: Natural, n2: Natural) extends Expression

  def evaluate(expression: Expression): Int =
    expression match {
      case Add(n1, n2) => Natural.evaluate(n1) + Natural.evaluate(n2)
      case Multiply(n1, n2) => Natural.evaluate(n1) * Natural.evaluate(n2)
    }
}
