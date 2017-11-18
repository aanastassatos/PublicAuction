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
    blocked -= amount;
  }

  synchronized void withdraw(final int amount)
  {
    total -= amount;
  }
}
