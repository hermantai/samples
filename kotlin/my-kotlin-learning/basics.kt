// Beginning of individual Kotlin lessons.

// ?: symbol
fun formatName(name: String?) = name ?: "Fellow Human"

// string interpolation
fun greetReader(greeting: String = "Hey", name: String?) =
  println("$greeting ${formatName(name)}")

// data class
data class Language(val name: String)
data class KotlinDeveloper(val name: String)

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
  Logger.logMessage("Logger.logMessage called from Kotlin")
  Logger.logDeveloper()
  Logger.callKotlinFuncFromJava()
}

fun loopAndRange(n: Int) : Int {
  var sum = 0
  for (i in 1..n) {
    sum += i
  }

  return sum
}

open class Person(val firstName: String, val lastName: String) {
  fun getFormattedName() = "$lastName, $firstName"
}

class Programmer(firstName: String, lastName: String, val favoriteLanguage: String) : Person(firstName, lastName)

fun useClass() {
  val programmer = Programmer("Peter", "Pan", "python")
  println("Programmer: ${programmer.getFormattedName()}")
}

fun functionAsFirstClass() {
  // store function in a variable
  val stringFilter: (String) -> Boolean = {
    string -> string.length > 3
  }

  val languages = listOf("c++", "php", "java", "kotlin")
  println("Filterd languages: " + languages.filter(stringFilter))

  // use function literal directly
  println("Filtered languages too: " + listOf("c++", "php", "java", "kotlin")
    .filter({string -> string.length > 3}))

  // mutable function variables
  var greetingProvider: () -> String = { "mutable function vars" }
  println(greetingProvider())
  greetingProvider = { "it's mutated" }
  println(greetingProvider())

  // nullable function variables have to be invoked using invoke
  var nullableGreetingProvider: (() -> String)? = null
  nullableGreetingProvider?.invoke()
  // Just to avoid a warning that ?. is unnecessary due to the compiler knows
  // I just assigned a value to the variable.
  if (true) {
    nullableGreetingProvider = { "nullable greeting provider" }
  }
  nullableGreetingProvider?.let { it() }

  var anotherNullableGreetingProvider : GreetingProvider? = null
  anotherNullableGreetingProvider?.invoke()


  println("typealiased function")
  var greetingProvider2: GreetingProvider2 = getGreetingProvider(true)
  println(greetingProvider2())
}

fun getGreetingProvider(isFriendly : Boolean) : () -> List<String> {
 return if (isFriendly) {
   { listOf("Hey", "Hi", "hello") }
 } else {
   { listOf("Go away", "Leave me alone") }
 }
}

typealias GreetingProvider = () -> String
typealias GreetingProvider2 = () -> List<String>

fun functionParameters() {
  fun funWithVararg(greeting: String, vararg names : String) {
    names.forEach {
      name -> println("$greeting $name")
    }
  }

  funWithVararg("hi", "peter", "tom")

  fun funWithDefaultParams(greeting : String, name : String = "Kotlin") {
    println("$greeting $name")
  }
  
  funWithDefaultParams("Hey")
  funWithDefaultParams(greeting = "Yo", name = "funWithDefaultParams")
}

fun demoInfixFunction() {
  val needle = Needle("apple")
  println("needle in haystack? " + (needle inside listOf("pen", "apple", "orange")))

  println("bc" isEqualTo "def")
}

infix fun <T> T.isEqualTo(other: T): Boolean = this == other

class Needle<T>(val value: T) {
  public infix fun inside(hayStack: List<T>): Boolean = hayStack.contains(value)
}
// end of lessons

fun main(args: Array<String>) {
  greetReader("Hello!", "Reader")

  // data class to demonstrate object-oriented programming
  val language = Language("Kotlin")
  printHeader("Data class")
  println(language.name)

  
  printHeader("Working with nulls")
  workingWithNulls()

  printHeader("Use java from kotlin")
  useJavaClass()
  
  printHeader("Loop and range")
  println(loopAndRange(10))
  
  printHeader("Classes")
  useClass()

  printHeader("Functions")
  functionAsFirstClass()

  printHeader("Function parameters")
  functionParameters()

  printHeader("Infix function")
  demoInfixFunction()
}

fun printHeader(header: String?) {
  if (header == null) {
    println("\n=====\n")
  } else {
    println("\n=== $header ====\n")
  }
}
