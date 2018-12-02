class SimpleSpice() {
  val name = "curry"
  val spiciness = "mild"
  val heat: Int
    get() {
      return 5
    }
}

fun mainForClasses() {
  val simpleSpice = SimpleSpice()
  println("${simpleSpice.name} ${simpleSpice.heat}")
}

class Spice(var name: String, var spiciness: String = "mild") {
  val heat: Int
    get() {
      return when (spiciness) {
        "mild" -> 1
        "medium" -> 3
        "spicy" -> 5
        "very spicy" -> 7
        "extremely spicy" -> 10
        else -> 0
      }
    }

  init {
    println("Spice: name=$name, spiciness=$spiciness, heat=$heat")
  }
}

// It's better to use helper methods than second constructors
fun makeSalt() = Spice("Salt")

fun mainForConstructors() {
  val spices1 = listOf(
      Spice("curry", "mild"),
      Spice("pepper", "medium"),
      Spice("cayenne", "spicy"),
      Spice("ginger", "mild"),
      Spice("red curry", "medium"),
      Spice("green curry", "mild"),
      Spice("hot pepper", "extremely spicy")
  )

  val spice = Spice("cayenne", spiciness = "spicy")

  val spicelist = spices1.filter {it.heat < 5}

}

fun main(args: Array<String>) {
  mainForClasses()
  mainForConstructors()
}