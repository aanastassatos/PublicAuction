package AuctionHouse;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class AuctionHouseClient extends Thread
{
  private final AuctionHouse auctionHouse;
  private final Socket socket;
  private  Socket centralSocket;

  private HouseItems houseItems;
  private List<Item> itemList = new ArrayList(houseItems.getItemList());

  private int currentBid;

  private ObjectInputStream agent_ois;
  private ObjectOutputStream agent_oos;
  private ObjectInputStream central_ois;
  private ObjectOutputStream central_oos;

  AuctionHouseClient(final Socket socket, final AuctionHouse auctionHouse)
  {
    this.socket = socket;
    this.auctionHouse = auctionHouse;
    try
    {
      agent_ois = new ObjectInputStream(socket.getInputStream());
      agent_oos = new ObjectOutputStream(socket.getOutputStream());

      //ASSIGN THE SOCKET TO CENTRAL HERE WHEN A CLIENT IS MADE
      centralSocket = new Socket("localhost", AuctionCentral.PORT);
      central_ois = new ObjectInputStream(centralSocket.getInputStream());
      central_oos = new ObjectOutputStream(centralSocket.getOutputStream());
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
        o = agent_ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      //THIS ONLY HEAR MESSAGES SENT FROM THE AGENT. AND HANDLE THE MESSAGES ACCORDINGLY
      if(o instanceof BidPlacedMessage) handleMessage((BidPlacedMessage)o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void handleMessage(final BidPlacedMessage message)
  {
    //FIND A WAY TO KEEP TRACK OF WHO'S CURRENTLY HOLDING THE HIGHEST BID

    //if the item is sold, send no longer available message to the agent
    if(!itemList.contains(message.getItem()))
    {
      try
      {
        agent_oos.writeObject(auctionHouse.itemSold(false));
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      if (message.getBidAmount() > currentBid)
      {
        // agent_oos.writeObject(auctionHouse.higherBidPlaced(message.getBidAmount(),newBiddingKey,));
        currentBid = message.getBidAmount();
        try
        {
          central_oos.writeObject(new PutHoldOnAccountMessage(message.getPublicID(), message.getBidAmount()));
        } catch (IOException e)
        {
          e.printStackTrace();
        }
      } else
        try
        {
          agent_oos.writeObject(auctionHouse.invalidBid(message.getPublicID()));
        } catch (IOException e)
        {
          e.printStackTrace();
        }
    }
  }

  int getCurrentBid()
  {
    return currentBid;
  }
}
  /*
  private void handleMessage(final SuccessfulBidMessage message)
  {
    try
    {
      agent_oos.writeObject(auctionHouse.bidSucceeded());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }*/

