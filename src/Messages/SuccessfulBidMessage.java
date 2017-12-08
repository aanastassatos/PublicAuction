package Messages;

import java.io.Serializable;

public class SuccessfulBidMessage implements Serializable
{
  private String itemName;
  private int amount;
  private int biddingKey;

  public SuccessfulBidMessage(String itemName, int amount, int biddingKey)
  {
    this.itemName = itemName;
    this.amount = amount;
    this.biddingKey = biddingKey;
  }

  public String getItemName()
  {
    return itemName;
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
