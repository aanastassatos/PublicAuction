package Agent;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionCentralConnection extends Thread
{
  private final Agent agent;
  private Socket socket;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  
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
  
  private void handleMessage(AgentInfoMessage msg)
  {
    agent.storeBiddingInfo(msg.getBiddingKey());
    agent.requestAuctionHouseList();
  }
  
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
  
  private void handleMessage(AuctionHouseConnectionInfoMessage msg)
  {
    agent.connectToAuctionHouse(msg.getAddress(), msg.getPort());
  }
  
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
