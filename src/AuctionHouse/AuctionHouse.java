package AuctionHouse;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;

public class AuctionHouse extends Thread
{
  public final static int PORT = 1113;

  public HouseItems houseItems;
  private final int maxNumOfItems = 10;
  private final int minNumOfItems = 3;

  private ServerSocket auctionHouseSocket;

  private AuctionHouseCentral central;

  //The clients have the auctionHouseID and the auction client
  private final HashMap<Integer,AuctionHouseClient> auctionHouseClients = new HashMap<>();
  private HashMap<Integer,Integer> itemsNCurrBids;

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

  AuctionHouse(String centralAddress, int centralPort, String name,int port) throws IOException
  {
    //auctionHouseSocket = new ServerSocket(port);
    this.central = new AuctionHouseCentral(centralAddress, centralPort, name, this);
    printInfo();
  }

  @Override
  public void run()
  {
    while (true)
    {
      try
      {
        Socket socket = this.auctionHouseSocket.accept();
        AuctionHouseClient client = new AuctionHouseClient(socket, this);
        auctionHouseClients.put(central.getPublicID(),client);
        //map the client to the list of clients, get their public ID
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  //Create random number of items in each auction house
  //When initialize houseItems object, it will print the list of items
  private void auctionHouseItems(int auctionHouseID)
  {
    Random rand = new Random();
    int randomNumItems = rand.nextInt((maxNumOfItems - minNumOfItems) + 1) + minNumOfItems;
    this.houseItems = new HouseItems(randomNumItems,auctionHouseID);
    this.itemsNCurrBids = new HashMap<>(houseItems.getItemNCurrBid(auctionHouseID));
  }

  //CENTRAL
  synchronized RequestMoneySentMessage requestMoney(int auctionHouseID, int agentID, int amount)
  {
    //request money to Central
    return new RequestMoneySentMessage(auctionHouseID, agentID, amount);
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

  synchronized InvalidBidMessage invalidBid(int biddingKey, int bidAmount, int auctionHousePublicID, int ItemID)
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
      //remove the item from the auctionhouse item list
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
      auctionHouseItems(central.getPublicID());
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }
}

/*central will take care of this, auction house only needs to send the message with the bidding key and the amount?
  synchronized PutHoldOnAccountMessage putHold(int biddingKey, int bidAmount)
  {
    return new PutHoldOnAccountMessage(agentInfo.get(biddingKey), bidAmount);
  }*/
 /* AgentInfoMessage getAgentInfo(final int publicID, final int auctionHouseID, final AuctionHouseClient agent)
  {
    auctionHousePublicID = auctionHouseID;
    int biddingKey =  rand.nextInt(Integer.MAX_VALUE);
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentKeys.put(publicID, biddingKey);
    auctionHouseClients.put(agentKeys.get(publicID), agent);
    System.out.println("Auction Agent with the ID "+publicID+" has connect to auctionHouse ID" +auctionHouseID+" with a bidding key: "
            +biddingKey);
    return agentInfo;
  }*/