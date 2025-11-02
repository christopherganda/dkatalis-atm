package atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import atm.model.User;

public class DebtServiceTest {
  private Map<String, User> users;
  private DebtService debtService;

  @BeforeEach
  void setUp() {
    users = new HashMap<>();
    debtService = new DebtService(users);
  }

  @Test
  void testSettleDebtsNoDebts() {
    User debtor = new User("alice");
    users.put("alice", debtor);
    BigDecimal availableAmount = BigDecimal.valueOf(100);

    BigDecimal remaining = debtService.settleDebts(debtor, availableAmount);
    assertEquals(BigDecimal.valueOf(100), remaining);
    assertTrue(debtor.getOwesTo().isEmpty());
  }

  @Test
  void testSettleDebtsPartialSettlement() {
    User debtor = new User("alice");
    User creditor = new User("bob");
    users.put("alice", debtor);
    users.put("bob", creditor);

    debtor.addOwesTo("bob", BigDecimal.valueOf(100));
    creditor.addOwedBy("alice", BigDecimal.valueOf(100));

    BigDecimal availableAmount = BigDecimal.valueOf(70);
    BigDecimal remaining = debtService.settleDebts(debtor, availableAmount);

    assertEquals(BigDecimal.ZERO, remaining);
    assertEquals(BigDecimal.valueOf(30), debtor.getOwesTo().peek().getAmount());
    assertEquals(BigDecimal.valueOf(70), creditor.getBalance());
    assertEquals(BigDecimal.valueOf(30), creditor.getOwedBy().get("alice"));
  }

  @Test
  void testSettleDebtsFullSettlement() {
    User debtor = new User("alice");
    User creditor = new User("bob");
    users.put("alice", debtor);
    users.put("bob", creditor);

    debtor.addOwesTo("bob", BigDecimal.valueOf(100));
    creditor.addOwedBy("alice", BigDecimal.valueOf(100));

    BigDecimal availableAmount = BigDecimal.valueOf(100);
    BigDecimal remaining = debtService.settleDebts(debtor, availableAmount);

    assertEquals(BigDecimal.ZERO, remaining);
    assertTrue(debtor.getOwesTo().isEmpty());
    assertEquals(BigDecimal.valueOf(100), creditor.getBalance());
    assertTrue(creditor.getOwedBy().isEmpty());
  }

  @Test
  void testSettleOwedAmountPartial() {
    User sender = new User("alice");
    User receiver = new User("bob");
    users.put("alice", sender);
    users.put("bob", receiver);

    sender.addOwedBy("bob", BigDecimal.valueOf(100));
    receiver.addOwesTo("alice", BigDecimal.valueOf(100));
    BigDecimal amount = BigDecimal.valueOf(70);

    BigDecimal remaining = debtService.settleOwedAmount(sender, receiver, amount);
    assertEquals(BigDecimal.ZERO, remaining);
    assertEquals(BigDecimal.valueOf(30), sender.getOwedBy().get("bob"));
    assertEquals(BigDecimal.valueOf(30), receiver.getOwesTo().peek().getAmount());
  }

  @Test
  void testSettleOwedAmountFull() {
    User sender = new User("alice");
    User receiver = new User("bob");
    users.put("alice", sender);
    users.put("bob", receiver);

    sender.addOwedBy("bob", BigDecimal.valueOf(100));
    receiver.addOwesTo("alice", BigDecimal.valueOf(100));
    BigDecimal amount = BigDecimal.valueOf(130);
    BigDecimal remaining = debtService.settleOwedAmount(sender, receiver, amount);

    assertEquals(BigDecimal.valueOf(30), remaining);
    assertTrue(sender.getOwedBy().isEmpty());
    assertTrue(receiver.getOwesTo().isEmpty());
  }

  @Test
  void testSettleOwedAmountNoOwedBy() {
    User sender = new User("alice");
    User receiver = new User("bob");
    users.put("alice", sender);
    users.put("bob", receiver);

    BigDecimal amount = BigDecimal.valueOf(100);
    BigDecimal remaining = debtService.settleOwedAmount(sender, receiver, amount);

    assertEquals(BigDecimal.valueOf(100), remaining);
  }
}
