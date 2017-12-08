package AuctionHouse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HouseItems
{
  private final List<String> ITEMLIST = Arrays.asList("PERSONAL ASSISTANT", "CUSTOM ARTWORK", "SIGNED CD", "TRAVEL PACKAGE", "TALK SHOW TICKETS",
      "POKEMON CARDS", "VIP INSIDERS TOUR", "SIGNED JERSEYS", "BASKETBALL TICKETS", "HANDMADE CHAIR",
      "GOLF LESSONS", "YOGA SESSIONS", "BOXING TICKETS", "DINNER WITH JOEL", "CODING LESSONS",
      "SNL TICKETS", "BEACH HOUSE DEAL", "PHOTO SHOOT", "MAGIC LESSONS", "BIG TV", "WINE TASTING", "CAR PARTS",
      "HAMILTON EXPERIENCE", "ONE YEAR FREE OIL CHANGE", "ROUND TRIP FOR TWO", "DISNEY TRIP", "HOT AIR BALLOON RIDE",
      "BOATING CRUISE");
  
  private HashMap<Integer,Item> currentHouseItems = new HashMap<>();
  
  HouseItems(int numItems)
  {
    makeItems(numItems);
  }
  
  //*************************************************************************************
  //Each parameter's type and name: int n
  //Method's return value : void
  //Description of what the method does.
  // - Make items
  // ************************************************************************************
  private void makeItems(int n)
  {
    Collections.shuffle(ITEMLIST);
    
    for(int i = 0; i < n; i++)
    {
      Item item = new Item(ITEMLIST.get(i));
      currentHouseItems.put(item.getItem().hashCode(), item);
    }
  }

//  //*************************************************************************************
//  //Each parameter's type and name: none
//  //Method's return value : void
//  //Description of what the method does.
//  // - update the item list by adding 1 to the current List
//  // ************************************************************************************
//  void updateItemList()
//  {
//    if(theRestOfTheItemsList.size() > 0)
//    {
//      Map.Entry<Integer,Item> entry = theRestOfTheItemsList.entrySet().iterator().next();
//      Integer itemID = entry.getKey();
//      Item itemName =entry.getValue();
//      currentHouseItems.put(itemID,itemName);
//      theRestOfTheItemsList.remove(itemID,itemName);
//    }
//  }
//
//  //*************************************************************************************
//  //Each parameter's type and name: none
//  //Method's return value : boolean
//  //Description of what the method does.
//  // - if no more new items, return true
//  // - else return false
//  // ************************************************************************************
//  boolean noMoreNewItem()
//  {
//    if(currentHouseItems.size() <=0) return true;
//    else return false;
//  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : boolean
  //Description of what the method does.
  // - if all items are sold, return true
  // - else return false
  // ************************************************************************************
  boolean allItemsAreSold()
  {
    if(currentHouseItems.size() == 0) return true;
    else return false;
  }

  //*************************************************************************************
  //Each parameter's type and name: int itemID
  //Method's return value : void
  //Description of what the method does.
  // - Remove an item when sold
  // ************************************************************************************
  synchronized void removeItem(int itemID)
  {
    currentHouseItems.remove(itemID);
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value :  HashMap<Integer,Item>
  //Description of what the method does.
  // - get the items map from current auction house list
  // ************************************************************************************
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

