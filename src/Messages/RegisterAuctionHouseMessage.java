package Messages;

import java.io.Serializable;

public class RegisterAuctionHouseMessage implements Serializable
{
  private final String name;

  public RegisterAuctionHouseMessage(final String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

}
