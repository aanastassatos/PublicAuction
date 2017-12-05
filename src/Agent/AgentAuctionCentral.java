package Agent;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class AgentAuctionCentral extends Thread
{
  private int biddingKey;
  final Agent agent;
  private String address;
  private static volatile Integer port;
  private static volatile Integer house;

  AgentAuctionCentral(String hostname, String name, int key, Agent agent)
  {
    this.agent = agent;
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
      oos.writeObject(getAuctionHouses(houses));
      AuctionHouseConnectionInfoMessage info = ((AuctionHouseConnectionInfoMessage)ois.readObject());
      this.address = info.getAddress();
      this.port = info.getPort();
      oos.writeObject(new CloseConnectionMessage());

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  String getAddress() { return address; }

  int getPort() {
    while(port == null)
    {

    }
    return port;
  }

  int getBiddingKey() { return biddingKey; }

  int getHouse() {
    while(house == null)
    {

    }
    return house;
  }

  RequestConnectionToAuctionHouseMessage getAuctionHouses(HashMap<Integer, String> houses)
  {
    Collection<Integer> houseList = houses.keySet();
    ArrayList<Integer> arrayList = new ArrayList<>(houseList);
    if(arrayList.size() != 0)
    {
      System.out.println("Auction houses!");
      for(int housesList: arrayList)
      {
        System.out.println(housesList);
      }
    }


    System.out.print("Enter the auction house: ");
    Scanner scanner = new Scanner(System.in);
    house = scanner.nextInt();
    return new RequestConnectionToAuctionHouseMessage(arrayList.get(house), biddingKey);
  }
}
