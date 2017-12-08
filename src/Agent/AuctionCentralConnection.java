package Agent;

/**
 * Agent class:
 * AuctionCentralConnection class:
 * Connects an agent to auction central, and displays auction houses
 * available
 */


import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Agent package:
 * AuctionCentralConnection class: connects an agent to
 * the auction central, generates a bidding key
 */

public class AuctionCentralConnection extends Thread
{
  private final Agent agent;
  private Socket socket;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;

  /**
   * Constructor makes connection to auction central from agent
   * @param hostname: address
   * @param name: agents name
   * @param bankKey: agents bank key
   * @param agent: agent to connect
   */
  AuctionCentralConnection(String hostname, String name, int bankKey, final Agent agent)
  {
    this.agent = agent;
    System.out.println("Connecting to Auction Central...");
    try{
      socket = new Socket(hostname, AuctionCentral.AuctionCentral.PORT);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      
      System.out.println("Connection Successful!!!");
      System.out.println("Acquiring Your Bidding Key...");
      
      oos.writeObject(new RegisterAgentMessage(name, bankKey));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * run method handles different messages
   * Messages handled:
   * AgentInfoMessage
   * AuctionHouseListMessage
   * AuctionHouseConnectionInfoMessage
   * CloseConnectionMessage
   */
  @Override
  public void run()
  {
    Object o;
    while(true)
    {
      try
      {
        o = ois.readObject();
  
        if(o instanceof AgentInfoMessage) handleMessage((AgentInfoMessage) o);
        else if(o instanceof AuctionHouseListMessage) handleMessage((AuctionHouseListMessage) o);
        else if(o instanceof AuctionHouseConnectionInfoMessage) handleMessage((AuctionHouseConnectionInfoMessage) o);
        else if(o instanceof CloseConnectionMessage)
        {
          closeConnection();
          return;
        }
        else throw new RuntimeException("Received unknown message");
        
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * sendMessages writes valid messages to the client
   * @param o: message to be sent
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
   * handles AgentInfoMessages
   * stores agents bidding key and requests a list of items
   * @param msg
   */
  private void handleMessage(AgentInfoMessage msg)
  {
    agent.storeBiddingInfo(msg.getBiddingKey());
    agent.requestAuctionHouseList();
  }

  /**
   * handles AuctionHouseListMessages, to get current
   * houses in the auction central
   * @param msg
   */
  private void handleMessage(AuctionHouseListMessage msg)
  {
    try
    {
      oos.writeObject(agent.selectAuctionHouse(msg.getAuctionHouseList()));
    } catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * handles AuctionHouseConnectionInfoMessages to connect agent
   * to auction central, with the valid port and address
   * @param msg
   */
  private void handleMessage(AuctionHouseConnectionInfoMessage msg)
  {
    agent.connectToAuctionHouse(msg.getAddress(), msg.getPort());
  }

  /**
   * Closes the connection between agent and auction central
   */
  private void closeConnection()
  {
    try
    {
      ois.close();
      socket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
