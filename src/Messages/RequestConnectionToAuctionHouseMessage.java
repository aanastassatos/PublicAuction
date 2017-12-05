package Messages;

import java.io.Serializable;

public class RequestConnectionToAuctionHouseMessage implements Serializable
{
  private final int auctionHouseID;

  
  public RequestConnectionToAuctionHouseMessage(final int auctionHouseID)
  {
    this.auctionHouseID = auctionHouseID;
  }
  
  public int getAuctionHouseID()
  {
    return auctionHouseID;
  }
}
