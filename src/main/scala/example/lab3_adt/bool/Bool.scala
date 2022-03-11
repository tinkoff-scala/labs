package example.lab3_adt.bool

sealed trait Bool {
  def and(other: Bool): Bool
  def or(other: Bool): Bool
  def negate(): Bool
}
object Bool {
  case class True() extends Bool {
    override def and(other: Bool): Bool = other

    override def or(other: Bool): Bool = True()

    override def negate(): Bool = False()
  }
  case class False() extends Bool {
    override def and(other: Bool): Bool = False()

    override def or(other: Bool): Bool = other

    override def negate(): Bool = True()
  }
}
