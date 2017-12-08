package Agent;

/**
 * Agent package:
 * AgentAuctionHouse class creates a new auction house socket to connect
 * to the agent, and simulates the bid
 */

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Agent package:
 * AgentAuctionHouse class: creates a new thread connected to the
 * auction house. Handles bidding and item information between the
 * auction house and the agent
 */

public class AgentAuctionHouse extends Thread
{
  final Agent agent;
  private int biddingKey;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  /**
   * Constructor
   * @param address
   * @param port
   * @param biddingKey
   * @param agent
   * @throws IOException
   */
  AgentAuctionHouse(String address, int port, int biddingKey, Agent agent) throws IOException
  {
    this.agent = agent;
    this.biddingKey = biddingKey;
    final Socket houseSocket = new Socket(address, port);
    oos = new ObjectOutputStream(houseSocket.getOutputStream());
    ois = new ObjectInputStream(houseSocket.getInputStream());
    oos.writeObject(new AgentInfoMessage(biddingKey));
  }

  /**
   * run method for when AgentAuctionHouse thread starts
   * continuously reads new messages and handles them according to
   * type
   * Messages Handled:
   * ItemListMessage
   * BidResultMessage
   * ItemSoldMessage
   * HigherBidPlacedMessage
   * SuccessfulBidMessage
   * NoItemLeftMessage
   * CloseConnectionMessage
   */
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

  /**
   * writes the input object to the socket stream
   * @param o: Message to be sent
   */
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

  /**
   * closes the connection to the socket
   */
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

  /**
   * handles an ItemListMessage writes a new item list
   * @param msg
   */
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

  /**
   * handles a bid result message, sending it to the auction house
   * @param msg
   */
  private void handleMessage(BidResultMessage msg)
  {
    agent.handleBidResult(msg.getResult());
  }

  /**
   * handles an item sold message, to alert the agent the item is sold
   * @param msg
   */
  private void handleMessage(ItemSoldMessage msg)
  {
    System.out.println(msg.getItemName()+" was sold for "+msg.getAmount());
  }

  /**
   * handles a Higher bid placed message, to alert the bidder of a higher bid
   * by another agent
   * @param msg
   */
  private void handleMessage(HigherBidPlacedMessage msg)
  {
    System.out.println(msg.getItemName()+" has recieved a bid for $"+msg.getAmount());
  }

  /**
   * handles a successful bid message, to alert the buyer they won the auction
   * @param msg
   */
  private void handleMessage(SuccessfulBidMessage msg)
  {
    System.out.println("You won the "+msg.getItemName()+" for $"+msg.getAmount()+"!");
  }
}
