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

  @Test
  void testLogoutValid() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertDoesNotThrow(() -> atm.logout());
  }

  @Test
  void testLogoutWithoutLogin() {
    Atm atm = new Atm();
    assertThrows(IllegalStateException.class, () -> atm.logout());
  }

  @Test
  void testWithdrawValid() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertDoesNotThrow(() -> atm.withdraw(BigDecimal.valueOf(100)));
  }

  @Test
  void testWithdrawGreaterThanBalanceError() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertThrows(IllegalStateException.class, () -> atm.withdraw(BigDecimal.valueOf(100)));
  }

  @Test
  void testWithdrawErrorNegativeValue() {
    Atm atm = new Atm();
    assertDoesNotThrow(() -> atm.login("chris"));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertThrows(IllegalArgumentException.class, () -> atm.withdraw(BigDecimal.valueOf(-100)));
  }

  @Test
  void testWithdrawErrorNotLoggedIn() {
    Atm atm = new Atm();
    assertThrows(IllegalStateException.class, () -> atm.withdraw(BigDecimal.valueOf(100)));
  }

  @Test
  void testTransferValid() {
    Atm atm = new Atm();
    String user1 = "user1";
    String user2 = "user2";

    assertDoesNotThrow(() -> atm.login(user1));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user2));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user1));
    assertDoesNotThrow(() -> atm.transfer(user2, BigDecimal.valueOf(100)));
  }

  @Test
  void testTransferOutOfBalanceError() {
    Atm atm = new Atm();
    String user1 = "user1";
    String user2 = "user2";

    assertDoesNotThrow(() -> atm.login(user1));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user2));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user1));
    assertThrows(IllegalStateException.class, () -> atm.transfer(user2, BigDecimal.valueOf(105)));
  }

  @Test
  void testTransferNegativeValueError() {
    Atm atm = new Atm();
    String user1 = "user1";
    String user2 = "user2";

    assertDoesNotThrow(() -> atm.login(user1));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user2));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user1));
    assertThrows(IllegalArgumentException.class, () -> atm.transfer(user2, BigDecimal.valueOf(-100)));
  }

  @Test
  void testTargetedUserNotExistError() {
    Atm atm = new Atm();
    String user1 = "user1";
    String user2 = "user2";

    assertDoesNotThrow(() -> atm.login(user1));
    assertDoesNotThrow(() -> atm.deposit(BigDecimal.valueOf(100)));
    assertDoesNotThrow(() -> atm.logout());
    assertDoesNotThrow(() -> atm.login(user1));
    assertThrows(IllegalArgumentException.class, () -> atm.transfer(user2, BigDecimal.valueOf(100)));
  }
}
