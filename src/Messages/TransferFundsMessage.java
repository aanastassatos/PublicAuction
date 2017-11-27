package Messages;

public class TransferFundsMessage
{
  private final int amount;
  private final int bankKey;
  
  public TransferFundsMessage(final int bankKey, final int amount)
  {
    this.amount = amount;
    this.bankKey = bankKey;
  }
  
  public int getAmount()
  {
    return amount;
  }
  
  public int getBankKey()
  {
    return bankKey;
  }
}
