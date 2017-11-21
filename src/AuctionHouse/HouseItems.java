package AuctionHouse;

import sun.jvm.hotspot.runtime.Threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HouseItems extends Threads
{
  private ArrayList<String> itemL = new ArrayList();
  private List<String> houseItems = new ArrayList();
  private int itemPrice;
  private final int maxVal = 5000;

  void makeItems()
  {
    for (char i = 'A'; i < 'z'; i++)
    {
      final String item = Character.toString(i);
      itemL.add(item);
    }
  }

  List<String> getNitems(ArrayList<String> allItems, int n)
  {
    makeItems();
    List<String> copy = new ArrayList<>(allItems);
    Collections.shuffle(copy);
    return copy.subList(0, n);
  }

  List<String> getItemList(int numItems)
  {
    houseItems = getNitems(itemL, numItems);
    return houseItems;
  }

  void getItemPrice()
  {
    for(int i = 0; i<houseItems.size(); i++)
    {
      itemPrice = (int) Math.random() * maxVal;
    }
  }
}
