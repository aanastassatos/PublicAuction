package Messages;

import java.io.Serializable;

public class RequestMoneySentMessage implements Serializable
{
  private int auctionHouseSecretKey;
  private int agentID;
  private int amount;

  public RequestMoneySentMessage(int auctionHouseSecretKey,int agentID,int amount)
  {
    this.auctionHouseSecretKey = auctionHouseSecretKey;
    this.agentID = agentID;
    this.amount = amount;
  }

  public int getAgentID()
  {
    return agentID;
  }

  public int getAmount()
  {
    return amount;
  }

  public int getAuctionHouseSecretKey()
  {
    return auctionHouseSecretKey;
  }
}
