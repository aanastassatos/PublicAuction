package Messages;

public class HigherBidPlacedMessage
{
  private final int newBidAmount;

  public HigherBidPlacedMessage(final int newBidAmount)
  {
    this.newBidAmount = newBidAmount;
  }

  public int getNewBidAmount()
  {
    return newBidAmount;
  }
}
