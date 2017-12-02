package Messages;

import java.io.Serializable;

public class PutHoldOnAccountMessage implements Serializable
{

  private final int biddingKey;
  private final int bidAmount;

  public PutHoldOnAccountMessage(final int biddingKey, final int bidAmount)
  {
    this.biddingKey = biddingKey;
    this.bidAmount = bidAmount;
  }

  public int getBidAmount()
  {
    return bidAmount;
  }

  public int getBiddingKey()
  {
    return biddingKey;
  }

}
