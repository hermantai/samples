package Spices

abstract class Spice(val name: String, val spiciness: String = "mild", color: SpiceColor): SpiceColor by color{
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
    println("Spice: name=$name, spiciness=$spiciness, heat=$heat, color=${color.color}")
  }

  abstract fun prepareSpice()
}

class Curry(name: String, spiciness : String = "extremely spice", color: SpiceColor = YellowSpiceColor) :
    Spice(name, spiciness = spiciness, color = color), Grinder {

  override fun prepareSpice() {
    grind()
  }

  override fun grind() {
    println("grind curry")
  }

  init {
    println("color is $color")
  }
}

interface Grinder {
  fun grind()
}

interface SpiceColor {
  val color: Color
}

object YellowSpiceColor : SpiceColor {
  override val color = Color.YELLOW
}

data class SpiceContainer(val spice: Spice) {
  val label = spice.name
}

enum class Color(val rgb: Int) {
  RED(0xFF0000), GREEN(0x00FF00), BLUE(0x0000FF), YELLOW(0xFFFF00);
}

fun main(args: Array<String>) {
  Curry(name = "yellow curry")

  val spiceCabinet = listOf(SpiceContainer(Curry("Yellow Curry", "mild")),
      SpiceContainer(Curry("Red Curry", "medium")),
      SpiceContainer(Curry("Green Curry", "spicy")))

  for(element in spiceCabinet) println(element.label)
}