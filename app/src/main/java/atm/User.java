package atm;

import java.math.BigDecimal;

public class User {
  private String username;
  private BigDecimal balance;

  public User(String username) {
    this.username = username;
    this.balance = BigDecimal.ZERO;
  }

  public String getUsername() {
    return username;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void addBalance(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void subtractBalance(BigDecimal amount) {
    balance = balance.subtract(amount);
  }
}