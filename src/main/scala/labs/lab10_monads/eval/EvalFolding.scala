package labs.lab10_monads.eval

import cats.Eval

object EvalFolding {
  // What problems can arise in this implementation? How can we use cats monads to fix it
  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = {
    def foldRightEval(l: List[A], accEval: Eval[B])
                     (fEval: (A, Eval[B]) => Eval[B]): Eval[B] =
      l match {
        case head :: tail =>
          fEval(head, foldRightEval(tail, accEval)(fEval))
        case Nil => accEval
      }
    foldRightEval(as, Eval.now(acc)){
      case (a, evalB) => Eval.defer(evalB.map(fn(a, _)))
    }.value
  }
}

object T extends App {
  import EvalFolding._
  val list = List(1,2,3,4,5)
  foldRight(list, 0){
    case (a, acc) =>
      println(s"a=$a, acc=$acc")
      acc + a
  }
}
