package Messages;

import java.io.Serializable;

public class CloseConnectionMessage implements Serializable
{
  private int publicID;
  private int secretKey;

  public CloseConnectionMessage(int publicID, int secretKey)
  {
    this.publicID = publicID;
    this.secretKey = secretKey;
  }

  public int getPublicID()
  {
    return publicID;
  }

  public int getSecretKey()
  {
    return secretKey;
  }
}
