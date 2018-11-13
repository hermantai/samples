fun main(args: Array<String>) {
  println(whatShouldIDoToday("happy"))
}

fun whatShouldIDoToday(mood: String, weather: String = "sunny", temperature: Int = 24) : String {
  return when {
    mood == "happy" && weather == "Sunny" -> "go for a walk"
    else -> "Stay home and read."
  }
}