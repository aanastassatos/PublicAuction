package AuctionHouse;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuctionHouseCentral extends Thread
{
  private Socket socket;

  private ObjectInputStream central_ois;
  private ObjectOutputStream central_oos;

  private AuctionHouse auctionHouse;

  AuctionHouseCentral(String address, int port, String name, AuctionHouse auctionHouse) throws UnknownHostException, IOException
  {
    try
    {
      this.auctionHouse = auctionHouse;
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
      //if(o instanceof BlockFundsResultMessage) handleMessage((BlockFundsResultMessage)o);
      if(o instanceof AuctionHouseInfoMessage) handleMessage((AuctionHouseInfoMessage) o);
      else if(o instanceof RequestConnectionToAuctionHouseMessage) handleMessage((RequestConnectionToAuctionHouseMessage) o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  synchronized BlockFundsResultMessage sendBlockFundsMessage(final ModifyBlockedFundsMessage message)
  {
    try
    {
      central_oos.writeObject(message);
      Object o = central_ois.readObject();
      return (BlockFundsResultMessage) o;
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }

  synchronized void requestMoney(int biddingKey, int amount)
  {
    try
    {
      WithdrawFundsMessage message = new WithdrawFundsMessage(biddingKey,amount);
      central_oos.writeObject(message);
    }catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /*private void handleMessage(final BlockFundsResultMessage message)
  {

  }*/

  private void handleMessage(final AuctionHouseInfoMessage message)
  {
    auctionHouse.storeInfo(message);
  }

  private void handleMessage(final RequestConnectionToAuctionHouseMessage message)
  {
    try
    {
      central_oos.writeObject(auctionHouse.getConnectionInfo());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
