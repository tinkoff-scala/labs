package example.lab2

object Lab2CoopPractice extends App {
  // Write code which prints 0 if a given list is empty, "1, element value" if list has 1 element, whole list if more than one
  val l01 = Nil
  val l02 = List(3)
  val l03 = List(1, 2, 3)

  def printL(l: List[Int]): Unit =
    l match {
      case Nil         => println("0")
      case head :: Nil => println(s"1, $head")
      case list        => println(list)
    }

  printL(l01)
  printL(l02)
  printL(l03)
  // Write a code to iterate over a list to print the elements and calculate the sum and product of all elements of this list
  val l = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val total = l.foldLeft((0, 1)) { case ((sum, prod), i) =>
    println(i)
    (sum + i, prod * i)
  }
  println(total)

  // Write a code to flatten a given List of Lists, nested list structure
  val l1 = List("kek")
  val l2 = List("Shrek")
  val nested = List(l1, l2)
  println(nested.flatten)

  // Write a code to find the largest and smallest number from a given list
  val l4 = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val l001 = l4.max
  val l002 = l4.min

  // Write a code to remove duplicates from a given list
  val l5 = List(1, 2, 3, 4, 5, 4, 3, 2, 1, 0)
  l5.distinct

  // Write a code to find the even and odd numbers from a given list
  println(
    l5.foldLeft((List.empty[Int], List.empty[Int])) { case ((evens, odds), i) =>
      if (i % 2 == 0) (evens.appended(i), odds)
      else (evens, odds.appended(i))
    }
  )
  // Write a code to add each element n times to a given list of integers
  val l7 = List(1, 2, 3)
  val l8 = List(4, 5, 6)
  val n = 6
  println(l7.flatMap(i => List.fill(n)(i)) ++ l8)

  // Write code that collapses Map[Int, String] in two strings: first - concatenation of string values with even keys, second - with odd keys
  val map1 = Map(1 -> "Shrek", 2 -> "Fiona", 3 -> "Farquad", 4 -> "Dragon")
  println(
    map1.foldLeft(("", "")) { case ((evensString, oddsString), (key, value)) =>
      if (key % 2 == 0) (evensString + " " + value, oddsString)
      else (evensString, oddsString + " " + value)
    }
  )

  // Write code that filters out Map[Int, Int] values with odd keys and return squares of left values
  val map2 = Map(1 -> 1, 2 -> 2, 3 -> 3, 4 -> 4)
  println(
    map2.collect {
      case (key, value) if key % 2 == 1 => (key, value * value)
    }
  )
}
