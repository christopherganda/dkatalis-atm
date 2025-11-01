package atm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class AppTest {
  private static final String ERR_NO_USER_LOGGED_IN = "You must be logged in to perform this action.";
  private static final String ERR_INVALID_AMOUNT = "Amount should be greater than or equal to 0.";
  private static final String ERR_SELF_TRANSFER = "Cannot transfer to self.";
  private static final String ERR_USER_NOT_FOUND = "Target user does not exist.";
  @Test
  void testLoginValid() {
    String input = "login chris\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Welcome to the ATM system"));
    assertTrue(output.contains("Hello, chris!"));
    assertTrue(output.contains("Your balance is $0"));
  }

  @Test
  void testLoginEmptyUsername() {
    String input = "login\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Error: Index 1 out of bounds for length 1"));
  }

  @Test
  void testDepositValid() {
    String input = "login chris\ndeposit 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Your balance is $100"));
  }

  @Test
  void testDepositNegativeValueError() {
    String input = "login chris\ndeposit -100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_INVALID_AMOUNT));
  }

  @Test
  void testDepositEmptyDepositValueError() {
    String input = "login chris\ndeposit\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Error: Index 1 out of bounds for length 1"));
  }

  @Test
  void testDepositWithoutLoginError() {
    String input = "deposit 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_NO_USER_LOGGED_IN));
  }

  @Test
  void testLogoutValid() {
    String input = "login chris\nlogout\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Goodbye, chris!"));
  }

  @Test
  void testLogoutWithoutLogin() {
    String input = "logout\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_NO_USER_LOGGED_IN));
  }

  @Test
  void testTransferValid() {
    String input = "login chris\ndeposit 100\nlogout\nlogin user2\nlogout\nlogin chris\ntransfer user2 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains("Transferred $100 to user2"));
    assertTrue(output.contains("Your balance is $0"));
  }

  @Test
  void testTransferNotLoggedInError() {
    String input = "transfer user2 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_NO_USER_LOGGED_IN));
  }

  @Test
  void testTransferNegativeValueError() {
    String input = "login chris\ndeposit 100\nlogout\nlogin user2\nlogout\nlogin chris\ntransfer user2 -100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_INVALID_AMOUNT));
  }

  @Test
  void testTransferToSelfError() {
    String input = "login chris\ndeposit 100\ntransfer chris 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_SELF_TRANSFER));
  }

  @Test
  void testTransferTargetNotExistError() {
    String input = "login chris\ndeposit 100\ntransfer user2 100\nexit\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setIn(inputStream);
    System.setOut(new PrintStream(outputStream));

    App.main(new String[]{});
    String output = outputStream.toString();
    assertTrue(output.contains(ERR_USER_NOT_FOUND));
  }
}