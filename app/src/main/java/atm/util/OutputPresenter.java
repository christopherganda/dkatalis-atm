package atm.util;

import java.math.BigDecimal;
import java.util.Map;

import atm.model.User;

public final class OutputPresenter {
  public static void printWelcome() {
    System.out.println("Welcome to the ATM system");
  }

  public static void printLoginGreeting(String username) {
    System.out.printf("Hello, %s!\n", username);
  }

  public static void printLogoutGreeting(String username) {
    System.out.printf("Goodbye, %s!\n", username);
  }

  public static void printBalance(User user) {
    System.out.printf("Your balance is %s\n", user.getBalanceString());
    
    for(Map.Entry<String, BigDecimal> entry : user.getOwesToMap().entrySet()) {
      System.out.printf("Owed %s to %s\n", 
        Formatter.formatCurrencyWithoutComma(entry.getValue()), entry.getKey());
    }

    for (Map.Entry<String, BigDecimal> entry : user.getOwedBy().entrySet()) {
      System.out.printf("Owed %s from %s\n", 
        Formatter.formatCurrencyWithoutComma(entry.getValue()), entry.getKey());
    }
  }

  public static void printTransfer(String username, BigDecimal amount) {
    System.out.printf("Transferred %s to %s\n", 
      Formatter.formatCurrencyWithoutComma(amount), username);
  }

  public static void printError(String message) {
    System.out.println("Error: " + message);
  }

  public static void printInvalidCommand() {
    System.out.println("Invalid command");
  }
}
