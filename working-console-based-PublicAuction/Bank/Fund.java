package Bank;

import java.util.LinkedList;

/**
 * Encapsulates financial transactions
 * When funds are blocked, an agent has placed a bid on an auction
 * If the agent wins the auction, the block will be removed, then the money withdrawn
 * If the agent loses, only the block is removed, with the actual amount of money in the account being unchanged
 * This prevents the situation where an agent bids on two auctions at the same type and the act of winning one makes the
 * value of the account to low to pay for the other
 */
class Fund
{
  private int total, blocked;

  private final LinkedList<String> transactionHistory = new LinkedList<>();

  Fund(final int initialBalance)
  {
    total = initialBalance;
    transactionHistory.add("Created new account with an initial balance of " + initialBalance);
  }

  synchronized LinkedList<String> getTransactionHistory()
  {
    return transactionHistory;
  }

  synchronized int getAvailable()
  {
    return total - blocked;
  }

  synchronized void addBlocked(final int amount)
  {
    blocked += amount;
    transactionHistory.add("Blocked " + amount + " resulting in " + toString());
  }

  synchronized void removeBlocked(final int amount)
  {
    if(amount > blocked) throw new RuntimeException("Attempt to unblock more than blocked.");
    blocked -= amount;
    transactionHistory.add("Unblocked " + amount + " resulting in " + toString());
  }

  synchronized void withdraw(final int amount)
  {
    if(amount > getAvailable()) throw new RuntimeException("Overdraw");
    total -= amount;
    transactionHistory.add("Withdrew " + amount + " resulting in " + toString());
  }

  @Override
  public String toString()
  {
    return "Total: " + total + " Blocked: " + blocked + " Available: " + getAvailable();
  }
}
