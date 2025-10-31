package atm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class AppTest {
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
    assertTrue(output.contains("Successfully logged in as: chris"));
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
    assertTrue(output.contains("Welcome to the ATM system"));
    assertTrue(output.contains("Successfully logged in as: chris"));
    assertTrue(output.contains("Current balance after deposit: 100"));
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
    assertTrue(output.contains("Error: Amount should not be lower than 0"));
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
    assertTrue(output.contains("Error: You must logged in before doing deposit"));
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
    assertTrue(output.contains("Welcome to the ATM system"));
    assertTrue(output.contains("Successfully logged in as: chris"));
    assertTrue(output.contains("Logout successfully. See you!"));
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
    assertTrue(output.contains("System is not logged in to any user"));
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
    assertTrue(output.contains("Transfered $100 to user2"));
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
    assertTrue(output.contains("Error: You must logged in before doing transfer"));
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
    assertTrue(output.contains("Error: Amount should not be lower than 0"));
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
    assertTrue(output.contains("Error: Cannot transfer to self"));
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
    assertTrue(output.contains("Error: Target user does not exist."));
  }
}