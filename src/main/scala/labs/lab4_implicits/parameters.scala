package labs.lab4_implicits

object parameters {
  case class Context(caller: String)

  object OperationsWithContext {
    def add(a: Int, b: Int, context: Context): Int = {
      println(s"Addition of $a and $b called by ${context.caller}")
      a + b
    }

    def multiply(a: Int, b: Int, context: Context): Int = {
      println(s"Multiplication of $a and $b called by ${context.caller}")
      a * b
    }

    // Что кажется здесь излишним? Можно ли как то облегчить вызовы функций?
  }

  // Так как контекст на вызов у нас всегда будет точно один,
  // и возможно он будет создан где то выше по стэку и проброшен через несколько вызовов функций
  // (например в http сервере для отслеживания пути запроса при логировании),
  // то было бы удобнее не пробрасывать его явно в каждый вызов функции
  object OperationsWithContextImpl {
    def add(a: Int, b: Int)(implicit context: Context): Int = {
      println(s"Addition of $a and $b called by ${context.caller}")
      a + b
    }

    def multiply(a: Int, b: Int)(implicit context: Context): Int = {
      println(s"Multiplication of $a and $b called by ${context.caller}")
      a * b
    }

    // Что кажется здесь излишним? Можно ли как то облегчить вызовы функций?
  }

  object Intermediary {
    def addIntermediary(a: Int, b: Int)(implicit context: Context): Int = {
      doSmth()
      OperationsWithContextImpl.add(a, b) // сюда передастся context
    }

    def doSmth() = ???
  }


  object ex1 {
    implicit val context = Context("Foo")
    Intermediary.addIntermediary(1, 2) // context неявно прокинется сначала в Intermediary.addIntermediary и дальше по цепочке вызова в OperationsWithContextImpl.add
  }
}
