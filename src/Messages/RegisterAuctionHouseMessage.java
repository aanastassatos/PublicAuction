package Messages;

import java.io.Serializable;

public class RegisterAuctionHouseMessage implements Serializable
{
  private final String name;
  private final String address;
  private final int port;

  public RegisterAuctionHouseMessage(final String name, final String address, final int port)
  {
    this.name = name;
    this.address = address;
    this.port = port;
  }
  
  public String getName()
  {
    return name;
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
