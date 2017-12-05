package Messages;

import java.io.Serializable;

public class SuccessfulBidMessage implements Serializable
{
  private int itemID;
  private int amount;
  private int biddingKey;

  public SuccessfulBidMessage(int itemID, int amount, int biddingKey)
  {
    this.itemID = itemID;
    this.amount = amount;
    this.biddingKey = biddingKey;
  }

  public int getItemID()
  {
    return itemID;
  }

  public int getAmount()
  {
    return amount;
  }

  public int getBiddingKey()
  {
    return biddingKey;
  }
}
