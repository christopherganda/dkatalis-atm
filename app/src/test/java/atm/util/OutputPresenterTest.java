package atm.util;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

public class OutputPresenterTest {

  @Test
  void testPrintWelcome() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printWelcome();
    String output = outputStream.toString();
    assert(output.contains("Welcome to the ATM system"));
  }

  @Test
  void testPrintLoginGreeting() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printLoginGreeting("chris");
    String output = outputStream.toString();
    assert(output.contains("Hello, chris!"));
  }

  @Test
  void testPrintBalance() {
    atm.model.User user = new atm.model.User("chris");
    user.addBalance(new java.math.BigDecimal("1500"));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printBalance(user);
    String output = outputStream.toString();
    assert(output.contains("Your balance is $1500"));
  }

  @Test
  void testPrintBalanceWithOwesAndOwedBy() {
    atm.model.User user = new atm.model.User("chris");
    user.addBalance(new java.math.BigDecimal("1500"));
    user.addOwesTo("alice", new java.math.BigDecimal("200"));
    user.addOwedBy("bob", new java.math.BigDecimal("300"));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printBalance(user);
    String output = outputStream.toString();
    assert(output.contains("Your balance is $1500"));
    assert(output.contains("Owed $200 to alice"));
    assert(output.contains("Owed $300 from bob"));
  }

  @Test
  void testPrintTransfer() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printTransfer("alice", new java.math.BigDecimal("250"));
    String output = outputStream.toString();
    assert(output.contains("Transferred $250 to alice"));
  }

  @Test
  void testPrintError() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printError("Sample error message");
    String output = outputStream.toString();
    assert(output.contains("Error: Sample error message"));
  }

  @Test
  void testPrintInvalidCommand() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new java.io.PrintStream(outputStream));

    OutputPresenter.printInvalidCommand();
    String output = outputStream.toString();
    assert(output.contains("Invalid command"));
  }
}
