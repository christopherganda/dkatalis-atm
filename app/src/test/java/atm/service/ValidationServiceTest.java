package atm.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import atm.model.User;

public class ValidationServiceTest {
  @Test
  void testValidateUsernameNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      ValidationService.validateUsername(null);
    });
  }

  @Test
  void testValidateUsernameEmpty() {
    assertThrows(IllegalArgumentException.class, () -> {
      ValidationService.validateUsername("");
    });
  }

  @Test
  void testValidateMultipleLogin() {
    User user = new User("chris");
    assertThrows(IllegalStateException.class, () -> {
      ValidationService.validateMultipleLogin(user);
    });
  }

  @Test
  void testValidateLoggedIn() {
    assertThrows(IllegalStateException.class, () -> {
      ValidationService.validateLoggedIn(null);
    });
  }

  @Test
  void testValidateAmountNegative() {
    assertThrows(IllegalArgumentException.class, () -> {
      ValidationService.validateAmount(java.math.BigDecimal.valueOf(-10));
    });
  }

  @Test
  void testValidateSelfTransfer() {
    User user = new User("chris");
    assertThrows(IllegalStateException.class, () -> {
      ValidationService.validateSelfTransfer(user, "chris");
    });
  }
}
