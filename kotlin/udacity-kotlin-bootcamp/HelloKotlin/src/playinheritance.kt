open class Book(var title : String, var author : String) {
  private var currentPage: Int = 0

  open fun readPage() {
    currentPage++
  }
}

class EBook(title : String, author : String, var format: String = "text") : Book(title, author) {

  private var wordsRead: Int = 0
  override fun readPage() {
    wordsRead += 250
  }
}