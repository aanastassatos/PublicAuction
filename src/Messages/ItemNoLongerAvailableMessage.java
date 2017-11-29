package Messages;

public class ItemNoLongerAvailableMessage
{
  private final boolean result;

  public ItemNoLongerAvailableMessage(boolean result)
  {
    this.result = result;
  }

  public boolean getResult()
  {
    return result;
  }
}
