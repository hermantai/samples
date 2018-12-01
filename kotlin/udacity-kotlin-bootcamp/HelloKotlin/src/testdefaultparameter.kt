import java.util.*

fun main(args: Array<String>) {
  println(getInt())
}

// Notice that the default parameter is a function call,
// so it's value will be determined at runtime, which is a random number.
fun getInt(a: Int = randomInt()) : Int {
  return a
}

fun randomInt() : Int {
  return Random().nextInt(5)
}