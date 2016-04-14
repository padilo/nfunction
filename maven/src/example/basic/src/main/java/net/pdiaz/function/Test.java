package net.pdiaz.function;

public class Test {
  public static void main(String[] args) {
    Function2<Integer, Double, String> f = new Function2<Integer, Double, String>() {
      public String apply(Integer param1, Double param2) {
        return "my string";
      }
    };
  }
}