package Agent;

import AuctionHouse.Item;
import Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class AgentAuctionHouse extends Thread
{
  final Agent agent;

  AgentAuctionHouse(int biddingKey, String address, int port, Agent agent)
  {
    this.agent = agent;
    new Thread(() -> connectToHouse(biddingKey, address, port)).start();
  }

  public void connectToHouse(int biddingKey, String address, int port)
  {
    System.out.println("address: " + address + "port: " + port);

    try
    {
      System.out.println("Here be monsters");
      final Socket houseSocket = new Socket(address, port);
      ObjectOutputStream oos = new ObjectOutputStream(houseSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(houseSocket.getInputStream());
      System.out.println("Or here?");
      oos.writeObject(new AgentInfoMessage(biddingKey));
      ItemListMessage itemsMessage = ((ItemListMessage) ois.readObject());
      HashMap<Integer, Item> items = itemsMessage.getItemList();
      System.out.println("Size is = : " + items.values().size());
      agent.setItems(items);


      oos.writeObject(new CloseConnectionMessage());
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }
}
