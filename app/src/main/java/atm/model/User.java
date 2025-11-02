package atm.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import atm.util.Formatter;

public class User {
  private String username;
  private BigDecimal balance;
  private Queue<Debt> owesTo;
  private Map<String, BigDecimal> owedBy;

  public User(String username) {
    this.username = username;
    this.balance = BigDecimal.ZERO;
    this.owesTo = new LinkedList<>();
    this.owedBy = new HashMap<>();
  }

  public String getUsername() {
    return username;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public String getBalanceString() {
    return Formatter.formatCurrencyWithoutComma(balance);
  }

  public Queue<Debt> getOwesTo() {
    return owesTo;
  }

  public Map<String, BigDecimal> getOwesToMap() {
    Map<String, BigDecimal> results = new LinkedHashMap<>();
    for(Debt debt : owesTo) {
      results.merge(debt.getUsername(), debt.getAmount(), BigDecimal::add);
    }
    return results;
  }

  public Map<String, BigDecimal> getOwedBy() {
    return owedBy;
  }

  public void addBalance(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void subtractBalance(BigDecimal amount) {
    balance = balance.subtract(amount);
  }

  // Meaning: This user owes to {username}
  public void addOwesTo(String username, BigDecimal amount) {
    owesTo.add(new Debt(username, amount));
  }

  // Meaning: This user is owed by {username}
  // if amount is <= 0, remove from owed by
  public void deductOrRemoveOwedBy(String username, BigDecimal amount) {
    if (!owedBy.containsKey(username)) {
      return;
    }

    owedBy.put(username, owedBy.get(username).subtract(amount));
    if (owedBy.get(username).compareTo(BigDecimal.ZERO) <= 0) {
      owedBy.remove(username);
    }
  }

  public void addOwedBy(String username, BigDecimal amount) {
    BigDecimal oldAmount = BigDecimal.ZERO;
    if(owedBy.containsKey(username)) {
      oldAmount = owedBy.get(username);
    }
    owedBy.put(username, amount.add(oldAmount));
  }
}