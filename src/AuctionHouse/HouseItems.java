package AuctionHouse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HouseItems
{
  private List<Item> itemL = new ArrayList();
  private List<Item> auctionHouseItemList = new ArrayList<>();
  private List<String> totalListItems;
  // Map of itemID and highestBid
  private final HashMap<Integer, Integer> itemNCurrHighestBid = new HashMap<>();

  //Map of a auctionhouse id to of the map of itemID and its current highest bid
  private final HashMap<Integer,HashMap<Integer,Integer>> auctionHouseItems = new HashMap<>();

  HouseItems(int numItems,int auctionHouseID)
  {
    makeItems();
    setItemNPrice(auctionHouseID,numItems);
    printList(auctionHouseID);
  }
  private final int totalItems = ItemsList.values().length;

  enum ItemsList
  {
    PERSONAL_ASSISTANT,CUSTOM_ARTWORK, SIGNED_CD, TRAVEL_PACKAGE, TALKSHOW_TICKETS,
    POKEMONCARDS, VIP_INSIDERS_TOUR, SIGNED_JERSEYS, BASKETBALL_TICKETS, HANDMADE_CHAIR,
    GOLF_LESSONS, YOGA_SESSIONS, BOXING_TICKETS, DINNER_WITH_CELEBS, CODING_LESSONS,
    SNL_TICKETS, BEACHHOUSE_DEAL, PHOTO_SHOOT, MAGIC_LESSONS, TVs, WINE_TASTING, CAR_PARTS,
    HAMILTON_EXPERIENCE, ONE_YEAR_FREE_OIL_CHANGE, ROUND_TRIP_FOR_TWO, DISNEY_TRIP, HOT_AIR_BALLOONS_RIDE,
    BOATING_CRUISE
  }

  private void makeItems()
  {
    totalListItems = Stream.of(ItemsList.values())
            .map(ItemsList::name)
            .collect(Collectors.toList());
    for(int i = 0; i< totalItems;i++)
    {
      Item j = new Item(totalListItems.get(i));
      itemL.add(j);
    }
  }

  private List<Item> getNitems(int n)
  {
    makeItems();
    List<Item> copy = new ArrayList<>(itemL);
    Collections.shuffle(copy);
    List<Item> list =  copy.subList(0, n);
    for(int i = 0; i <list.size(); i++)
    {
      list.get(i).setID(i);
    }
    return list;
  }

  private void setItemNPrice(int houseID, int numItems)
  {
    //INITIAL THE ITEMS AND INITIAL PRICES FOR EACH
    this.auctionHouseItemList = getNitems(numItems);
    for(int i = 0; i < auctionHouseItemList.size();i ++)
    {
      Item item = auctionHouseItemList.get(i);
      itemNCurrHighestBid.put(item.getID(),item.getPrice());
    }
    auctionHouseItems.put(houseID,itemNCurrHighestBid);
  }

  HashMap<Integer,Integer> getItemNCurrBid(int houseID)
  {
    return auctionHouseItems.get(houseID);
  }

  //synchronized?
  int getCurrentHighestBid(int houseID, int itemID)
  {
    HashMap<Integer,Integer> getItemNCurrBid = getItemNCurrBid(houseID);
    Iterator iter = getItemNCurrBid.entrySet().iterator();
    while (iter.hasNext())
    {
      Map.Entry pair = (Map.Entry) iter.next();
      //equals correctly?
      if (pair.getKey().equals(itemID))
      {
        int bidAmount = getItemNCurrBid.get(pair.getKey());
        return bidAmount;
      }
    }
    return -1;
  }

  void removeItem(int houseID, int itemID)
  {
    HashMap<Integer,Integer> getItemNCurrBid = getItemNCurrBid(houseID);
    Iterator i = getItemNCurrBid.entrySet().iterator();
    while (i.hasNext())
    {
      Map.Entry pair = (Map.Entry) i.next();
      if (pair.getKey().equals(itemID))
      {
        getItemNCurrBid.remove(pair.getKey());
      }
    }
  }

  String printList(int auctionHouseID)
  {
    String str = "";
    if(auctionHouseItemList.size() > 0)
    {
      for(int i = 0; i < itemL.size(); i++)
      {
        Item item = itemL.get(i);
        str += "Auction House ID: " +auctionHouseID+ "\nItem ID: " + item.getID() + "Item Name: " +item.getItem() + "Highest Bidder: " +item.getHighestBidderKey()+
                " Highest Bid: " +item.getHighestBid()+"\n";
      }
    }
    else str+= "No more Items";
    return str;
  }
}
