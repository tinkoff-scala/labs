package labs.lab9_streaming

object Movies {
  val spiderMan: Movie = Movie("Spider-man", year = 2002, rating = 76)
  val spiderMan2: Movie = Movie("Spider-man 2", year = 2004, rating = 73)
  val spiderMan3: Movie = Movie("Spider-man 3", year = 2007, rating = 71)
  val ironMan: Movie = Movie("Iron man", year = 2008, rating = 79)
  val ironMan2: Movie = Movie("Iron man 2", year = 2010, rating = 69)
  val ironMan3: Movie = Movie("Iron man 3", year = 2013, rating = 71)
  val avengers: Movie = Movie("Avengers", year = 2012, rating = 79)
  val avengersUltron: Movie = Movie("Avengers: Age of Ultron", year = 2015, rating = 72)
  val avengersInfinityWar: Movie = Movie("Avengers: Infinity War", year = 2018, rating = 80)
  val avengersFinal: Movie = Movie("Avengers: Final", year = 2019, rating = 77)

  val all: List[Movie] = List(
    spiderMan,
    spiderMan2,
    spiderMan3,
    ironMan,
    ironMan2,
    ironMan3,
    avengers,
    avengersUltron,
    avengersInfinityWar,
    avengersFinal
  )
}
