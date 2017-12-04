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

  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private AuctionHouse auctionHouse;



  public AuctionHouseCentral(String address, int port, String name, AuctionHouse auctionHouse) throws UnknownHostException, IOException
  {
    try
    {
      this.auctionHouse = auctionHouse;
      socket = new Socket(address, port);

      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
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
    //this is to listen from central to return the result
    //how to send messages to central?
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
      else if(o instanceof HigherBidPlacedMessage) handleMessage((HigherBidPlacedMessage)o);
      else if(o instanceof RequestMoneySentMessage) handleMessage((RequestMoneySentMessage)o);
      else if(o instanceof HoldAccountResult) handleMessage((HoldAccountResult)o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void handleMessage(final PutHoldOnAccountMessage message)
  {
    try
    {
      oos.writeObject((auctionHouse.putHold(message.getBiddingKey(), message.getBidAmount())));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void handleMessage(final HigherBidPlacedMessage message)
  {
    try
    {
      oos.writeObject((auctionHouse.higherBidPlaced(message.getOldBiddingKey(),message.getnewBidAmount(),message.getNewBiddingKey())));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void handleMessage(final RequestMoneySentMessage message)
  {
    try
    {
      oos.writeObject((auctionHouse.requestMoney()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void handleMessage(final HoldAccountResult message)
  {
    // central sends message to auction house about the validity of the agent who placed the bid
    // returns true if amount money is valid and returns the public id of the agent

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

}
