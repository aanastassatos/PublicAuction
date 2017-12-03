package AuctionHouse;

import Messages.*;

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

  public AuctionHouseCentral(String address, int port, String name) throws UnknownHostException, IOException
  {
    try
    {
      auctionHouse = new AuctionHouse(address,port,name,AuctionHouse.PORT);
      socket = new Socket(address, port);

      central_oos = new ObjectOutputStream(socket.getOutputStream());
      central_ois = new ObjectInputStream(socket.getInputStream());
      central_oos.writeObject(new RegisterAuctionHouseMessage(name));
      Object o = central_ois.readObject();
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
      if(o instanceof HoldAccountResult) handleMessage((HoldAccountResult)o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void handleMessage(final HoldAccountResult message)
  {
    // central sends message to auction house about the validity of the agent who placed the bid
    // returns true if amount money is valid and returns the public id of the agent
    try
    {
      if(message.isValid() == true)
      {
        agent_oos.writeObject(auctionHouse.recievedBid(message.getPublicID()));
      }
      else
      {
        agent_oos.writeObject(auctionHouse.invalidBid(message.getPublicID()));
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void handleMessage(final RequestMoneySentMessage message)
  {
    try
    {
      central_oos.writeObject((auctionHouse.requestMoney()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }


  int getSecretKey()
  {
    return secretKey;
  }

  int getPublicID()
  {
    return publicID;
  }

}
/*else if(o instanceof HigherBidPlacedMessage) handleMessage((HigherBidPlacedMessage)o);
      else if(o instanceof RequestMoneySentMessage) handleMessage((RequestMoneySentMessage)o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }*/