package AuctionHouse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HouseItems
{
  private HashMap<Integer, Item> theRestOfTheItemsList = new HashMap<>();
  private HashMap<Integer,Item> currentHouseItems = new HashMap<>();

  HouseItems(int numItems)
  {
    makeItems(numItems);
  }

  enum ItemsList
  {
    PERSONAL_ASSISTANT, CUSTOM_ARTWORK, SIGNED_CD, TRAVEL_PACKAGE, TALKSHOW_TICKETS,
    POKEMONCARDS, VIP_INSIDERS_TOUR, SIGNED_JERSEYS, BASKETBALL_TICKETS, HANDMADE_CHAIR,
    GOLF_LESSONS, YOGA_SESSIONS, BOXING_TICKETS, DINNER_WITH_CELEBS, CODING_LESSONS,
    SNL_TICKETS, BEACHHOUSE_DEAL, PHOTO_SHOOT, MAGIC_LESSONS, TVs, WINE_TASTING, CAR_PARTS,
    HAMILTON_EXPERIENCE, ONE_YEAR_FREE_OIL_CHANGE, ROUND_TRIP_FOR_TWO, DISNEY_TRIP, HOT_AIR_BALLOONS_RIDE,
    BOATING_CRUISE
  }

  private void makeItems(int n)
  {
    List<String> totalListItems = Stream.of(ItemsList.values())
            .map(ItemsList::name)
            .collect(Collectors.toList());
    Collections.shuffle(totalListItems);
    Item item;
    for (int i = 0; i < n; i++)
    {
      item = new Item(totalListItems.get(i));
      //Item number from 0 -> 2 go to currentHouseItems
      while(i < 3)
      {
        item.setID(i);
        currentHouseItems.put(item.getID(),item);
      }
      //The rest should go to auctionHouseItemList
      while(i >= 3)
      {
        item.setID(i);
        theRestOfTheItemsList.put(item.getID(), item);
      }
    }
  }

  //HOW TO GET THE ID FROM THE ITEM LIST???
  void updateItemList()
  {
    if(theRestOfTheItemsList.size() > 0)
    {
      Map.Entry<Integer,Item> entry = theRestOfTheItemsList.entrySet().iterator().next();
      Integer itemID = entry.getKey();
      Item itemName =entry.getValue();
      currentHouseItems.put(itemID,itemName);
      theRestOfTheItemsList.remove(itemID,itemName);
    }
  }

  void removeItem(int itemID)
  {
    for( Integer id : currentHouseItems.keySet())
    {
      // == OR EQUALS??
      if(id == itemID)
      {
        Item name = currentHouseItems.get(id);
        currentHouseItems.remove(id,name);
      }
    }
  }

  HashMap<Integer,Item> getCurrentHouseItems()
  {
    return currentHouseItems;
  }
}


/*Iterator iter = auctionHouseItemList.entrySet().iterator();
    while(iter.hasNext())
    {
      Map.Entry pair = (Map.Entry) iter.next();
      if(pair.getKey().equals(itemID)) auctionHouseItemList.remove(pair.getKey());
    }*/

  /*HashMap<Integer, Item> getAuctionHouseItemList()
  {
    return auctionHouseItemList;
  }*/

//  String printList(int auctionHouseID)
//  {
//    String str = "";
//    if(auctionHouseItemList.size() > 0)
//    {
//      for(int i = 0; i < itemL.size(); i++)
//      {
//        Item item = itemL.get(i);
//        str += "Auction House ID: " +auctionHouseID+ "\nItem ID: " + item.getID() + "Item Name: " +item.getItem() + "Highest Bidder: " +item.getHighestBidderKey()+
//                " Highest Bid: " +item.getHighestBid()+"\n";
//      }
//    }
//    else str+= "No more Items";
//    return str;
//  }
/*

//  private void setItemNPrice(int numItems)
//  {
//    //INITIAL THE ITEMS AND INITIAL PRICES FOR EACH
//    auctionHouseItemList = getNitems(numItems);
//    for(int i = 0; i < auctionHouseItemList.size();i ++)
//    {
//      Item item = auctionHouseItemList.get(i);
//      itemNCurrHighestBid.put(item.getID(),item.getPrice());
//    }
//  }

  //synchronized?
  int getCurrentHighestBid(int itemID)
  {
    Iterator iter = itemNCurrHighestBid.entrySet().iterator();
    while (iter.hasNext())
    {
      Map.Entry pair = (Map.Entry) iter.next();
      //equals correctly?
      if (pair.getKey().equals(itemID))
      {
        int bidAmount = itemNCurrHighestBid.get(pair.getKey());
        return bidAmount;
      }
    }
    return -1;
  }
 */

