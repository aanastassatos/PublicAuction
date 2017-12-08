package AuctionHouse;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Item implements Serializable
{
  private final int BIDDING_TIME = 3;
  private final int maxVal = 100;
  private final int minVal = 1;

  private String item;
  private int itemID;
  private int itemPrice;
  private int highestBidderKey = 0;
  private int highestBid;

  private static Random r = AuctionHouse.rand;

  transient private Timer timer;
  private int biddingTimeLeft = BIDDING_TIME;

  Item(String item)
  {
    timer = new Timer();
    this.item = item;
    setPrice();
  }

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :  void
  //Description of what the method does.
  // - set and get methods for price
  // *******************************************************
  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
    highestBid = itemPrice;
  }

  public Integer getPrice()
  {
    return itemPrice;
  }

  //******************************************
  //Each parameter's type and name: none
  //Method's return value :  void
  //Description of what the method does.
  // - start the timer
  // *****************************************
  void startTimer()
  {
    this.timer.scheduleAtFixedRate(new TimerTask()
    {
      @Override
      public void run()
      {
        tick();
      }
    }, 0, 1000);
  }

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :  boolean
  //Description of what the method does.
  // - return true if the time is up and false otherwise
  // *******************************************************
  boolean isTimeUp()
  {
    if(biddingTimeLeft == 0) return true;
    return false;
  }

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :  void
  //Description of what the method does.
  // - decrement the time
  // *******************************************************
  private void tick()
  {
    if(biddingTimeLeft > 0)
    {
      this.biddingTimeLeft--;
      System.out.println("time left is: " + biddingTimeLeft);
    }
  }

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :
  //Description of what the method does.
  // - set and get methods for id
  // *******************************************************
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

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :
  //Description of what the method does.
  // - set and get methods for bidder key
  // *******************************************************
  int getHighestBidderKey()
  {
    return highestBidderKey;
  }

  void setHighestBidderKey(int highestBidderKey)
  {
    startTimer();
    this.highestBidderKey = highestBidderKey;
  }

  //********************************************************
  //Each parameter's type and name: none
  //Method's return value :
  //Description of what the method does.
  // - set and get methods for highest bid
  // *******************************************************
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
