package atm.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
  private User user;

  @BeforeEach
  void setUp() {
    user = new User("chris");
  }

  @Test
  void testUser() {
    assertNotNull(user);
    assertEquals("chris", user.getUsername());
    assertEquals(BigDecimal.ZERO, user.getBalance());
    assertTrue(user.getOwesTo().isEmpty());
    assertTrue(user.getOwedBy().isEmpty());
  }

  @Test
  void testGetBalanceString() {
    assertEquals("$0", user.getBalanceString());
    user.addBalance(BigDecimal.valueOf(100));
    assertEquals("$100", user.getBalanceString());
  }

  @Test
  void testAddBalance() {
    user.addBalance(BigDecimal.valueOf(100));
    assertEquals(BigDecimal.valueOf(100), user.getBalance());
  }

  @Test
  void testSubtractBalance() {
    user.addBalance(BigDecimal.valueOf(100));
    user.subtractBalance(BigDecimal.valueOf(30));
    assertEquals(BigDecimal.valueOf(70), user.getBalance());
  }

  @Test
  void testAddOwesTo() {
    user.addOwesTo("alice", BigDecimal.valueOf(100));
    Queue<Debt> owesTo = user.getOwesTo();
    assertEquals(1, owesTo.size());
    Debt debt = owesTo.peek();
    assertEquals("alice", debt.getUsername());
    assertEquals(BigDecimal.valueOf(100), debt.getAmount());
  }

  @Test
  void testAddOwesToMultiple() {
    user.addOwesTo("alice", BigDecimal.valueOf(100));
    user.addOwesTo("bob", BigDecimal.valueOf(50));
    Queue<Debt> owesTo = user.getOwesTo();
    assertEquals(2, owesTo.size());
  }

  @Test
  void testAddOwesToSameUserMultipleTimes() {
    user.addOwesTo("alice", BigDecimal.valueOf(100));
    user.addOwesTo("alice", BigDecimal.valueOf(50));
    Queue<Debt> owesTo = user.getOwesTo();
    assertEquals(2, owesTo.size());
  }

  @Test
  void testGetOwesToMap() {
    user.addOwesTo("alice", BigDecimal.valueOf(100));
    user.addOwesTo("alice", BigDecimal.valueOf(50));
    user.addOwesTo("bob", BigDecimal.valueOf(30));
    
    Map<String, BigDecimal> owesToMap = user.getOwesToMap();
    assertEquals(2, owesToMap.size());
    assertEquals(BigDecimal.valueOf(150), owesToMap.get("alice"));
    assertEquals(BigDecimal.valueOf(30), owesToMap.get("bob"));
  }

  @Test
  void testGetOwesToMapEmpty() {
    Map<String, BigDecimal> owesToMap = user.getOwesToMap();
    assertTrue(owesToMap.isEmpty());
  }

  @Test
  void testAddOwedBy() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertEquals(1, owedBy.size());
    assertEquals(BigDecimal.valueOf(100), owedBy.get("alice"));
  }

  @Test
  void testAddOwedByMultipleUsers() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    user.addOwedBy("bob", BigDecimal.valueOf(50));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertEquals(2, owedBy.size());
    assertEquals(BigDecimal.valueOf(100), owedBy.get("alice"));
    assertEquals(BigDecimal.valueOf(50), owedBy.get("bob"));
  }

  @Test
  void testAddOwedByMultipleSameUsers() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    user.addOwedBy("alice", BigDecimal.valueOf(50));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertEquals(1, owedBy.size());
    assertEquals(BigDecimal.valueOf(150), owedBy.get("alice"));
  }

  @Test
  void testDeductOrRemoveOwedByPartial() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    user.deductOrRemoveOwedBy("alice", BigDecimal.valueOf(30));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertEquals(1, owedBy.size());
    assertEquals(BigDecimal.valueOf(70), owedBy.get("alice"));
  }

  @Test
  void testDeductOrRemoveOwedByToZero() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    user.deductOrRemoveOwedBy("alice", BigDecimal.valueOf(100));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertTrue(owedBy.isEmpty());
    assertFalse(owedBy.containsKey("alice"));
  }

  @Test
  void testDeductOrRemoveOwedByBelowZero() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    user.deductOrRemoveOwedBy("alice", BigDecimal.valueOf(150));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertTrue(owedBy.isEmpty());
  }

  @Test
  void testGetOwedBy() {
    user.addOwedBy("alice", BigDecimal.valueOf(100));
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertNotNull(owedBy);
    assertEquals(1, owedBy.size());
  }

  @Test
  void testGetOwedByEmpty() {
    Map<String, BigDecimal> owedBy = user.getOwedBy();
    assertNotNull(owedBy);
    assertTrue(owedBy.isEmpty());
  }

  @Test
  void testOwesAndOwedBy() {
    user.addOwesTo("alice", BigDecimal.valueOf(100));
    user.addOwedBy("bob", BigDecimal.valueOf(50));
    
    assertEquals(1, user.getOwesTo().size());
    assertEquals(1, user.getOwedBy().size());
    assertEquals(BigDecimal.valueOf(100), user.getOwesTo().peek().getAmount());
    assertEquals(BigDecimal.valueOf(50), user.getOwedBy().get("bob"));
  }
}

