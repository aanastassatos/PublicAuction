package AuctionHouse;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionHouseClient extends Thread
{
  private final AuctionHouse auctionHouse;
  private final Socket socket;
  //private  Socket centralSocket;

  private HouseItems houseItems;

  private ObjectInputStream agent_ois;
  private ObjectOutputStream agent_oos;

  AuctionHouseClient(final Socket socket, final AuctionHouse auctionHouse)
  {
    this.socket = socket;
    this.auctionHouse = auctionHouse;
    this.houseItems = auctionHouse.houseItems;
    try
    {
      agent_oos = new ObjectOutputStream(socket.getOutputStream());
      agent_ois = new ObjectInputStream(socket.getInputStream());
      
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

      if(o instanceof AgentInfoMessage) handleMessage((AgentInfoMessage) o);
      if(o instanceof BidPlacedMessage) handleMessage((BidPlacedMessage)o);
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void handleMessage(final AgentInfoMessage message)
  {
    try
    {
      ItemListMessage msg = auctionHouse.registerAgent(message, this);
      agent_oos.writeObject(msg);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final BidPlacedMessage message)
  {
    try
    {
      agent_oos.writeObject(auctionHouse.placeBid(message.getItemID(), message.getBiddingKey(), message.getBidAmount()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
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


//In case 2 people bids at the same time with higher value than the current highest bid
//  private boolean highestBid(int bid, int itemID, int auctionHouseID)
//  {
//    currentBid = houseItems.getCurrentHighestBid(auctionHouseID,itemID);
//    while(currentBid < bid)
//    {
//      if(highestBid.compareAndSet(currentBid,bid)) return true;
//      currentBid = highestBid.get();
//    }
//    return false;
//  }


//    HashMap<Integer,Integer> itemNBid = new HashMap<>();
//    itemNBid.put(message.getItemID(),message.getBidAmount());
//    agentBiddingItem.put(message.getBiddingKey(),itemNBid);
//
//    if(!itemList.contains(message.getItemID()))
//    {
//      try
//      {
//        agent_oos.writeObject(auctionHouse.itemSold(false));
//      } catch (IOException e)
//      {
//        e.printStackTrace();
//      }
//    }
//    else
//    {
//      if (highestBid(message.getBidAmount(),message.getItemID(),message.getAuctionHouseID()))
//      {
//        //agent_oos.writeObject(auctionHouse.higherBidPlaced(message.getBidAmount(),newBiddingKey,));
//        currentBid = message.getBidAmount();
//        try
//        {
//          //MAKE A HASHMAP OF PUBLIC ID AND SECRET KEY bc the hold requires the secret key
//          central_oos.writeObject(new ModifyBlockedFundsMessage(message.getBiddingKey(),message.getBidAmount(), ModifyBlockedFundsMessage.TransactionType.Add, UUID.randomUUID()));
//        } catch (IOException e)
//        {
//          e.printStackTrace();
//        }
//      } else
//        try
//        {
//          //check
//          agent_oos.writeObject(auctionHouse.invalidBid(message.getBiddingKey(),message.getBidAmount(),message.getAuctionHouseID(),message.getItemID()));
//        } catch (IOException e)
//        {
//          e.printStackTrace();
//        }
//    }

 /*
  private List<Item> itemList;

  private int currentBid;

  private final AtomicInteger highestBid = new AtomicInteger();

  int getCurrentHighestBid(int auctionHouseID, int itemID)
  {
    // GO TO THE CURRENT AUCTION HOUSE AND LOOK UP THE ITEM ID TO RETURN THE CURRENT BID
    return currentBid;
  }*/