package Messages;

import java.io.Serializable;
import java.util.UUID;

public class ModifyBlockedFundsMessage implements Serializable
{

  public enum TransactionType
  {
    Add, Remove;
  };

  private final int amount;
  private final int accountNumber;
  private final TransactionType type;
  private final UUID transactionId;

  public ModifyBlockedFundsMessage(final int account, final int amount, final TransactionType type, final UUID transactionId)
  {
    this.amount = amount;
    this.accountNumber = account;
    this.type = type;
    this.transactionId = transactionId;
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

  public UUID getTransactionId()
  {
    return transactionId;
  }
}
