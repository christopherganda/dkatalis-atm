package atm.app;

import java.math.BigDecimal;
import java.util.Scanner;

import atm.model.Atm;
import atm.util.OutputPresenter;

public class App {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Atm atm = new Atm();
    OutputPresenter.printWelcome();
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
            atm.withdraw(new BigDecimal(params[1]));
            break;
          case "transfer":
            atm.transfer(params[1], new BigDecimal(params[2]));
            break;
          case "logout":
            atm.logout();
            break;
          case "exit":
            scanner.close();
            return;
          default:
            OutputPresenter.printInvalidCommand();
            break;
        }
      } catch (Exception e) {
        OutputPresenter.printError(e.getMessage());
      }
    }
  }
}