data class Fish (var name: String)

fun main(args: Array<String>) {
  fishExamples()
}

fun fishExamples() {
  val fish = Fish("splashy")

  myWith(fish.name) {
    println(capitalize())
  }

  with(fish.name) {
    println(capitalize())
  }
}

fun myWith(name: String, block: String.() -> Unit) {
  name.block()
}