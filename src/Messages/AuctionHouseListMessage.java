package Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AuctionHouseListMessage implements Serializable
{
  private final HashMap<Integer, String> auctionHouseList;
  
  public AuctionHouseListMessage(final HashMap<Integer, String> auctionHouseList)
  {
    this.auctionHouseList = auctionHouseList;
  }
  
  public HashMap<Integer, String> getAuctionHouseList()
  {
    return auctionHouseList;
  }
}
