package atm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AtmTest {
  @Test
  void testLoginValidUser() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
  }

  @Test
  void testLoginEmptyUsername() {
    Atm atm = new Atm();
    assertThrows(IllegalArgumentException.class, () -> atm.login(""));
  }

  @Test
  void testLoginMultipleLogin() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertThrows(IllegalStateException.class, () -> atm.login("chris2"));
  }
}
