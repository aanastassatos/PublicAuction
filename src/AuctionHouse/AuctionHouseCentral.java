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

  public AuctionHouseCentral(String address, int port, String name) throws UnknownHostException, IOException
  {
    try
    {
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

  //Register to Central
  //Send messages - Bid Placed
  //              - HigherBidPlaced
  //              - GetHoldOnAccount
  //              - WithdrawFromAccount

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
      /*if(o instanceof HoldAccountResult) handleMessage((HoldAccountResult)o);
      else if(o instanceof HigherBidPlaced) handleMessage((HigherBidPlaced)o);
      else if(o instanceof GetBid) handleMessage((GetBid)o);
      else if(o instanceof PlaceHoldOnAccountMessage) handleMessage((PlaceHoldOnAccountMessage)o);
      else if(o instanceof CloseAuctionHouseMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");*/
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
}
