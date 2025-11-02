package atm.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;

import atm.model.Debt;
import atm.model.User;
import atm.util.OutputPresenter;

public class DebtService {
  private Map<String, User> users;

  public DebtService(Map<String, User> users) {
    this.users = users;
  }

  // settleDebts is to settle debts from debtor to creditors/users that are owed to
  public BigDecimal settleDebts(User debtor, BigDecimal availableAmount) {
    Queue<Debt> owedTo = debtor.getOwesTo();
    while(!owedTo.isEmpty() && availableAmount.compareTo(BigDecimal.ZERO) > 0) {
      Debt debt = owedTo.peek();
      User userOwedTo = users.get(debt.getUsername());

      if (userOwedTo == null) {
        owedTo.poll();
        continue;
      }

      // If available amount is greater than or equal to debt amount,
      // we will remove from queue and deduct from owedBy map
      // else we will reduce the debt amount and deduct from owedBy map
      if (availableAmount.compareTo(debt.getAmount()) >= 0) {
        availableAmount = availableAmount.subtract(debt.getAmount());
        userOwedTo.deductOrRemoveOwedBy(debtor.getUsername(), debt.getAmount());
        userOwedTo.addBalance(debt.getAmount());
        OutputPresenter.printTransfer(debt.getUsername(), debt.getAmount());
        owedTo.poll();
      } else {
        debt.reduceAmount(availableAmount);
        userOwedTo.deductOrRemoveOwedBy(debtor.getUsername(), availableAmount);
        userOwedTo.addBalance(availableAmount);
        OutputPresenter.printTransfer(debt.getUsername(), availableAmount);
        availableAmount = BigDecimal.ZERO;
      }
    }
    return availableAmount;
  }

  // settleOwedAmount is tocheck if sender is owed by receiver, then we will adjust the owedBy and owesTo
  public BigDecimal settleOwedAmount(User sender, User receiver, BigDecimal amount) {
    Map<String, BigDecimal> owedBy = sender.getOwedBy();
    String receiverUsername = receiver.getUsername();

    if (owedBy.containsKey(receiverUsername)) {
      BigDecimal debtAmount = owedBy.get(receiverUsername);
      Queue<Debt> owesTo = receiver.getOwesTo();

      if (amount.compareTo(debtAmount) >= 0) {
        // Transfer amount covers or exceed debt
        amount = amount.subtract(debtAmount);
        owedBy.remove(receiverUsername);
        removeDebtFromQueue(owesTo, sender.getUsername());
      } else {
        // Transfer amount is less than debt
        owedBy.put(receiverUsername, debtAmount.subtract(amount));
        reduceDebtInQueue(owesTo, sender.getUsername(), amount);
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

  private void reduceDebtInQueue(Queue<Debt> owesTo, String username, BigDecimal amount) {
    for (Debt debt : owesTo) {
      if (debt.getUsername().equals(username)) {
        debt.reduceAmount(amount);
        break;
      }
    }
  }
}
