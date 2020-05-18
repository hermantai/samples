public class Logger {
  public static void logMessage(String message) {
    System.out.println(message);
  }

  public static void logDeveloper() {
    KotlinDeveloper kotlineDev = new KotlinDeveloper("peter");
    System.out.println("use kotline class from java: " + kotlineDev.getName());
  }

  public static void callKotlinFuncFromJava() {
    BasicsKt.greetReader("Hi", "kotlin dev");
  }
}
