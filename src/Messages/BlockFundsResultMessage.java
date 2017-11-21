package Messages;

import java.io.Serializable;

public class BlockFundsResultMessage implements Serializable
{
  private final boolean result;

  public BlockFundsResultMessage(boolean result)
  {
    this.result = result;
  }

  public boolean getResult()
  {
    return result;
  }
}
