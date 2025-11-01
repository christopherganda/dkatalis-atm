package atm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import util.Formatter;

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
    System.out.printf("Hello, %s!\n", loggedInUser.getUsername());
    printYourBalance();
  }

  public void deposit(BigDecimal amount) {
    if (loggedInUser == null) {
      throw new IllegalStateException("You must logged in before doing deposit");
    }

    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount should not be lower than 0");
    }

    amount = processDepositOwes(amount);
    loggedInUser.addBalance(amount);
    printYourBalance();
  }

  public void withdraw(BigDecimal amount) {
    if (loggedInUser == null) {
      throw new IllegalStateException("You must logged in before doing deposit");
    }

    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount should not be lower than 0");
    }

    if (loggedInUser.getBalance().compareTo(amount) < 0) {
      throw new IllegalStateException("Your remaining balance is " + loggedInUser.getBalance());
    }

    loggedInUser.subtractBalance(amount);
    printYourBalance();
  }

  // There are 2 cases for transfer
  // 1. if loggedInUser is owed by the targetUser, if yes we need to adjust the owes and owed, if amount has remaining, we deduct loggedInUser's balance
  // 2. if loggedInUser is not owed by targetUser, we deduct loggedInUser's balance, if amount is greater than balance, then we need to add owes and owed to
  public void transfer(String receiverUsername, BigDecimal amount) {
    if (loggedInUser == null) {
      throw new IllegalStateException("You must logged in before doing transfer");
    }

    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount should not be lower than 0");
    }

    if (receiverUsername.equals(loggedInUser.getUsername())) {
      throw new IllegalStateException("Cannot transfer to self");
    }

    User receiver = users.get(receiverUsername);
    if (receiver == null) {
      throw new IllegalStateException("Target user does not exist.");
    }

    BigDecimal remainingAmount = processTransferOwedBy(receiver, amount);
    if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal senderBalance = loggedInUser.getBalance();
      if (senderBalance.compareTo(remainingAmount) >= 0) {
        loggedInUser.subtractBalance(remainingAmount);
        receiver.addBalance(remainingAmount);
        printTransferredToMsg(receiverUsername, remainingAmount);
      } else {
        BigDecimal overBalance = remainingAmount.subtract(senderBalance);
        loggedInUser.subtractBalance(senderBalance); // set balance to 0
        receiver.addBalance(senderBalance);
        receiver.addOwedBy(loggedInUser.getUsername(), overBalance);
        loggedInUser.addOwesTo(receiverUsername, overBalance);
        printTransferredToMsg(receiverUsername, senderBalance);
      }
    }
    printYourBalance();
  }

  public void logout() {
    if (loggedInUser == null) {
      throw new IllegalStateException("System is not logged in to any user");
    }
    System.out.printf("Goodbye, %s!\n", loggedInUser.getUsername());
    loggedInUser = null;
  }

  // The logic idea behind owesTo and owedBy
  // user1 balance 100
  // user2 0

  // user1 transfer 150 to user 2
  // user1 balance 0, user2 balance 150
  // user1 owesTo(Queue) user2 50
  // user2 owedBy(Map) user1 50

  // let's say user1 do deposit
  // user1 deposit 50
  // user1 balance 0, user2 balance 150
  // user1 owesTo(Queue) user2 has to be removed
  // user2 owedBy(Map) user1 has to be removed

  // let's say if user2 do the transfer
  // user2 transfer to user1 50
  // user1 balance 50, user2 balance 100
  // user2 owedBy(map) user1 has to be removed
  // user1 owesTo(Queue) has to be removed

  // Iterate loggedInUser.getOwesTo
  // then deduct amount based on queue
  // or remove from queue if amount is greater than owes amount.
  private BigDecimal processDepositOwes(BigDecimal amount) {
    Queue<Debt> owedTo = loggedInUser.getOwesTo();
    while(!owedTo.isEmpty() && amount.compareTo(BigDecimal.ZERO) > 0) {
      Debt debt = owedTo.peek();
      User userOwedTo = users.get(debt.getUsername());
      if (amount.compareTo(debt.getAmount()) > 0) {
        amount = amount.subtract(debt.getAmount());
        userOwedTo.deductOrRemoveOwedBy(loggedInUser.getUsername(), debt.getAmount());
        userOwedTo.addBalance(debt.getAmount());
        printTransferredToMsg(debt.getUsername(), debt.getAmount());
        owedTo.poll();
      } else {
        debt.reduceAmount(amount);
        userOwedTo.deductOrRemoveOwedBy(loggedInUser.getUsername(), amount);
        userOwedTo.addBalance(amount);
        printTransferredToMsg(debt.getUsername(), amount);
        amount = BigDecimal.ZERO;
      }
    }
    return amount;
  }

  private BigDecimal processTransferOwedBy(User receiver, BigDecimal amount) {
    Map<String, BigDecimal> owedBy = loggedInUser.getOwedBy();
    String receiverUsername = receiver.getUsername();

    if (owedBy.containsKey(receiverUsername)) {
      BigDecimal debtAmount = owedBy.get(receiverUsername);
      Queue<Debt> owesTo = receiver.getOwesTo();

      if (amount.compareTo(debtAmount) >= 0) { // amount transferred >= debtAmount
        amount = amount.subtract(debtAmount);
        owedBy.remove(receiverUsername);
        removeDebtFromQueue(owesTo, loggedInUser.getUsername());
      } else { // amount < debtAmount
        owedBy.put(receiverUsername, debtAmount.subtract(amount));
        for (Debt debt : owesTo) {
          if (debt.getUsername().equals(loggedInUser.getUsername())) {
            debt.reduceAmount(amount);
            break;
          }
        }
        amount = BigDecimal.ZERO;
      }
    }

    return amount;
  }

  private void removeDebtFromQueue(Queue<Debt> owesTo, String username) {
    int size = owesTo.size();
    for (int i = 0; i < size; i++) {
      Debt debt = owesTo.poll();
      if (!debt.getUsername().equals(username)) {
        owesTo.offer(debt);
      }
    }
  }

  private void printYourBalance() {
    System.out.printf("Your balance is %s\n", loggedInUser.getBalanceString());

    for(Map.Entry<String, BigDecimal> entry : loggedInUser.getOwesToMap().entrySet()) {
      System.out.printf("Owed %s to %s\n", Formatter.formatCurrencyWithoutComma(entry.getValue()), entry.getKey());
    }

    for (Map.Entry<String, BigDecimal> entry : loggedInUser.getOwedBy().entrySet()) {
      System.out.printf("Owed %s from %s\n", Formatter.formatCurrencyWithoutComma(entry.getValue()), entry.getKey());
    }
  }

  private void printTransferredToMsg(String username, BigDecimal amount) {
    System.out.printf("Transferred %s to %s\n", Formatter.formatCurrencyWithoutComma(amount), username);
  }
}