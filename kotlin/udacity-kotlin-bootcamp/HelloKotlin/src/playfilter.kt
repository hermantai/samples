fun main(args : Array<String>) {
  val spices = listOf("curry", "pepper", "cayenne", "ginger", "red curry", "green curry", "red pepper" )

  val filtered = spices.filter{s -> s.contains("curry")}.sortedBy { it.length }

  println(filtered)

  println(spices.take(3).filter{it.startsWith('c')})
}