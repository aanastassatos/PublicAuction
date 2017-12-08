package Messages;

import java.io.Serializable;

public class AuctionHouseConnectionInfoMessage implements Serializable
{
  private final String address;
  private final int port;
  
  public AuctionHouseConnectionInfoMessage(final String address, final int port)
  {
    this.address = address;
    this.port = port;
  }
  
  public String getAddress()
  {
    return address;
  }
  
  public int getPort()
  {
    return port;
  }
}
