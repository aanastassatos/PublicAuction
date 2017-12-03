package AuctionHouse;

import AuctionCentral.AuctionCentral;
import AuctionCentral.AuctionClient;
import Messages.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AuctionHouse extends Thread
{
  public final static int PORT = 50500;

  public HouseItems houseItems;
  private final int maxNumOfItems = 10;
  private final int minNumOfItems = 3;

  private ServerSocket auctionHouseSocket;
  private AuctionHouseCentral central;
  //private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();

  //auction house only knows about the agents' bidding key and publicID to send to central
  //private final HashMap<Integer, Integer> agentInfo = new HashMap<>();

  //private Time timer;

  public static void main(String[] args)
  {
    try
    {
      String centralAddress = "localhost";

      AuctionHouse auctionHouse = new AuctionHouse(centralAddress, AuctionCentral.PORT, "AA", PORT);
      //auctionHouse.start();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  AuctionHouse(String centralAddress, int centralPort, String name, int port) throws IOException
  {
    auctionHouseSocket = new ServerSocket(port);
    central = new AuctionHouseCentral(centralAddress, centralPort, name);
    printInfo();
  }

  @Override
  public void run()
  {
    while (true)
    {
      try
      {
        Socket socket = auctionHouseSocket.accept();
        AuctionHouseClient client = new AuctionHouseClient(socket, this);
        //map the client to the list of clients, get their public ID
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private void auctionHouseItems()
  {
    Random rand = new Random();
    int randomNumItems = rand.nextInt((maxNumOfItems - minNumOfItems) + 1) + minNumOfItems;
    houseItems = new HouseItems(randomNumItems);
    //how to return the list of items
  }

  //CENTRAL
  synchronized HoldAccountResult getHoldAccountResult(boolean isValid, int publicID)
  {
    //if the valid is true, do nothing
    //otherwise, send invalidBidMessage to Agent
    isValid = false;
    publicID = 0;
    return new HoldAccountResult(isValid,publicID);
  }

  synchronized RequestMoneySentMessage requestMoney()
  {
    //request money to Central

    return new RequestMoneySentMessage();
  }


  // AGENTS
  synchronized HigherBidPlacedMessage higherBidPlaced(int oldBiddingKey, int newBidAmount, int newBiddingKey)
  {
    /* inform the old agent that there is a higher bid placed
       call bid placed and send to central
     */
    return new HigherBidPlacedMessage(newBidAmount);
  }

  synchronized ItemNoLongerAvailableMessage itemSold(boolean isInvalid)
  {
    // if item is invalid
    // inform the agent
    return new ItemNoLongerAvailableMessage(isInvalid);
  }

  synchronized BidReceivedMessage recievedBid(int publicID)
  {
    //inform the agent that the message is received
    return new BidReceivedMessage();
  }

  synchronized InvalidBidMessage invalidBid(int publicID)
  {
    // After getting the result from Central,
    // if result is invalid, send this message to agent
    return new InvalidBidMessage();
  }

  synchronized SuccessfulBidMessage bidSucceeded()
  {
    //have to have time for each bid
    int time = 0;
    if(time >= 30)
    {
      time = 0; //reset timer
      //send the successful message to the current bidder
      return new SuccessfulBidMessage();
    }
    // AFTER 30' the highest bid remains is the winner
    // Inform the agent with given public ID (or bidding key)
    return new SuccessfulBidMessage();
  }
  public void printInfo()
  {
    try
    {
      System.out.println("Server Ip: " + InetAddress.getLocalHost());
      System.out.println("Server host name: " + InetAddress.getLocalHost().getHostName());
      auctionHouseItems();
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  public List<Item> getHouseItemList()
  {
    return houseItems.getItemList();
  }
}

/*central will take care of this, auction house only needs to send the message with the bidding key and the amount?
  synchronized PutHoldOnAccountMessage putHold(int biddingKey, int bidAmount)
  {
    return new PutHoldOnAccountMessage(agentInfo.get(biddingKey), bidAmount);
  }*/
