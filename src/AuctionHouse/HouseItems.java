package AuctionHouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HouseItems
{
  private List<Item> itemL = new ArrayList();
  private List<String> totalListItems;
  private final HashMap<Item, Integer> itemNPrice = new HashMap<>();

  HouseItems(int numItems)
  {
    makeItems();
    getItemNPrice(numItems);
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
    return copy.subList(0, n);
  }

  private HashMap<Item,Integer> getItemNPrice(int n)
  {
    List<Item> auctionHouseItemList = getNitems(n);
    for(int i = 0; i < auctionHouseItemList.size();i ++)
    {
      Item item = auctionHouseItemList.get(i);
      itemNPrice.put(item,item.getPrice());
      System.out.printf("%s %d%s %-30s %s %d\n", "Item",i,": ", item, "Price: ",item.getPrice());
    }
    return itemNPrice;
  }

  public List<Item> getItemList()
  {
    return itemL;
  }

}
