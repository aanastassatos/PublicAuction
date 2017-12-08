package AuctionHouse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HouseItems
{
  private HashMap<Integer, Item> theRestOfTheItemsList = new HashMap<>();
  private HashMap<Integer,Item> currentHouseItems = new HashMap<>();
  private Item newItem;

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

  //*************************************************************************************
  //Each parameter's type and name: int n
  //Method's return value : void
  //Description of what the method does.
  // - Make items
  // ************************************************************************************
  synchronized private void makeItems(int n)
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
      if (i < 3)
      {
        item.setID(i);
        currentHouseItems.put(item.getID(), item);
      }
      //The rest should go to auctionHouseItemList
      else if (i >= 3)
      {
        item.setID(i);
        theRestOfTheItemsList.put(item.getID(), item);
      }
    }
  }
  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - update the item list by adding 1 to the current List
  // ************************************************************************************
  void updateItemList()
  {
    if(theRestOfTheItemsList.size() > 0)
    {
      Map.Entry<Integer,Item> entry = theRestOfTheItemsList.entrySet().iterator().next();
      Integer itemID = entry.getKey();
      newItem =entry.getValue();
      currentHouseItems.put(itemID,newItem);
      theRestOfTheItemsList.remove(itemID,newItem);
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : boolean
  //Description of what the method does.
  // - if no more new items, return true
  // - else return false
  // ************************************************************************************
  boolean noMoreNewItem()
  {
    if(theRestOfTheItemsList.size() <=0) return true;
    else return false;
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : boolean
  //Description of what the method does.
  // - if all items are sold, return true
  // - else return false
  // ************************************************************************************
  boolean allItemsAreSold()
  {
    if(noMoreNewItem() && currentHouseItems.size()<=0) return true;
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

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :  HashMap<Integer,Item>
  //Description of what the method does.
  // - get the items map from current auction house list
  // *******************************************************
  HashMap<Integer,Item> getCurrentHouseItems()
  {
    return currentHouseItems;
  }

  Item getNewItem()
  {
    return newItem;
  }
}



