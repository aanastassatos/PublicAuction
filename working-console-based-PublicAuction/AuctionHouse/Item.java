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
  private UUID highestBidTransactionID;

  private static Random r = AuctionHouse.rand;

  transient private Timer timer;
  private int biddingTimeLeft = BIDDING_TIME;

  Item(String item)
  {
    timer = new Timer();
    this.item = item;
    setPrice();
  }

  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
    highestBid = itemPrice;
  }

  public Integer getPrice()
  {
    return itemPrice;
  }

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

  boolean isTimeUp()
  {
    if(biddingTimeLeft == 0) return true;
    return false;
  }

  private void tick()
  {
    if(biddingTimeLeft > 0)
    {
      this.biddingTimeLeft--;
      System.out.println("time left is: " + biddingTimeLeft);
    }
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
  
  int getHighestBidderKey()
  {
    return highestBidderKey;
  }

  void setHighestBidderKey(int highestBidderKey)
  {
    startTimer();
    this.highestBidderKey = highestBidderKey;
  }

  public int getHighestBid()
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
