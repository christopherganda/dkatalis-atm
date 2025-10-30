package atm;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

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

  @Test
  void testDepositValid() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
  }

  @Test
  void testDepositErrorNotLoggedIn() {
    Atm atm = new Atm();
    assertThrows(IllegalStateException.class, () -> atm.deposit(BigDecimal.valueOf(100)));
  }

  @Test
  void testDepositErrorNegativeValue() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertThrows(IllegalArgumentException.class, () -> atm.deposit(BigDecimal.valueOf(-100)));
  }
}
