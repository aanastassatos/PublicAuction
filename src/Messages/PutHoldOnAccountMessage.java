package Messages;

import java.io.Serializable;

public class PutHoldOnAccountMessage implements Serializable
{

  private final int biddingKey;
  private final int bidAmount;
  private final int auctionHouseSecretKey;

  public PutHoldOnAccountMessage(final int biddingKey, final int bidAmount, final int auctionHouseSecretKey)
  {
    this.biddingKey = biddingKey;
    this.bidAmount = bidAmount;
    this.auctionHouseSecretKey = auctionHouseSecretKey;
  }

  public int getBidAmount()
  {
    return bidAmount;
  }

  public int getBiddingKey()
  {
    return biddingKey;
  }

  public int getAuctionHouseSecretKey()
  {
    return auctionHouseSecretKey;
  }
}
