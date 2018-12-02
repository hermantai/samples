package Aquarium

fun main (args: Array<String>) {
  buildAquarium()
}

fun buildAquarium() {

  val myAquarium = Aquarium()
  println("Length: ${myAquarium.length}")

  myAquarium.length = 20
  println("Length: ${myAquarium.length}")

  println("Length: ${myAquarium.volume}")

  val smallAquarium = Aquarium(length = 20, width = 15, height = 30)
  print("Volume: ${smallAquarium.volume} litres")

  val myAquarium2 = Aquarium(numberOfFish = 30)
}