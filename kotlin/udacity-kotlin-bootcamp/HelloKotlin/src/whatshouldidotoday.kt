fun main(args: Array<String>) {
  println(whatShouldIDoToday(getMood()))
}

fun whatShouldIDoToday(mood: String, weather: String = "Sunny", temperature: Int = 24) : String {
  println("mood is $mood")
  return when {
    mood == "happy" && weather == "Sunny" -> "go for a walk"
    isFreezingCold(mood, weather, temperature) -> "stay in bed"
    temperature > 35 -> "go swimming"
    temperature in 10..20 -> "ok weather"
    temperature < 10 -> "it is cold"
    else -> "Stay home and read."
  }
}

fun getMood() : String {
  print("mood? ")
  return readLine()!!
}

fun isFreezingCold(mood: String, weather: String, temperature: Int) = mood == "sad" && weather == "rainy" && temperature == 0