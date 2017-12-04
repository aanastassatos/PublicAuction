package AuctionHouse;

import Messages.*;
//import sun.jvm.hotspot.opto.Block;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class AuctionHouseCentral extends Thread
{
  private Socket socket;

  private int secretKey;
  private int publicID;

  private ObjectInputStream central_ois;
  private ObjectOutputStream central_oos;
  private AuctionHouse auctionHouse;
  private ObjectInputStream agent_ois;
  private ObjectOutputStream agent_oos;
  
  public AuctionHouseCentral(String address, int port, String name, AuctionHouse auctionHouse) throws UnknownHostException, IOException
  {
    try
    {
      this.auctionHouse = auctionHouse;
      socket = new Socket(address, port);

      central_oos = new ObjectOutputStream(socket.getOutputStream());
      central_ois = new ObjectInputStream(socket.getInputStream());
      central_oos.writeObject(new RegisterAuctionHouseMessage(name));
      Object o = central_ois.readObject();

//      agentSocket = new Socket("localhost", AuctionHouse.PORT);
//      agent_oos = new ObjectOutputStream(agentSocket.getOutputStream());
//      agent_ois = new ObjectInputStream(agentSocket.getInputStream());
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {
    //this is to listen from central to return the result
    //how to send messages to central?
    while(true)
    {
      Object o = null;
      try
      {
        o = central_ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      if(o instanceof BlockFundsResultMessage) handleMessage((BlockFundsResultMessage)o);
      else if(o instanceof AuctionHouseInfoMessage) handleMessage((AgentInfoMessage) o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void handleMessage(final AuctionHouseInfoMessage message)
  {
    auctionHouse.storeInfo(message);
  }
  
  private void handleMessage(final BlockFundsResultMessage message)
  {
    // central sends message to auction house about the validity of the agent who placed the bid
    // returns true if amount money is valid and returns the public id of the agent
   /* try
    {
      if(message.getResult() == true)
      {

        agent_oos.writeObject(auctionHouse.recievedBid(()));
      }
      else
      {
        agent_oos.writeObject(auctionHouse.invalidBid(message.getResult)
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }*/
  }

  /*private void requestMoneySent(int agentI)
  {
    try
    {
      central_oos.writeObject((auctionHouse.requestMoney(this.getPublicID(), .getAgentID(),())));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }*/

  int getSecretKey()
  {
    return secretKey;
  }

  int getPublicID()
  {
    return publicID;
  }

}
//else if(o instanceof RequestMoneySentMessage) handleMessage((RequestMoneySentMessage)o);
