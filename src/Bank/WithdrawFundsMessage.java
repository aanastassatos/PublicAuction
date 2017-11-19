package Bank;

import java.io.Serializable;

public class WithdrawFundsMessage implements Serializable
{
  private final int amount;
  private final int accountNumber;

  public WithdrawFundsMessage(final int account, final int amount)
  {
    this.amount = amount;
    this.accountNumber = account;
  }

  public int getAmount()
  {
    return amount;
  }

  public int getAccountNumber()
  {
    return accountNumber;
  }
}
