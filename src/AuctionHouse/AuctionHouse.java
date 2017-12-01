package AuctionHouse;

import AuctionCentral.AuctionCentral;
import AuctionCentral.AuctionClient;
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
  public final static int PORT = 50500;

  public HouseItems houseItems;
  private final int maxNumOfItems = 10;
  private final int minNumOfItems = 3;

  private ServerSocket auctionHouseSocket;
  private AuctionHouseCentral central;
  private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();

  //auction house only knows about the agents' bidding key and publicID to send to central
  private final HashMap<Integer, Integer> agentInfo = new HashMap<>();


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
  }

  //CENTRAL
  synchronized PutHoldOnAccountMessage putHold(int biddingKey, int bidAmount)
  {
    return new PutHoldOnAccountMessage(agentInfo.get(biddingKey), bidAmount);
  }

  synchronized HigherBidPlacedMessage higherBidPlaced(int oldBiddingKey, int newBidAmount, int newBiddingKey)
  {
    /* put hold on account of new id
       release the hold of the old public id
     */
    return new HigherBidPlacedMessage(agentInfo.get(oldBiddingKey), newBidAmount, agentInfo.get(newBiddingKey));
  }

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
  synchronized ItemNoLongerAvailableMessage invalidItem(boolean isInvalid)
  {
    // if item is invalid
    // inform the agent
    return new ItemNoLongerAvailableMessage(isInvalid);
  }

  synchronized BidReceivedMessage recievedBid()
  {
    //inform the agent that the message is received
    return new BidReceivedMessage();
  }

  synchronized InvalidBidMessage invalidBid()
  {
    // After getting the result from Central,
    // if result is invalid, send this message to agent
    return new InvalidBidMessage();
  }

  synchronized SuccessfulBidMessage bidSucceeded()
  {
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


}
