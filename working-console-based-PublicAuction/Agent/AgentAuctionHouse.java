package Agent;

import AuctionHouse.Item;
import Messages.*;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class AgentAuctionHouse extends Thread
{
  final Agent agent;
  private int biddingKey;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  AgentAuctionHouse(String address, int port, int biddingKey, Agent agent) throws IOException
  {
    this.agent = agent;
    this.biddingKey = biddingKey;
    final Socket houseSocket = new Socket(address, port);
    oos = new ObjectOutputStream(houseSocket.getOutputStream());
    ois = new ObjectInputStream(houseSocket.getInputStream());
    oos.writeObject(new AgentInfoMessage(biddingKey));
  }
  
  @Override
  public void run()
  {
    Object o;
    while(true)
    {
      o = null;
      try
      {
        o = ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      
      if(o instanceof ItemListMessage) handleMessage((ItemListMessage) o);
      else if(o instanceof BidResultMessage) handleMessage((BidResultMessage) o);
      else if(o instanceof ItemSoldMessage) handleMessage((ItemSoldMessage) o);
      else if(o instanceof HigherBidPlacedMessage) handleMessage((HigherBidPlacedMessage) o);
      else if(o instanceof SuccessfulBidMessage) handleMessage((SuccessfulBidMessage) o);
      else if(o instanceof NoItemLeftMessage) System.out.println("No items left");
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
    }
  }
  
  void sendMessage(Object o)
  {
    try
    {
      oos.writeObject(o);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  void closeConnection()
  {
    try
    {
      oos.writeObject(new CloseConnectionMessage());
      ois.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(ItemListMessage msg)
  {
    try
    {
      oos.writeObject(agent.bidOnItems(msg.getItemList()));
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(BidResultMessage msg)
  {
    agent.handleBidResult(msg.getResult());
  }
  
  private void handleMessage(ItemSoldMessage msg)
  {
    System.out.println(msg.getItemName()+" was sold for "+msg.getAmount());
  }
  
  private void handleMessage(HigherBidPlacedMessage msg)
  {
    System.out.println(msg.getItemName()+" has recieved a bid for $"+msg.getAmount());
  }
  
  private void handleMessage(SuccessfulBidMessage msg)
  {
    System.out.println("You won the "+msg.getItemName()+" for $"+msg.getAmount()+"!");
  }
  
//  void setItemToBid(Item item)
//  {
//    this.itemToBid = item.getID();
//  }
//
//  void setAmountToBid(int bidAmount)
//  {
//    this.amountToBid = bidAmount;
//  }
//
//  public void connectToHouse(String address, int port)
//  {
//
//  }
//
//  String getSuccess() {
//    return success;
//  }
//
//  void startAuction()
//  {
//    try
//    {
//      while (true)
//      {
//        if (itemToBid != null)
//        {
//          oos.writeObject(new BidPlacedMessage(biddingKey, itemToBid, amountToBid));
//          itemToBid = null;
//        }
//        Object readMessage = ois.readObject();
//        if (readMessage instanceof ItemListMessage)
//        {
//          ItemListMessage listMessage = (ItemListMessage) readMessage;
//          items = listMessage.getItemList();
//          //agent.setItems(items);
//        } else if (readMessage instanceof SuccessfulBidMessage) handleMessage((SuccessfulBidMessage) readMessage);
//        else if (readMessage instanceof BidResultMessage) handleMessage((BidResultMessage) readMessage);
//        else if (readMessage instanceof NoItemLeftMessage)
//        {
//          oos.writeObject(new CloseConnectionMessage());
//          break;
//        }
//      }
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
//
//  HashMap<Integer, Item> getItems()
//  {
//    while(items == null)
//    {
//
//    }
//    return items;
//  }
//
//  private void handleMessage(SuccessfulBidMessage message)
//  {
//    success = "You Won!";
//  }
//
//  private void handleMessage(BidResultMessage message)
//  {
//    String result;
//    BidResultMessage.BidResult bidResult = message.getResult();
//    switch(bidResult)
//    {
//      case BID_IS_TOO_LOW:
//        result = "Bid is too low!";
//        break;
//      case SUCCESS:
//        result = "Bid Placed!";
//        break;
//      case INSUFFICIENT_FUNDS:
//        result = "Insufficient Funds in Your Account :(";
//        break;
//      case NOT_IN_STOCK:
//        result = "Item Not In Stock";
//        break;
//      default:
//        result = "Here Be Monsters";
//        break;
//    }
//    Platform.runLater(() -> new AgentSuccessGUI(result));
//  }
}
