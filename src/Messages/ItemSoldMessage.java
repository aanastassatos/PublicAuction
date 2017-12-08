package Messages;

import java.io.Serializable;

public class ItemSoldMessage implements Serializable
{
  private int itemID;
  private String itemName;
  private int amount;

  public ItemSoldMessage(String itemName, int amount)
  {
    this.amount = amount;
    this.itemName = itemName;
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
