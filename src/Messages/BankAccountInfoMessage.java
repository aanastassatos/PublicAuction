package Messages;

import java.io.Serializable;

public class BankAccountInfoMessage implements Serializable
{
  private final int accountNumber;

  public BankAccountInfoMessage(int accountNumber)
  {
    this.accountNumber = accountNumber;
  }

  public int getAccountNumber()
  {
    return accountNumber;
  }
}
