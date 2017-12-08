package AuctionHouse;

import java.util.*;

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