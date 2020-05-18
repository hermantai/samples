import com.gmail.htaihm.Developer;

// ?: symbol
fun formatName(name: String?) = name ?: "Fellow Human"

// string interpolation
fun greetReader(greeting: String = "Hey", name: String?) =
  println("$greeting ${formatName(name)}")

// data class
data class Language(val name: String)

// Working with nulls:
// safe calls, non-null assertion, conditionals, elvis operator, safe casts
fun workingWithNulls() {
  var languages = listOf("Kotlin", "Java")
  languages.isNotEmpty()

  // safe call
  var companies: List<String>? = listOf("apple", "google")
  companies?.isNotEmpty()
  companies?.get(0)?.toLowerCase()

  // non-null assertion
  companies!!.size
  // the compiler will treat companies as non-null type after this line

  // condition
  var args: Array<String>? = arrayOf("arg1", "arg2")
  var argCount = if(args != null) args.size else 0
  println("argCount $argCount")
  // ?: is elvis operator
  argCount = args?.size ?: 0
  println("argCount $argCount")
}

fun useJavaClass() {
  val developer = Developer("herman", "python")
  println("${developer.name} loves ${developer.getFavoriteLanguage()}")
}

fun main(args: Array<String>) {
  greetReader("Hello!", "Reader")

  // data class to demonstrate object-oriented programming
  val language = Language("Kotlin")
  printHeader("Data class")
  println(language.name)

  
  printHeader("Working with nulls")
  workingWithNulls()

  useJavaClass()
}

fun printHeader(header: String?) {
  if (header == null) {
    println("\n=====\n")
  } else {
    println("\n=== $header ====\n")
  }
}
