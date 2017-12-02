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

  private int minimumBid;
  private int currentBid;

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

  String getItem()
  {
    return item;
  }

  int initBid()
  {
    minimumBid = r.nextInt(itemPrice/10);
    return minimumBid;
  }
  @Override
  public String toString()
  {
    return " " + getItem() + " ";
  }

  @Override
  public int hashCode()
  {
    return 31* String.valueOf(item).hashCode();
  }

  @Override
  public boolean equals(Object str)
  {
    if(this == str)
    {
      return true;
    }
    if(str instanceof Item)
    {
      Item anotherLetter = (Item) str;
      return (item == anotherLetter.item);
    }
    else
    {
      return false;
    }
  }
}
