package atm;

import java.math.BigDecimal;

public class Debt {
  private String username;
  private BigDecimal amount;

  public Debt(String username, BigDecimal amount) {
    this.username = username;
    this.amount = amount;
  }

  public String getUsername() {
    return username;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void reduceAmount(BigDecimal value) {
    amount = amount.subtract(value);
  }
}