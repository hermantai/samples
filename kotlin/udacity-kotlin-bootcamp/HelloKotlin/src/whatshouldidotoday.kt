fun main(args: Array<String>) {
  println(whatShouldIDoToday("happy"))
}

fun whatShouldIDoToday(mood: String, weather: String = "sunny", temperature: Int = 24) : String {
  return when {
    mood == "happy" && weather == "Sunny" -> "go for a walk"
    temperature < 10 -> "it is cold"
    temperature in 10..20 -> "ok weather"
    else -> "Stay home and read."
  }
}