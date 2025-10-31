package atm;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Atm atm = new Atm();
    System.out.println("Welcome to the ATM system");
    while (true) {
      String input = scanner.nextLine().trim();
      String[] params = input.split(" ");
      try {
        switch(params[0]) {
          case "login":
            atm.login(params[1]);
            break;
          case "deposit":
            atm.deposit(new BigDecimal(params[1]));
            break;
          case "withdraw":
            break;
          case "transfer":
            break;
          case "logout":
            atm.logout();
            break;
          case "exit":
            scanner.close();
            return;
          default:
            System.out.println("Invalid command");
            break;
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }
}