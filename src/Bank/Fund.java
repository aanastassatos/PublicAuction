package Bank;

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
