package Agent;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class AgentAuctionCentral extends Thread
{
  private int biddingKey;
  private Socket auctionHouseSocket;

  AgentAuctionCentral(String hostname, String name, int key)
  {
    new Thread(() -> connectToAuctionCentral(hostname, name, key)).start();
  }

  public void connectToAuctionCentral(String hostname, String name, int key)
  {
    try
    {
      final Socket s = new Socket(hostname, AuctionCentral.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

      oos.writeObject(new RegisterAgentMessage(name, key));
      AgentInfoMessage agentInfoMessage = ((AgentInfoMessage)ois.readObject());
      biddingKey = agentInfoMessage.getBiddingKey();

      oos.writeObject(new RequestAuctionHouseListMessage());
      AuctionHouseListMessage auctionHouses = ((AuctionHouseListMessage)ois.readObject());

      HashMap<Integer, String> houses = auctionHouses.getAuctionHouseList();
      Collection<Integer> houseList = houses.keySet();
      ArrayList<Integer> arrayList = new ArrayList<>(houseList);

      oos.writeObject(new RequestConnectionToAuctionHouseMessage(arrayList.get(0)));
      AuctionHouseConnectionInfoMessage info = ((AuctionHouseConnectionInfoMessage)ois.readObject());
      auctionHouseSocket = info.getAuctionHouseSocket();
      oos.writeObject(new CloseConnectionMessage());

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  Socket getAuctionHouseSocket() { return auctionHouseSocket; }

  int getBiddingKey() { return biddingKey; }
}
