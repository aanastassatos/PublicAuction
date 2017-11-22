package Messages;

import java.io.Serializable;

public class DeregisterAuctionHouseMessage implements Serializable
{
  private final int publicID;
  private final int secretKey;
  
  public DeregisterAuctionHouseMessage(final int publicID, final int secretKey)
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
