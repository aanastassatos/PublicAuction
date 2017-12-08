package Messages;

import java.io.Serializable;

public class BankAccountInfoMessage implements Serializable
{
  private final int accountNumber;
  private final int secretKey;

  public BankAccountInfoMessage(final int accountNumber, final int secretKey)
  {
    this.accountNumber = accountNumber;
    this.secretKey = secretKey;
  }

  public int getAccountNumber()
  {
    return accountNumber;
  }

  public int getSecretKey()
  {
    return secretKey;
  }
}
