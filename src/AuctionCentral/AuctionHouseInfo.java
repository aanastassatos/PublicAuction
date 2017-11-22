package AuctionCentral;

import java.io.Serializable;
import java.util.UUID;

public class AuctionHouseInfo implements Serializable
{
  private final String name;
  private final int publicID;
  private final UUID secretKey = UUID.randomUUID();
  
  AuctionHouseInfo(final String name, final int publicID)
  {
    this.name = name;
    this.publicID = publicID;
  }
  
  public String getName()
  {
    return name;
  }
  
  public int getPublicID()
  {
    return publicID;
  }
  
  public UUID getSecretKey()
  {
    return secretKey;
  }
}
