package Messages;

import AuctionHouse.Item;

import java.io.Serializable;
import java.util.List;

public class ItemListMessage implements Serializable
{
  private final List<Item> itemList;
  private final int auctionHouseID;
  
  public ItemListMessage(final List<Item> itemList, final int auctionHouseID)
  {
    this.itemList = itemList;
    this.auctionHouseID = auctionHouseID;
  }
  
  public List<Item> getItemList()
  {
    return itemList;
  }
  
  public int getAuctionHouseID()
  {
    return auctionHouseID;
  }
}
