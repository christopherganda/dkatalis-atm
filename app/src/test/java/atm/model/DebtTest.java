package atm.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class DebtTest {
  @Test
  void testDebt() {
    Debt debt = new Debt("alice", BigDecimal.valueOf(100));
    assertNotNull(debt);
    assertEquals("alice", debt.getUsername());
    assertEquals(BigDecimal.valueOf(100), debt.getAmount());
  }

  @Test
  void testReduceAmount() {
    Debt debt = new Debt("alice", BigDecimal.valueOf(100));
    debt.reduceAmount(BigDecimal.valueOf(30));
    assertEquals(BigDecimal.valueOf(70), debt.getAmount());
  }

  @Test
  void testReduceAmountToZero() {
    Debt debt = new Debt("bob", BigDecimal.valueOf(50));
    debt.reduceAmount(BigDecimal.valueOf(50));
    assertEquals(BigDecimal.ZERO, debt.getAmount());
  }
}
