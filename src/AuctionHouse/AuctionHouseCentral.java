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
  private final int maxNumOfItems = 10;
  private int secretKey;
  private int publicID;
  private Random r = new Random();
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private AuctionHouse auctionHouse;

  public AuctionHouseCentral(String address, int port, String name) throws UnknownHostException, IOException
  {
    try
    {
      socket = new Socket(address, port);
      ois = new ObjectInputStream(socket.getInputStream());
      oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(new RegisterAuctionHouseMessage(name));
      Object o = ois.readObject();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {
    while(true)
    {
      Object o = null;
      try
      {
        o = ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }

      if(o instanceof PutHoldOnAccountMessage) handleMessage((PutHoldOnAccountMessage)o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
     // else if(o instanceof HigherBidPlaced) handleMessage((HigherBidPlaced)o);
      /*if(o instanceof HoldAccountResult) handleMessage((HoldAccountResult)o);
      else if(o instanceof PlaceHoldOnAccountMessage) handleMessage((PlaceHoldOnAccountMessage)o);
      */
    }
  }

  private void handleMessage(final PutHoldOnAccountMessage message)
  {
    try
    {
      oos.writeObject((auctionHouse.putHold(message.getPublicID(), message.getBidAmount())));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
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

  int getSecretKey()
  {
    return secretKey;
  }

  int getPublicID()
  {
    return publicID;
  }

  /*private void handleMessage(final HigherBidPlaced message)
  {
    try
    {
      //get the ID of the old agent, the ID of the new agent, the
      oos.writeObject((auctionHouse.higherBidPlaced(message.getOldPublicID(), message.getBidAmount(),message.getNewPublicID());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }*/

}
