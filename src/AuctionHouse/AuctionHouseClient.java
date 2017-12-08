package AuctionHouse;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionHouseClient extends Thread
{
  private final AuctionHouse auctionHouse;
  private final Socket socket;

  private ObjectInputStream agent_ois;
  private ObjectOutputStream agent_oos;

  AuctionHouseClient(final Socket socket, final AuctionHouse auctionHouse)
  {
    this.socket = socket;
    this.auctionHouse = auctionHouse;
    try
    {
      agent_oos = new ObjectOutputStream(socket.getOutputStream());
      agent_ois = new ObjectInputStream(socket.getInputStream());
      
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {
    while (true)
    {
      Object o = null;

      try
      {
        o = agent_ois.readObject();
      } catch (Exception e)
      {
         e.printStackTrace();
         return;
      }

      if(o instanceof AgentInfoMessage) handleMessage((AgentInfoMessage) o);
      else if(o instanceof RequestItemListMessage) handleMessage((RequestItemListMessage) o);
      else if(o instanceof BidPlacedMessage) handleMessage((BidPlacedMessage)o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else
      {
        throw new RuntimeException("Received unknown message");
      }
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: Object m
  //Method's return value : void
  //Description of what the method does.
  // - This sends any type of messages to the agent
  // ************************************************************************************
  void sendMessage(Object m)
  {
    try
    {
      agent_oos.writeObject(m);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - when the auction house is done, send message to agent to close connection
  // ************************************************************************************
  private void closeConnection()
  {
    try
    {
      agent_oos.writeObject(new CloseConnectionMessage());
      agent_ois.close();
      socket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: final AgentInfoMessage message
  //Method's return value : void
  //Description of what the method does.
  // - Register the agent and send the item list to agent
  // ************************************************************************************
  private void handleMessage(final AgentInfoMessage message)
  {
    auctionHouse.registerAgent(message.getBiddingKey(), this);
  }

  //*************************************************************************************
  //Each parameter's type and name: final BidPlacedMessage message
  //Method's return value : void
  //Description of what the method does.
  // - This handles BidPlacedMessage
  // ************************************************************************************
  private void handleMessage(final BidPlacedMessage message)
  {
    try
    {
      agent_oos.writeObject(auctionHouse.placeBid(message.getItemID(), message.getBiddingKey(), message.getBidAmount()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RequestItemListMessage message)
  {
    try
    {
      agent_oos.writeObject(auctionHouse.sendItemList());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}