package atm.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import atm.service.DebtService;
import atm.service.ValidationService;
import atm.util.OutputPresenter;

public class Atm {
  private Map<String, User> users = new HashMap<String, User>();
  private User loggedInUser = null;
  private final DebtService debtSerivce;

  private static final String ERR_USER_NOT_FOUND = "Target user does not exist.";

  public Atm() {
    this.debtSerivce = new DebtService(users);
  }

  // improvement: how to handle concurrent requests
  public void login(String username) {
    ValidationService.validateUsername(username);
    ValidationService.validateMultipleLogin(loggedInUser);
    
    users.putIfAbsent(username, new User(username));
    loggedInUser = users.get(username);
    OutputPresenter.printLoginGreeting(username);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void deposit(BigDecimal amount) {
    ValidationService.validateLoggedIn(loggedInUser);
    ValidationService.validateAmount(amount);

    BigDecimal remaining = debtSerivce.settleDebts(loggedInUser, amount);
    loggedInUser.addBalance(remaining);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void withdraw(BigDecimal amount) {
    ValidationService.validateLoggedIn(loggedInUser);
    ValidationService.validateAmount(amount);

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
    ValidationService.validateLoggedIn(loggedInUser);
    ValidationService.validateAmount(amount);
    ValidationService.validateSelfTransfer(loggedInUser, receiverUsername);

    User receiver = getUser(receiverUsername);
    BigDecimal remainingAmount = debtSerivce.settleOwedAmount(loggedInUser, receiver, amount);
    processBalanceTransfer(receiver, remainingAmount);
    OutputPresenter.printBalance(loggedInUser);
  }

  public void logout() {
    ValidationService.validateLoggedIn(loggedInUser);
    OutputPresenter.printLogoutGreeting(loggedInUser.getUsername());
    loggedInUser = null;
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

  private User getUser(String username) {
    User user = users.get(username);
    if (user == null) {
      throw new IllegalStateException(ERR_USER_NOT_FOUND);
    }
    return user;
  }
}