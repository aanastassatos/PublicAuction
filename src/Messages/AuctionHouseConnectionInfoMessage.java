package Messages;

import java.net.Socket;

public class AuctionHouseConnectionInfoMessage
{
  private final Socket auctionHouseSocket;
  
  public AuctionHouseConnectionInfoMessage(final Socket auctionHouseSocket)
  {
    this.auctionHouseSocket = auctionHouseSocket;
  }
  
  public Socket getAuctionHouseSocket()
  {
    return auctionHouseSocket;
  }
}
