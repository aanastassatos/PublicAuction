package Messages;

import java.io.Serializable;

public class BidPlacedMessage implements Serializable
{
  private int bidAmount;
  private int biddingKey;
  private int auctionHouseID;
  private int itemID;

  public BidPlacedMessage(int biddingKey, int auctionHouseID, int itemID, int bidAmount)
  {
    this.biddingKey = biddingKey;
    this.auctionHouseID = auctionHouseID;
    this.itemID = itemID;
    this.bidAmount = bidAmount;
  }

  public int getBidAmount()
  {
    return bidAmount;
  }

  //fix this, need to get the ITEM from the AuctionHouseItemList
  public int getItemID()
  {
    return itemID;
  }

  public int getBiddingKey()
  {
    return biddingKey;
  }

  public int getAuctionHouseID()
  {
    return auctionHouseID;
  }
}
