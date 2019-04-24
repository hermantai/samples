package playdagger;

public class PlayDagger {
  public static void main(String args[]) {
    System.out.println("hello world");

    CoffeeShop coffeeShop = DaggerCoffeeShop.builder().userName("herman").build();
    coffeeShop.maker().brew();
  }
}
