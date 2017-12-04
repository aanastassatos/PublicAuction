package Agent;

import AuctionHouse.Item;
import Messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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

      oos.writeObject(new AgentInfoMessage(biddingKey));
      AuctionHouseItemsListMessage itemsMessage = ((AuctionHouseItemsListMessage)ois.readObject());
      ArrayList<Item> items = itemsMessage.getItems();
      agent.setItems(items);

      //oos.writeObject(new PlaceBidOnItemMessage());

      oos.writeObject(new CloseConnectionMessage());
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }
}
