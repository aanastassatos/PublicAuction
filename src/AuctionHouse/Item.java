package AuctionHouse;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Item implements Serializable
{
  private final int BIDDING_TIME = 30;
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
  private boolean timerRunning = false;
  private int biddingTimeLeft = BIDDING_TIME;

  Item(String item)
  {
    this.item = item;
    setPrice();
    timer = new Timer();
  }

  public int getPrice()
  {
    return itemPrice;
  }
  
  public String getItem()
  {
    return item;
  }
  
  public int getHighestBid()
  {
    return highestBid;
  }
  
  void setHighestBidderKey(int highestBidderKey)
  {
    if(!timerRunning)
    {
      startTimer();
    }
    this.highestBidderKey = highestBidderKey;
  }
  
  void setHighestBid(int highestBid)
  {
    this.highestBid = highestBid;
    biddingTimeLeft = BIDDING_TIME;
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
  
  int getHighestBidderKey()
  {
    return highestBidderKey;
  }
  
  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
    highestBid = itemPrice;
  }
  
  private void tick()
  {
    if(biddingTimeLeft > 0)
    {
      this.biddingTimeLeft--;
      System.out.println("time left is: " + biddingTimeLeft);
    }
  }
}
