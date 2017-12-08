package Messages;

import java.io.Serializable;

public class ItemSoldMessage implements Serializable
{
  private int itemID;
  private String itemName;
  private int amount;

  public ItemSoldMessage(int itemID, String itemName, int amount)
  {
    this.itemID = itemID;
    this.amount = amount;
    this.itemName = itemName;
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
