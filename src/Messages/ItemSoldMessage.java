package Messages;

import java.io.Serializable;

public class ItemSoldMessage implements Serializable
{
  private int itemID;
  private String itemName;
  private int amount;

  public ItemSoldMessage(int itemID, String itemName, int amount)
  {
    this.itemName = itemName;
    this.itemID = itemID;
    this.amount = amount;
  }

  public int getItemID()
  {
    return itemID;
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
