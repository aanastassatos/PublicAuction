package Bank;

import java.io.Serializable;

public class ModifyBlockedFundsMessage implements Serializable
{
  enum TransactionType
  {
    Add, Remove;
  };

  private final int amount;
  private final int accountNumber;
  private final TransactionType type;

  public ModifyBlockedFundsMessage(final int account, final int amount, TransactionType type)
  {
    this.amount = amount;
    this.accountNumber = account;
    this.type = type;
  }

  public int getAmount()
  {
    return amount;
  }

  public int getAccountNumber()
  {
    return accountNumber;
  }

  public TransactionType getType()
  {
    return type;
  }
}
