package Messages;

import java.io.Serializable;
import java.util.UUID;

public class BlockFundsResultMessage implements Serializable
{
  private final boolean result;
  private final UUID transactionId;

  public BlockFundsResultMessage(final boolean result, final UUID transactionId)
  {
    this.result = result;
    this.transactionId = transactionId;
  }

  public boolean getResult()
  {
    return result;
  }

  public UUID getTransactionId()
  {
    return transactionId;
  }
}
