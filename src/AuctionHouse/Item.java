package AuctionHouse;

import java.util.Random;

public class Item
{
  private String item;
  private int itemID;
  private int itemPrice;
  private int highestBidderKey;
  private int highestBid = 0;

  private final int maxVal = 100;
  private final int minVal = 1;
  private static Random r = AuctionHouse.rand;

  Item(String item)
  {
    this.item = item;
    setPrice();
  }

  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
  }

  public int getPrice()
  {
    return itemPrice;
  }

  void setID(int itemID)
  {
    this.itemID = itemID;
  }

  public int getID()
  {
    return itemID;
  }

  public String getItem()
  {
    return item;
  }
  
//  int getHighestBidderKey()
//  {
//    return highestBidderKey;
//  }
//
//  void setHighestBidderKey(int highestBidderKey)
//  {
//    this.highestBidderKey = highestBidderKey;
//  }
//
//  int getHighestBid()
//  {
//    return highestBid;
//  }
//
//  void setHighestBid(int highestBid)
//  {
//    this.highestBid = highestBid;
//  }

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
