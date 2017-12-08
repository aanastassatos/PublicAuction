package Messages;

import java.io.Serializable;

public class DeregisterAuctionHouseResultMessage implements Serializable
{
  private final boolean result;
  
  public DeregisterAuctionHouseResultMessage(boolean result)
  {
    this.result = result;
  }
  
  public boolean getResult()
  {
    return result;
  }
}
