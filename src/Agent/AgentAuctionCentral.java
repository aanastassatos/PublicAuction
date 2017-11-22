package Agent;

import AuctionCentral.AuctionCentral;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AgentAuctionCentral extends Thread
{

  public static void connectToAuctionCentral(String hostname)
  {
    try
    {
      final Socket s = new Socket(hostname, AuctionCentral.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

      //oos.writeObject(new Messagetype - to get auction list from central());

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
