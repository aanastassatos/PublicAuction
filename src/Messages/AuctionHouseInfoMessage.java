package Messages;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class AuctionHouseInfoMessage implements Serializable
{
  private final int publicID;
  private final int secretKey;
  
  public AuctionHouseInfoMessage(final int publicID, final int secretKey)
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
