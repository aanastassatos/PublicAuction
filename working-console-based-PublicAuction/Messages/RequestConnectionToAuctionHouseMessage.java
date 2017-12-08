package Messages;

import java.io.Serializable;

public class RequestConnectionToAuctionHouseMessage implements Serializable
{
  private final int auctionHouseID;
  private final int agentID;

  
  public RequestConnectionToAuctionHouseMessage(final int auctionHouseID, final int agentID)
  {
    this.auctionHouseID = auctionHouseID;
    this.agentID = agentID;
  }
  
  public int getAuctionHouseID()
  {
    return auctionHouseID;
  }
  
  public int getAgentID()
  {
    return agentID;
  }
}
