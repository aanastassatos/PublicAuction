package Messages;

import AuctionHouse.Item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by catherinewright on 12/4/17.
 */
public class AuctionHouseItemsListMessage implements Serializable
{
  ArrayList<Item> itemsList;

  AuctionHouseItemsListMessage(int auctionHouseID)
  {

  }

  public ArrayList<Item> getItems()
  {
    return itemsList;
  }
}
