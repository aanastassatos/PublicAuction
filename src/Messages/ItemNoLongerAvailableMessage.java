package Messages;

public class ItemNoLongerAvailableMessage
{
  private final boolean isInvalid;

  public ItemNoLongerAvailableMessage(boolean result)
  {
    this.isInvalid = result;
  }

  public boolean isInvalid()
  {
    return isInvalid;
  }
}
