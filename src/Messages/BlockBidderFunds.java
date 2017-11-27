package Messages;

public class BlockBidderFunds
{
  
  private final int amount;
  private final int bidderID;
  private final ModifyBlockedFundsMessage.TransactionType type;
  
  public BlockBidderFunds(final int bidderID, final int amount, ModifyBlockedFundsMessage.TransactionType type)
  {
    this.amount = amount;
    this.bidderID = bidderID;
    this.type = type;
  }
  
  public int getAmount()
  {
    return amount;
  }
  
  public int getBidderID()
  {
    return bidderID;
  }
  
  public ModifyBlockedFundsMessage.TransactionType getType()
  {
    return type;
  }
}