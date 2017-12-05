package Messages;

public class BidResultMessage
{
  public enum BidResult
  {
    NOT_IN_STOCK, BID_IS_TOO_LOW, SUCCESS, INSUFICIENT_FUNDS
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
