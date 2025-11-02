package atm.service;

import java.math.BigDecimal;

import atm.model.User;

public final class ValidationService {
  private static final String ERR_NO_USER_LOGGED_IN = "You must be logged in to perform this action.";
  private static final String ERR_INVALID_AMOUNT = "Amount should be greater than or equal to 0.";
  private static final String ERR_INVALID_USERNAME = "Username cannot be empty";
  private static final String ERR_SELF_TRANSFER = "Cannot transfer to self.";

  private ValidationService() {}

  public static void validateUsername(String username) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException(ERR_INVALID_USERNAME);
    }
  }

  public static void validateMultipleLogin(User currentLoggedInUser) {
    if (currentLoggedInUser != null) {
      throw new IllegalStateException("Currently logged in as: " + currentLoggedInUser);
    }
  }

  public static void validateLoggedIn(User loggedInUser) {
    if (loggedInUser == null) {
      throw new IllegalStateException(ERR_NO_USER_LOGGED_IN);
    }
  }

  public static void validateAmount(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(ERR_INVALID_AMOUNT);
    }
  }
  
  public static void validateSelfTransfer(User loggedInUser, String receiverUsername) {
    if (loggedInUser.getUsername().equals(receiverUsername)) {
      throw new IllegalStateException(ERR_SELF_TRANSFER);
    }
  }
}
