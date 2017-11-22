package AuctionHouse;

import java.util.Random;

class Item
{
  private String item;
  private long itemID;
  private int itemPrice;
  private final int maxVal = 5000;
  private final int minVal = 10;
  private static int idInc = 1;
  private Random r = new Random();

  Item(String item)
  {
    this.item = item;
    setID();
    setPrice();
  }

  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
  }

  int getPrice()
  {
    return itemPrice;
  }

  private void setID()
  {
    itemID = idInc;
    idInc *= 11;
  }

  long getID()
  {
    return itemID;
  }
}
