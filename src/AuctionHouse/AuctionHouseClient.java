package AuctionHouse;

import Messages.ItemNoLongerAvailableMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class AuctionHouseClient extends Thread
{
  private final AuctionHouse auctionHouse;
  private final Socket socket;

  private HouseItems houseItems;

  private Item biddedItem;
  private int currentBid;

  private ObjectInputStream ois;
  private ObjectOutputStream oos;

  AuctionHouseClient(final Socket socket, final AuctionHouse auctionHouse)
  {
      this.socket = socket;
      this.auctionHouse = auctionHouse;
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
    while (true)
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
      /*if (o instanceof ItemNoLongerAvailableMessage) handleMessage((ItemNoLongerAvailableMessage) o);
      else if(o instanceof InvalidBidMessage) handleMessage((InvalidBidMessage)o);
      else if(o instanceof BidPlacedMessage) handleMessage((BidPlacedMessage)o);
      else if(o instanceof ReceivedPlacedBidMessage) handleMessage((ReceivedPlacedBidMessage)o);
      else if(o instanceof SuccessfulBidMessage) handleMessage((SuccessfulBidMessage)o);
      else if(o instanceof CloseConnectionMessage)
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

  private void handleMessage(final ItemNoLongerAvailableMessage message)
  {
    try
    {
      oos.writeObject(new ItemNoLongerAvailableMessage(true));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}
