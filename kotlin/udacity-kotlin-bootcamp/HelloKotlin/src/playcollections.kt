fun main(args: Array<String>) {
  val allBooks = setOf("Macbeth", "Romeo and Juliet", "Hamlet", "A Midsummer Night's Dream")
  val library = mapOf("Shakespeare" to allBooks)
  println(library.any { it.value.contains("Hamlet") })

  val moreBooks = mutableMapOf<String, String>("Wilhelm Tell" to "Schiller")
  moreBooks.getOrPut("Jungle Book") { "Kipling" }
  moreBooks.getOrPut("Hamlet") { "Shakespeare" }
  println(moreBooks)
}