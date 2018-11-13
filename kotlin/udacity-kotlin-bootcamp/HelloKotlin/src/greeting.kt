fun main(args: Array<String>) {
  val hours = args[0].toInt()

  if (hours < 12) {
    println("Good morning, Kotlin")
  } else {
    println("Good night, Kotlin")
  }
}