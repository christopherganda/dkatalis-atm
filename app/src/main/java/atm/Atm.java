
package atm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Atm {
  private Map<String, BigDecimal> users = new HashMap<String, BigDecimal>();
  private String loggedInUser = null;

  // improvement: how to handle concurrent requests
  public void login(String username) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }

    if (loggedInUser != null) {
      throw new IllegalStateException("Currently logged in as: " + loggedInUser);
    }

    users.putIfAbsent(username, BigDecimal.valueOf(0));
    loggedInUser = username;
    System.out.println("Successfully logged in as: " + loggedInUser);
  }
}