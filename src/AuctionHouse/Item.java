package AuctionHouse;

import java.util.Random;

class Item
{
  private String item;
  private int itemID;
  private int itemPrice;
  private int highestBidderKey;
  private int highestBid = 0;

  private final int maxVal = 5000;
  private final int minVal = 10;
  private static int idInc = 1;
  private Random r = new Random();

  Item(String item)
  {
    this.item = item;
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

  void setID(int itemID)
  {
    this.itemID = itemID;
    //itemID = idInc;
    //idInc *= 11;
  }

  int getID()
  {
    return itemID;
  }

  String getItem()
  {
    return item;
  }


  int getHighestBidderKey()
  {
    return highestBidderKey;
  }

  void setHighestBidderKey(int highestBidderKey)
  {
    this.highestBidderKey = highestBidderKey;
  }

  int getHighestBid()
  {
    return highestBid;
  }

  void setHighestBid(int highestBid)
  {
    this.highestBid = highestBid;
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
