package Aquarium

class Aquarium (var length: Int = 100, var width: Int = 20, var height: Int = 40){

  var volume: Int
    get() = width * height * length / 1000
    set(value) {
      height = (value * 1000) / (width * length)
    }

  var water = volume * 0.9

  constructor(numberOfFish: Int): this() {
    val water = numberOfFish * 2000 // cm3
    val tank = water + water * 0.1
    height = (tank / (length * width)).toInt()
  }

  fun volume2() = width * height * length / 1000
}