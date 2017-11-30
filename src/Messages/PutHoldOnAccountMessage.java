package Messages;

public class PutHoldOnAccountMessage
{

  private final int publicID;
  private final int bidAmount;

  public PutHoldOnAccountMessage(final int publicID, final int bidAmount)
  {
    this.publicID = publicID;
    this.bidAmount = bidAmount;
  }

  public int getBidAmount()
  {
    return bidAmount;
  }

  public int getPublicID()
  {
    return publicID;
  }

}
