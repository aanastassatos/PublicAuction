package Messages;

public class HigherBidPlacedMessage
{
  private final int oldBiddingKey;
  private final int newBidAmount;
  private final int newBiddingKey;

  public HigherBidPlacedMessage(final int oldBiddingKey, final int newBidAmount,final int newBiddingKey)
  {
    this.oldBiddingKey = oldBiddingKey;
    this.newBidAmount = newBidAmount;
    this.newBiddingKey = newBiddingKey;
  }

  public int getnewBidAmount()
  {
    return newBidAmount;
  }

  public int getOldBiddingKey()
  {
    return oldBiddingKey;
  }

  public int getNewBiddingKey()
  {
    return newBidAmount;
  }
}
