package labs.lab5_variance
/**
class Editor[-T <: Rectangle](polygon: T) {
  def changeColor(newColor: String): B = ??? // Contravariant type T occurs in covariant position in type T of value changeColor. Можно ввести type bound для обхода этого ограничения
}
abstract class Rectangle(val color: String) {
  def recolor(newColor: String): Rectangle
}
case class Square(override val color: String) extends Rectangle(color) {
  override def recolor(newColor: String): Rectangle = Square(newColor)
}
object Ex3 {
  val editor: Editor[Square] = new Editor[Rectangle](Square("black"))
  val recolored = editor.changeColor("white")
}
*/

class Editor[-T <: Rectangle](polygon: T) {
  def changeColor[B <: T](newColor: String): B = ???
}
abstract class Rectangle(val color: String) {
  def recolor(newColor: String): Rectangle
}
case class Square(override val color: String) extends Rectangle(color) {
  override def recolor(newColor: String): Rectangle = Square(newColor)
}
object Ex3 {
  val editor: Editor[Square] = new Editor[Rectangle](Square("black"))
  val recolored = editor.changeColor("white")
}