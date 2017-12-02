package Messages;

import java.io.Serializable;

public class HoldAccountResult implements Serializable
{
  private boolean isValid;
  private int publicID;
  public HoldAccountResult(boolean isValid,int publicID)
  {
    isValid = isValid;
    publicID = publicID;
  }

  public int getPublicID()
  {
    return publicID;
  }

  public boolean isValid()
  {
    return isValid;
  }
}
