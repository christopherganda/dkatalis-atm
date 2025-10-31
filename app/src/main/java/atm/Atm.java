package atm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Atm {
  private Map<String, User> users = new HashMap<String, User>();
  private User loggedInUser = null;

  // improvement: how to handle concurrent requests
  public void login(String username) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }

    if (loggedInUser != null) {
      throw new IllegalStateException("Currently logged in as: " + loggedInUser);
    }

    users.putIfAbsent(username, new User(username));
    loggedInUser = users.get(username);
    System.out.println("Successfully logged in as: " + loggedInUser.getUsername());
  }

  public void deposit(BigDecimal amount) {
    if (loggedInUser == null) {
      throw new IllegalStateException("You must logged in before doing deposit");
    }

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount should not be lower than 0");
    }

    loggedInUser.deposit(amount);
    System.out.println("Current balance after deposit: " + loggedInUser.getBalance());
  }
}