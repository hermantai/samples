import java.util.*

fun main(args: Array<String>) {
  val rollDice = { sides: Int ->
    if (sides == 0) 0
    else Random().nextInt(sides) + 1
  }

  val rollDice2: (Int) -> Int = {sides ->
    if (sides == 0) 0
    else Random().nextInt(sides) + 1
  }

  gamePlay(12, rollDice2)
}

fun gamePlay(sides: Int, rollDice : (Int) -> Int) {
  println(rollDice(sides))
}