import java.util.*

fun main(args: Array<String>) {
  println("Hello, ${args[0]}!")
  feedTheFish()
}

fun dayOfWeek() {
  println("What day is it today?")

  when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
    1 -> println("Sunday")
    2 -> println("Monday")
    3 -> println("Tuesday")
    4 -> println("Wednesday")
    5 -> println("Thursday")
    6 -> println("Friday")
    7 -> println("Saturday")
    else -> println("Invalid")
  }
}

fun feedTheFish() {
  val day = randomDay()
  val food = fishFood(day)
  println("Today is $day and the fish eat $food")


  if (shouldChangeWater(day)) {
    println("Change the water today")
  }
}

fun shouldChangeWater(
    day: String,
    temperature: Int = 22,
    dirty: Int = 20) : Boolean {
  return when {
    isTooHot(temperature) -> true
    dirty > 30 -> true
    day == "Sunday" -> true
    else -> false
  }
}

fun isTooHot(temperature: Int)= temperature > 30

fun swim(time: Int, speed: String = "fast") {
  println("Swimming $speed")
}

fun randomDay() : String {
  val week = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
  return week[Random().nextInt(7)]
}

fun fishFood (day : String) : String {
  return when(day) {
    "Monday" -> "flakes"
    "Tuesday" -> "pellets"
    "Wednesday" -> "redworms"
    "Thursday" -> "granules"
    "Friday" ->  "mosquitoes"
    "Saturday" -> "lettuce"
    "Sunday" -> "plankton"
    else -> "fasting"
  }
}

var dirty = 20

val waterFilter: (Int) -> Int = { dirty -> dirty / 2}
fun feedFish(dirty: Int) = dirty + 10

// Kotlin prefers function parameter to be the last one
fun updateDirty(dirty: Int, operation: (Int) -> Int) : Int {
  return operation(dirty)
}

fun dirtyProcessor() {
  dirty = updateDirty(dirty, waterFilter)
  dirty = updateDirty(dirty, ::feedFish)
  // combine higher order function and lambdas: last paramater called syntax
  dirty = updateDirty(dirty) {
    dirty -> dirty + 50
  }
}