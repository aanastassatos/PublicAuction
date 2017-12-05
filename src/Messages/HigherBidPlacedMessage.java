package Messages;

import java.io.Serializable;

public class HigherBidPlacedMessage implements Serializable
{
  private int itemID;
  private int amount;

  public HigherBidPlacedMessage(int itemID, int amount)
  {
    this.itemID = itemID;
    this.amount = amount;
  }

  public int getItemID()
  {
    return itemID;
  }

  public int getAmount()
  {
    return amount;
  }
}
