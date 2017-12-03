package Messages;

import java.io.Serializable;

public class BidPlacedMessage implements Serializable
{
  private int bidAmount;
  private int publicID;
  private int item;

  //how to choose item??
  //public BidPlacedMessage(int bidAmount,int publicID, AuctionHouse.Item item)
  public BidPlacedMessage(int bidAmount,int publicID)
  {
    bidAmount = bidAmount;
    publicID = publicID;
  }

  public int getPublicID()
  {
    return publicID;
  }

  public int getBidAmount()
  {
    return bidAmount;
  }

  //fix this, need to get the ITEM from the AuctionHouseItemList
  public int getItem()
  {
    return item;
  }
}
