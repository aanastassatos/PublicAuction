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
      central_oos.writeObject(new RegisterAuctionHouseMessage(name, auctionHouse.getAddress(), auctionHouse.getPort()));
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

      if(o instanceof AuctionHouseInfoMessage) handleMessage((AuctionHouseInfoMessage) o);
//      else if(o instanceof RequestConnectionToAuctionHouseMessage) handleMessage((RequestConnectionToAuctionHouseMessage) o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - Tell the central to close the connection because there is no more items to sell
  // ************************************************************************************
  synchronized void closeConnection()
  {
    try
    {
      central_oos.writeObject(new CloseConnectionMessage());
    }catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: final ModifyBlockedFundsMessage message
  //Method's return value : BlockFundsResultMessage
  //Description of what the method does.
  // - This sends the block fund message to Auction Central
  // ************************************************************************************
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

  //*************************************************************************************
  //Each parameter's type and name: int biddingKey, int amount
  //Method's return value : void
  //Description of what the method does.
  // - this send a message to central to request money after the item is sold
  // ************************************************************************************
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

  //*************************************************************************************
  //Each parameter's type and name: final AuctionHouseInfoMessage message
  //Method's return value : void
  //Description of what the method does.
  // - This gets the secret key and public ID from central and store them
  // ************************************************************************************
  private void handleMessage(final AuctionHouseInfoMessage message)
  {
    auctionHouse.storeInfo(message);
  }

//  //*************************************************************************************
//  //Each parameter's type and name: final RequestConnectionToAuctionHouseMessage message
//  //Method's return value : void
//  //Description of what the method does.
//  // - send the connection info to Auction Central
//  // ************************************************************************************
//  private void handleMessage(final RequestConnectionToAuctionHouseMessage message)
//  {
//    try
//    {
//      central_oos.writeObject(auctionHouse.getConnectionInfo());
//    } catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//  }
}
