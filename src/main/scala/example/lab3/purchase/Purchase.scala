package example.lab3.purchase

import java.time.Instant

case class ProductInfo(name: String, price: BigDecimal)

case class PurchaseLine(products: Seq[ProductInfo], amount: Int)

case class Purchase(line: PurchaseLine, date: Instant)

object A extends App {
  val moloko = ProductInfo("moloko", 100.1)
  val hleb = ProductInfo("hleb", 50.34)
  val vesta = ProductInfo("lada vesta", 2_000_000)
  val line = PurchaseLine(Seq(moloko, hleb, vesta), 1)
  val line2 = PurchaseLine(Seq(moloko), 3)
  val purchase = Purchase(line, Instant.now())
  val purchase2 = Purchase(line2, Instant.now())
  val list = List(purchase, purchase2)
  val dates = list.map(_.date)

  list.map { p =>
    p.line.products.foldLeft(BigDecimal(0))(_ + _.price) * p.line.amount
  }.sum
}
