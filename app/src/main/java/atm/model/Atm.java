package atm.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import atm.util.OutputPresenter;

public class Atm {
  private Map<String, User> users = new HashMap<String, User>();
  private User loggedInUser = null;

  private static final String ERR_NO_USER_LOGGED_IN = "You must be logged in to perform this action.";
  private static final String ERR_INVALID_AMOUNT = "Amount should be greater than or equal to 0.";
  private static final String ERR_INVALID_USERNAME = "Username cannot be empty";
  private static final String ERR_SELF_TRANSFER = "Cannot transfer to self.";
  private static final String ERR_USER_NOT_FOUND = "Target user does not exist.";

  // improvement: how to handle concurrent requests
  public void login(String username) {
    validateUsername(username);
    validateMultipleLoginAttempt();
    
    users.putIfAbsent(username, new User(username));
    loggedInUser = users.get(username);
    OutputPresenter.printLoginGreeting(username);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void deposit(BigDecimal amount) {
    validateLoggedIn();
    validateAmount(amount);

    amount = processDepositOwes(amount);
    loggedInUser.addBalance(amount);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void withdraw(BigDecimal amount) {
    validateLoggedIn();
    validateAmount(amount);

    if (loggedInUser.getBalance().compareTo(amount) < 0) {
      throw new IllegalStateException("Your remaining balance is " + loggedInUser.getBalance());
    }

    loggedInUser.subtractBalance(amount);
    OutputPresenter.printBalance(loggedInUser);
  }

  // There are 2 cases for transfer
  // 1. if loggedInUser is owed by the targetUser, if yes we need to adjust the owes and owed, if amount has remaining, we deduct loggedInUser's balance
  // 2. if loggedInUser is not owed by targetUser, we deduct loggedInUser's balance, if amount is greater than balance, then we need to add owes and owed to
  public void transfer(String receiverUsername, BigDecimal amount) {
    validateLoggedIn();
    validateAmount(amount);
    validateSelfTransfer(receiverUsername);

    User receiver = getUser(receiverUsername);
    BigDecimal remainingAmount = processTransferOwedBy(receiver, amount);
    processBalanceTransfer(receiver, remainingAmount);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void logout() {
    if (loggedInUser == null) {
      throw new IllegalStateException(ERR_NO_USER_LOGGED_IN);
    }
    OutputPresenter.printLogoutGreeting(loggedInUser.getUsername());
    loggedInUser = null;
  }

  // Private Helpers
  // Iterate loggedInUser.getOwesTo
  // then deduct amount based on queue
  // or remove from queue if amount is greater than owes amount.
  private BigDecimal processDepositOwes(BigDecimal amount) {
    Queue<Debt> owedTo = loggedInUser.getOwesTo();
    while(!owedTo.isEmpty() && amount.compareTo(BigDecimal.ZERO) > 0) {
      Debt debt = owedTo.peek();
      User userOwedTo = users.get(debt.getUsername());
      // If amount is greater than debt amount, we settle all the debt
      // else we deduct the debt according to amount
      if (amount.compareTo(debt.getAmount()) >= 0) {
        amount = amount.subtract(debt.getAmount());
        userOwedTo.deductOrRemoveOwedBy(loggedInUser.getUsername(), debt.getAmount());
        userOwedTo.addBalance(debt.getAmount());
        OutputPresenter.printTransfer(debt.getUsername(), debt.getAmount());
        owedTo.poll();
      } else {
        debt.reduceAmount(amount);
        userOwedTo.deductOrRemoveOwedBy(loggedInUser.getUsername(), amount);
        userOwedTo.addBalance(amount);
        OutputPresenter.printTransfer(debt.getUsername(), amount);
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

      // If transfer amount is greater than the owedBy debt, we will
      // remove owedBy and from queue, remaining amount will deduct balance
      // If transfer amount is lower than owedBy debt,
      // we will deduct debtAmount from owedBy and queue.
      if (amount.compareTo(debtAmount) >= 0) {
        amount = amount.subtract(debtAmount);
        owedBy.remove(receiverUsername);
        removeDebtFromQueue(owesTo, loggedInUser.getUsername());
      } else {
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

  // If senderBalance is greater than remaining amount to be transferred,
  // Then we will just deduct senderBalance without any owe
  // Else we will deduct based on senderBalance
  // And we will process owedBy and OwesTo
  private void processBalanceTransfer(User receiver, BigDecimal remainingAmount) {
    String receiverUsername = receiver.getUsername();
    if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal senderBalance = loggedInUser.getBalance();
      if (senderBalance.compareTo(remainingAmount) >= 0) {
        loggedInUser.subtractBalance(remainingAmount);
        receiver.addBalance(remainingAmount);
        OutputPresenter.printTransfer(receiverUsername, remainingAmount);
      } else {
        BigDecimal overBalance = remainingAmount.subtract(senderBalance);
        loggedInUser.subtractBalance(senderBalance);
        receiver.addBalance(senderBalance);
        receiver.addOwedBy(loggedInUser.getUsername(), overBalance);
        loggedInUser.addOwesTo(receiverUsername, overBalance);
        OutputPresenter.printTransfer(receiverUsername, senderBalance);
      }
    }
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

  private void validateUsername(String username) {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException(ERR_INVALID_USERNAME);
    }
  }

  private void validateMultipleLoginAttempt() {
    if (loggedInUser != null) {
      throw new IllegalStateException("Currently logged in as: " + loggedInUser);
    }
  }

  private void validateLoggedIn() {
    if (loggedInUser == null) {
      throw new IllegalStateException(ERR_NO_USER_LOGGED_IN);
    }
  }

  private void validateAmount(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(ERR_INVALID_AMOUNT);
    }
  }
  
  private void validateSelfTransfer(String receiverUsername) {
    if (loggedInUser.getUsername().equals(receiverUsername)) {
      throw new IllegalStateException(ERR_SELF_TRANSFER);
    }
  }

  private User getUser(String username) {
    User user = users.get(username);
    if (user == null) {
      throw new IllegalStateException(ERR_USER_NOT_FOUND);
    }
    return user;
  }
}