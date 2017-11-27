package AuctionCentral;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionClient extends Thread
{
  private final AuctionCentral auctionCentral;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private final Socket socket;
  
  AuctionClient(final Socket socket, final AuctionCentral auctionCentral)
  {
    this.socket = socket;
    this.auctionCentral = auctionCentral;
    try
    {
      ois = new ObjectInputStream(socket.getInputStream());
      oos = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
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
      
      if(o instanceof RegisterAgentMessage) handleMessage((RegisterAgentMessage) o);
      else if(o instanceof RegisterAuctionHouseMessage) handleMessage((RegisterAuctionHouseMessage) o);
      else if(o instanceof DeregisterAuctionHouseMessage) handleMessage((DeregisterAuctionHouseMessage) o);
      else if(o instanceof RequestAuctionHouseListMessage) handleMessage((RequestAuctionHouseListMessage) o);
      else if(o instanceof RequestConnectionToAuctionHouseMessage) handleMessage((RequestConnectionToAuctionHouseMessage) o);
      else if(o instanceof BlockBidderFunds) auctionCentral.modifyBidderFunds((BlockBidderFunds) o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
    }
  }
  
  Socket getSocket()
  {
    return socket;
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
  
  private void handleMessage(final RegisterAgentMessage msg)
  {
    try
    {
      oos.writeObject(auctionCentral.registerAgent(msg.getName(), msg.getBankKey(), this));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RegisterAuctionHouseMessage msg)
  {
    try
    {
      oos.writeObject(auctionCentral.registerAuctionHouse(msg.getName(), this));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final DeregisterAuctionHouseMessage msg)
  {
    try
    {
      oos.writeObject(auctionCentral.deRegisterAuctionHouse(msg.getPublicID(), msg.getSecretKey()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RequestAuctionHouseListMessage msg)
  {
    try
    {
      oos.writeObject(auctionCentral.getAuctionHouseList());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RequestConnectionToAuctionHouseMessage msg)
  {
    try
    {
      oos.writeObject(auctionCentral.connectClientToAuctionHouse(msg.getAuctionHouseID()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
