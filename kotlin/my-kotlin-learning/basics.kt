// Beginning of individual Kotlin lessons.

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// ?: symbol
fun formatName(name: String?) = name ?: "Fellow Human"

// string interpolation
fun greetReader(greeting: String = "Hey", name: String?) =
  println("$greeting ${formatName(name)}")

// data class
data class Language(val name: String)
// used by Java Logger.java
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

fun String.isKotlin() = this == "Kotlin"

fun demoExtensionFunctions() {
  println("IsKotlin? " + "Kotlin".isKotlin())
}

open class Person(val firstName: String, val lastName: String) {
  fun getFormattedName() = "$lastName, $firstName"
}

class Programmer(
  firstName: String, lastName: String,
  val favoriteLanguage: String) : Person(firstName, lastName)

class Course(courseTitle: String) {
  // properties are public by default
  // kotlin generates getting/setter for var and getting for val
  val title = courseTitle  // usually the constructor "val title: String" should take care of this
  var description = ""
    set(value) {
      field = value + " (modified)"
    }
    get() {
      return field + " (returned)"
    }
}

// Same as class School public constructor(var teacher: String) {
class School(var teacher: String) {
  val numOfStudents: Int

  init {
    numOfStudents = 100
  }

  fun praiseTeacher(praise: String) {
    println("$praise, $teacher")
  }

  // secondary constructor needs to delegate to the primary constructor
  constructor(realTeacher: String, enabled: Boolean) : this(realTeacher + " (real) " + enabled) // { ...}
}

fun useClass() {
  val programmer = Programmer("Peter", "Pan", "python")
  println("Programmer: ${programmer.getFormattedName()}")

  val course = Course("courseTitle1")
  course.description = "desc1"
  println("course title: ${course.title}, desc: ${course.description}")

  val school = School("teacher1")
  school.teacher = "teacher2"
  println(school.teacher)
  school.praiseTeacher("Thanks!")
  println("${school.numOfStudents} students in school")

  val school2 = School("Professor", true)
  println(school2.teacher)
}

interface GameObject {
  val id: String
  val id2: String
    get() = "defaultId2"
  // not allowed: val id3 = "id3default"
  val id4: String

  fun update(currentTime: Long) {
    println("id: $id GameObject update")
  }
}

// interface inheritance
interface Movable : GameObject {
  fun move(currentTime: Long)
}

interface Drawable {
  fun draw()
  fun update(currentTime: Long) {
    println("Drawable update")
  }
}

class Player : Movable, Drawable {
  override val id = "playerId"
  override val id4
    get() = "id4FromPlayer"

  override fun update(currentTime: Long) {
    println("Update time: " + currentTime + " for " + id)
    println("id2 " + id2)
    println("id4 " + id4)
    // Cannot use super because two implemented interfaces have default
    // implementations.
    //super.update(currentTime)
    super<Movable>.update(currentTime)
    super<Drawable>.update(currentTime)
  }

  override fun move(currentTime: Long) {
    println("move time: ${currentTime} for $id")
  }

  override fun draw() {
    println("draw itself, $id")
  }
}

fun demoInterfaces() {
  val player = Player()
  player.update(1000)
  player.move(2000)
  player.draw()
}

interface Shape {
  fun getArea() : Double
}
// open not needed
abstract class Rectangle2 : Shape
open class Rectangle(val width: Double, val height: Double) : Shape {
  // prevent subclass from overriding this
  final override fun getArea() = width * height

  open fun printArea() = println("Area is ${getArea()}")
}
class Square(val edgeLength : Double) : Rectangle(edgeLength, edgeLength) {
  override fun printArea() = println("Square area is ${getArea()}")
}
fun demoAbstractClasses() {
 val rect = Rectangle(10.0, 20.0)
 println("rect area: ${rect.getArea()}")
 Square(12.0).printArea()
}

interface SettingsProvider {
  fun getSetting(key: String) : String
  fun putSetting(key: String, value: String)
}

object SettingsManager : SettingsProvider {
  private val map = HashMap<String, String>()

  override fun getSetting(key : String) = map.getOrDefault(key, "")
  override fun putSetting(key : String, value : String) {
    map.put(key, value)
  }

  fun printSettings() = map.forEach{ println(it) }
}

class UserManager(settingsProvider : SettingsProvider) : SettingsProvider by
settingsProvider

fun demoDelegate() {
  val userManager = UserManager(SettingsManager)
  userManager.putSetting("a", "apple")
  println("usermanager: " + userManager.getSetting("a"))

  val settingsProvider : SettingsProvider = SettingsManager
  (settingsProvider as SettingsManager).putSetting("b", "boy")
  // Once it's casted once, don't have to be casted subsequently.
  settingsProvider.putSetting("c", "cat")
  println("settingsProvider casted to SettingsManager")
  settingsProvider.printSettings()

  val settingsProvider2 : SettingsProvider = SettingsManager
  if (settingsProvider2 is SettingsManager) {
    println("settingsProvider2 uses smart casting")
    settingsProvider.printSettings()
  }

}

data class Article(val title: String, val author: String) {
  // Not in the primary constructor, so won't be used in equals, hashcode,
  // toString.
  var snippet: String = ""
}
fun demoDataClass() {
  val article1 = Article("title1", "author1")
  article1.snippet = "article1 has this snippet"
  val article2 = Article("title1", "author1")
  article2.snippet = "article2 has this snippet"
  println("${article1} == ${article2}? " + (article1 == article2))
  val article3 = article1.copy("title3")
  println("${article1} == ${article3}? " + (article1 == article3))

  // title = article1.getComponent1()
  // author = article1.getComponent2()
  val (title, author) = article1
  println("deconstructed " + title + ", " + author)
}

class FooWithObject {
  companion object {
    const val ID_FROM_OBJECT = "123"
    @JvmStatic
    val ID_FROM_OBJECT_WITH_JVM_STATIC = "123456"
  }
}
fun demoUseStaticFromJava() {
  Logger.callKotlinStaticVariable()
}

fun demoUsingCoroutines() {
  runBlocking {
    launch {
      delay(2000L)
      println("Done using coroutines")
    }
    println("printed from a coroutine")
    useCoroutineScope()
  }
}

suspend fun useCoroutineScope() = coroutineScope {
  launch {
    delay(1000L)
    println("end of useCoroutineScope")
  }
  println("printed inside useCoroutineScope")
}

/**
 * From https://kotlinlang.org/docs/flow.html#flows
 */
fun demoUsingFlows() {
  runBlocking {
    runBlocking {
      launch {
          for (k in 1..3) {
              println("I'm not blocked $k")
              delay(100)
          }
      }
      // Collect the flow
      simpleFlow().collect { value -> println("Got ${value} from flow") }
    }

    runBlocking {
      var flowWithEmitEcho = simpleFlowWithEmitEcho(100)
      launch {
        // The print out proves that flowWithEmitEcho is not run until
        // the delay is done, thus we need "collect" to start
        // the flow.
        delay(1000)
        flowWithEmitEcho.collect { value ->
          println("Got ${value} from flow with echo")
        }
      }
    }

    runBlocking {
      // The print out proves that once a flow is started by collect,
      // it can keep emitting values without waiting for the
      // receiver to handle the values.
      var flowWithEmitEcho2 = simpleFlowWithEmitEcho2(100)
      flowWithEmitEcho2.collect { value ->
        launch {
          delay(1000)
          println("Got ${value} from flow with echo 2")
        }
      }
    }
  }
}

fun simpleFlow(): Flow<Int> = flow {
  for (i in 1..3) {
    delay(100)
    emit(i)
  }
}

fun simpleFlowWithEmitEcho(delayInMs: Long): Flow<Int> = flow {
  printWithTimeStamp("simpleFlowWithEmitEcho started")
  for (i in 1..3) {
    delay(delayInMs)
    printWithTimeStamp("simpleFlowWithEmitEcho emits $i")
    emit(i)
  }
}

fun simpleFlowWithEmitEcho2(delayInMs: Long): Flow<Int> = flow {
  printWithTimeStamp("simpleFlowWithEmitEcho2 started")
  for (i in 1..15) {
    delay(delayInMs)
    printWithTimeStamp("simpleFlowWithEmitEcho2 emits $i")
    emit(i)
  }
}
/*
fun main() = runBlocking<Unit> {
    // Launch a concurrent coroutine to check if the main thread is blocked
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    // Collect the flow
    simple().collect { value -> println(value) }
}
*/

// end of lessons

fun main(args: Array<String>) {
  var argCount = args.size
  println("main argCount $argCount")

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
  
  printHeader("Functions")
  functionAsFirstClass()

  printHeader("Function parameters")
  functionParameters()

  printHeader("Infix function")
  demoInfixFunction()

  printHeader("Extension functions")
  demoExtensionFunctions()

  printHeader("Classes")
  useClass()

  printHeader("Interfaces")
  demoInterfaces()

  printHeader("abstract classes")
  demoAbstractClasses()

  printHeader("delegate")
  demoDelegate()

  printHeader("data class")
  demoDataClass()

  printHeader("use static from java")
  demoUseStaticFromJava()

  printHeader("use coroutines")
  demoUsingCoroutines()

  printHeader("use flows")
  demoUsingFlows()
}

fun printHeader(header: String?) {
  if (header == null) {
    println("\n=====\n")
  } else {
    println("\n=== $header ====\n")
  }
}

fun getCurrentDateTime(): String {
  val format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  val locale = Locale.getDefault()
  val date = Calendar.getInstance().time
  val formatter = SimpleDateFormat(format, locale)
  return formatter.format(date)
}

fun printWithTimeStamp(s: Any) {
  print(getCurrentDateTime() + ": ")
  println(s)
}
