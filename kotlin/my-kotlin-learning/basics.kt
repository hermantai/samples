// ?: symbol
fun formatName(name: String?) = name ?: "Fellow Human"

// string interpolation
fun greetReader(greeting: String = "Hey", name: String?) =
  println("$greeting ${formatName(name)}")

fun main(args: Array<String>) {
  greetReader("Hello!", "Reader")
}
