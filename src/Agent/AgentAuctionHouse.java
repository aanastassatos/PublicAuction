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
  HashMap<Integer, Item> items;

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
      final Socket houseSocket = new Socket(address, port);
      ObjectOutputStream oos = new ObjectOutputStream(houseSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(houseSocket.getInputStream());
      oos.writeObject(new AgentInfoMessage(biddingKey));
      ItemListMessage itemsMessage = ((ItemListMessage) ois.readObject());
      items = itemsMessage.getItemList();
      //System.out.println("Size is = : " + items.values().size());
      agent.setItems(items);

      if(agent.getItemToBidOn() != null)
      {
        oos.writeObject(new BidPlacedMessage(biddingKey, agent.getItemToBidOn(), agent.getAmountBid()));
      }
      
      while(true)
      {
        Object readMessage = ois.readObject();
        if (readMessage instanceof ItemListMessage)
        {
          ItemListMessage listMessage = (ItemListMessage) readMessage;
          items = listMessage.getItemList();
          agent.setItems(items);
        } else if (readMessage instanceof SuccessfulBidMessage) handleMessage((SuccessfulBidMessage) readMessage);
        else if (readMessage instanceof BidResultMessage) handleMessage((BidResultMessage) readMessage);
        else if(readMessage instanceof SuccessfulBidMessage) System.out.println("poop");
        else if(readMessage instanceof NoItemLeftMessage)
        {
          oos.writeObject(new CloseConnectionMessage());
          break;
        }
        
        
      }
    }

    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  void handleMessage(SuccessfulBidMessage message)
  {

  }

  void handleMessage(BidResultMessage message)
  {

  }
}
