package Agent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AgentAuctionHouse
{

  AgentAuctionHouse(Socket auctionHouseSocket)
  {
    try
    {
      ObjectOutputStream oos = new ObjectOutputStream(auctionHouseSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream((auctionHouseSocket.getInputStream()));


    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }
}
