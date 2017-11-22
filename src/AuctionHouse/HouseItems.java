package AuctionHouse;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HouseItems
{
  private List<String> itemL = new ArrayList();
  private List<String> houseItems = new ArrayList();
  private Item item;

  HouseItems(int numItems)
  {
    makeItems();
    houseItems = getNitems(numItems);
    for(int i = 0; i < houseItems.size(); i++)
    {
      item = new Item(houseItems.get(i));
      System.out.println("item name is: " +houseItems.get(i)+ "; item ID is: " + item.getID()+ "; item price is : " +item.getPrice());
    }
  }

  private void makeItems()
  {
    for (char i = 'A'; i < 'z'; i++)
    {
      final String item = Character.toString(i);
      itemL.add(item);
    }
  }

  private List<String> getNitems(int n)
  {
    makeItems();
    List<String> copy = new ArrayList<>(itemL);
    Collections.shuffle(copy);
    return copy.subList(0, n);
  }

}
