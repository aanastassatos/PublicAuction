package Bank;

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

  Fund(final int initialBalance)
  {
    total = initialBalance;
  }

  synchronized int getAvailable()
  {
    return total - blocked;
  }

  synchronized void addBlocked(final int amount)
  {
    blocked += amount;
  }

  synchronized void removeBlocked(final int amount)
  {
    if(amount > blocked) throw new RuntimeException("Attempt to unblock more than blocked.");
    blocked -= amount;
  }

  synchronized void withdraw(final int amount)
  {
    if(amount > getAvailable()) throw new RuntimeException("Overdraw");
    total -= amount;
  }

  @Override
  public String toString()
  {
    return "Total: " + total + " Blocked: " + blocked + " Available: " + getAvailable();
  }
}
