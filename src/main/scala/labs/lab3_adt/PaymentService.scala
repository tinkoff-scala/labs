package labs.lab3_adt

final case class AccountNumber(value: String) extends AnyVal
final case class CardNumber(value: String) extends AnyVal
final case class ValidityDate(month: Int, year: Int)
sealed trait PaymentMethod
object PaymentMethod {
  final case class BankAccount(accountNumber: AccountNumber) extends PaymentMethod
  final case class CreditCard(cardNumber: CardNumber, validityDate: ValidityDate) extends PaymentMethod
  final case object Cash extends PaymentMethod
}

import PaymentMethod._

final case class PaymentStatus(value: String) extends AnyVal
trait BankAccountService {
  def processPayment(amount: BigDecimal, accountNumber: AccountNumber): PaymentStatus
}
trait CreditCardService {
  def processPayment(amount: BigDecimal, creditCard: CreditCard): PaymentStatus
}
trait CashService {
  def processPayment(amount: BigDecimal): PaymentStatus
}

// Exercise. Implement `PaymentService.processPayment` using pattern matching and ADTs.
class PaymentService(
                      bankAccountService: BankAccountService,
                      creditCardService: CreditCardService,
                      cashService: CashService,
                    ) {
  def processPayment(amount: BigDecimal, method: PaymentMethod): PaymentStatus = method match {
    case BankAccount(accountNumber) => bankAccountService.processPayment(amount, accountNumber)
    case card @ CreditCard(_, _) => creditCardService.processPayment(amount, card)
    case PaymentMethod.Cash => cashService.processPayment(amount)
  }
}

// Let's compare that to `NaivePaymentService.processPayment` implementation, which does not use ADTs, but
// provides roughly the same features as `PaymentService`.
// Question. What are disadvantages of `NaivePaymentService`? Are there any advantages?
trait NaivePaymentService { // Obviously a bad example!
  def processPayment(
                      amount: BigDecimal,
                      bankAccountNumber: Option[String],
                      validCreditCardNumber: Option[String],
                      isCash: Boolean,
                    ): String =
    (bankAccountNumber, validCreditCardNumber, isCash) match {
      case (Some(accountNumber), _, _) => accountNumber
      case (None, Some(card), _) => card
      case _ => "cash"
    }
}

