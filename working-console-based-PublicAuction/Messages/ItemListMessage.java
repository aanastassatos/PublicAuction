package Messages;

import AuctionHouse.Item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ItemListMessage implements Serializable
{
  private final HashMap<Integer, Item> itemList;
  private final int auctionHouseID;
  
  public ItemListMessage(final HashMap<Integer, Item> itemList, final int auctionHouseID)
  {
    this.itemList = itemList;
    this.auctionHouseID = auctionHouseID;
  }
  
  public HashMap<Integer, Item> getItemList()
  {
    return itemList;
  }
  
  public int getAuctionHouseID()
  {
    return auctionHouseID;
  }
}
