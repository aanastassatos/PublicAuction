package Agent;

import AuctionHouse.Item;
import Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class AgentAuctionHouse
{
  final Agent agent;

  AgentAuctionHouse(int biddingKey, Socket auctionHouseSocket, Agent agent)
  {
    this.agent = agent;
    try
    {
      ObjectOutputStream oos = new ObjectOutputStream(auctionHouseSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream((auctionHouseSocket.getInputStream()));
      System.out.println("Here be monsters");
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
