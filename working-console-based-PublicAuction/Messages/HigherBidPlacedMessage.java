package Messages;

import java.io.Serializable;

public class HigherBidPlacedMessage implements Serializable
{
  private String itemName;
  private int amount;

  public HigherBidPlacedMessage(String itemName, int amount)
  {
    this.itemName = itemName;
    this.amount = amount;
  }

  public String getItemName()
  {
    return itemName;
  }

  public int getAmount()
  {
    return amount;
  }
}
