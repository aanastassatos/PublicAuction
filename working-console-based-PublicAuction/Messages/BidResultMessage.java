package Messages;

import java.io.Serializable;

public class BidResultMessage implements Serializable
{
  public enum BidResult
  {
    NOT_IN_STOCK, BID_IS_TOO_LOW, SUCCESS, INSUFFICIENT_FUNDS
  }
  
  private BidResult result;
  
  public BidResultMessage(BidResult result)
  {
    this.result = result;
  }
  
  public BidResult getResult()
  {
    return result;
  }
}
