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
}