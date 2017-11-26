package Messages;

public class RequestConnectionToAuctionHouseMessage
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
