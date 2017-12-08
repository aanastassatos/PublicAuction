package AuctionHouse;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Item implements Serializable
{
  private final int BIDDING_TIME = 30;
  private final int maxVal = 100;
  private final int minVal = 1;

  private String item;
  private int itemPrice;
  private int highestBidderKey = 0;
  private int highestBid;

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

  //*************************************************************************************
  //Each parameter's type and name:none
  //Method's return value : String
  //Description of what the method does.
  // - get the name of the item
  // ************************************************************************************
  public String getItem()
  {
    return item;
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : int
  //Description of what the method does.
  // - set and get the highest bid
  // ************************************************************************************
  public int getHighestBid()
  {
    return highestBid;
  }

  void setHighestBid(int highestBid)
  {
    this.highestBid = highestBid;
    biddingTimeLeft = BIDDING_TIME;
  }

  //**********************************************************
  //Each parameter's type and name: int highestBidderKey
  //Method's return value : void
  //Description of what the method does.
  // - Set and set the bidder key
  // *********************************************************
  void setHighestBidderKey(int highestBidderKey)
  {
    if(!timerRunning)
    {
      startTimer();
    }
    this.highestBidderKey = highestBidderKey;
  }

  int getHighestBidderKey()
  {
    return highestBidderKey;
  }

  //**********************************************************
  //Each parameter's type and name:
  //Method's return value : void
  //Description of what the method does.
  // - start the timer count down
  // *********************************************************
  private void startTimer()
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

  //**********************************************************
  //Each parameter's type and name:
  //Method's return value : void
  //Description of what the method does.
  // - check to see if the time is up
  // *********************************************************
  boolean isTimeUp()
  {
    if(biddingTimeLeft == 0) return true;
    return false;
  }

  //**********************************************************
  //Each parameter's type and name:
  //Method's return value : void
  //Description of what the method does.
  // - set the price
  // *********************************************************
  private void setPrice()
  {
    itemPrice = r.nextInt((maxVal - minVal) + 1) + minVal;
    highestBid = itemPrice;
  }

  //**********************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - decrement the time
  // *********************************************************
  private void tick()
  {
    if(biddingTimeLeft > 0)
    {
      this.biddingTimeLeft--;
      System.out.println("time left is: " + biddingTimeLeft);
    }
  }
}
