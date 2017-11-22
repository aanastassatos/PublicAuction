package Messages;

import java.io.Serializable;

public class AgentInfoMessage implements Serializable
{
  private final int biddingKey;
  
  public AgentInfoMessage(final int biddingKey)
  {
    this.biddingKey = biddingKey;
  }
  
  public int getBiddingKey()
  {
    return biddingKey;
  }
}
