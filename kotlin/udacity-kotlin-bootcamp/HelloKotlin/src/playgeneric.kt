open class BaseBuildingMaterial() {
  open val numberNeeded = 1
}

class Wood : BaseBuildingMaterial() {
  override val numberNeeded = 4
}

class Brick : BaseBuildingMaterial() {
  override val numberNeeded = 8
}

class Building<T: BaseBuildingMaterial>(val buildingMaterial: T) {

  val baseMaterialsNeeded = 100
  val actualMaterialsNeeded = buildingMaterial.numberNeeded * baseMaterialsNeeded

  fun build() {
    println(" $actualMaterialsNeeded ${buildingMaterial::class.simpleName} required")
  }
}

open class Stuff {
  open val value = 1
}

class Pencil : Stuff() {
  override val value = 2
}

class DoStuff(val stuff : Stuff) {
  fun doIt() {
    println(stuff.value)
  }
}

fun main(args: Array<String>) {
  Building(Wood()).build()

  DoStuff(Pencil()).doIt()
}
