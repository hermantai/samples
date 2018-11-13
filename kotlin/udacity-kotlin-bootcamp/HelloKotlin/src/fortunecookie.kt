fun main(args: Array<String>) {
  println("Your fortune is: ${getFortuneCookie(getBirthday())}")
}

fun getBirthday() : Int {
  print("Enter your birthday: ")
  return readLine()?.toIntOrNull() ?: 1
}

fun getFortuneCookie(birthday: Int) : String {
  val cookies = listOf("You will have a great day!",
      "Things will go well for you today.",
      "Enjoy a wonderful day of success.",
      "Be humble and all will turn out well.",
      "Today is a good day for exercising restraint.",
      "Take it easy and enjoy life!",
      "Treasure your friends because they are your greatest fortune.")

  val index = when (birthday) {
    in 1..7 -> 4
    14, 17 -> 5
    else -> birthday.rem(cookies.size)
  }
  return cookies[index]
}
