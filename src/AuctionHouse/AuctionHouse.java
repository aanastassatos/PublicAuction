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
import java.util.UUID;

public class AuctionHouse extends Thread
{
  final static Random rand = new Random();
  private final static int PORT = rand.nextInt((60000-50000)+1)+50000;
  private static String address;
  
  public HouseItems houseItems;
  private final int maxNumOfItems = 10;
  private final int minNumOfItems = 3;


  private int secretKey;
  private int publicID;

  private ServerSocket auctionHouseSocket;

  private AuctionHouseCentral central;

  //The clients have the auctionHouseID and the auction client
  private final HashMap<Integer,AuctionHouseClient> auctionHouseClients = new HashMap<>();

  //private Time timer;

  public static void main(String[] args)
  {
    try
    {
      String centralAddress = "localhost";
      address = "localhost";
      
      AuctionHouse auctionHouse = new AuctionHouse(centralAddress, AuctionCentral.PORT, "AA", PORT);
      auctionHouse.start();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private HouseItems items;
  
  AuctionHouse(String centralAddress, int centralPort, String name,int port) throws IOException
  {
    int randomNumItems = rand.nextInt((maxNumOfItems - minNumOfItems) + 1) + minNumOfItems;
    auctionHouseSocket = new ServerSocket(port);
    this.central = new AuctionHouseCentral(centralAddress, centralPort, name, this);
    items = new HouseItems(randomNumItems);
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

  //CENTRAL
  synchronized RequestMoneySentMessage requestMoney(int auctionHouseID, int agentID, int amount)
  {
    //request money to Central
    return new RequestMoneySentMessage(auctionHouseID, agentID, amount);
  }
  
  // AGENTS
//  synchronized HigherBidPlacedMessage higherBidPlaced(int oldBiddingKey, int newBidAmount, int newBiddingKey)
//  {
//    /* inform the old agent that there is a higher bid placed
//       call bid placed and send to central
//     */
//    return new HigherBidPlacedMessage(newBidAmount);
//  }
//
//  synchronized ItemNoLongerAvailableMessage itemSold(boolean isInvalid)
//  {
//    // if item is invalid
//    // inform the agent
//    return new ItemNoLongerAvailableMessage(isInvalid);
//  }
//
//  synchronized BidReceivedMessage recievedBid(int publicID)
//  {
//    //inform the agent that the message is received
//    return new BidReceivedMessage();
//  }
//
//  synchronized InvalidBidMessage invalidBid(int biddingKey, int bidAmount, int auctionHousePublicID, int ItemID)
//  {
//    // After getting the result from Central,
//    // if result is invalid, send this message to agent
//    return new InvalidBidMessage();
//  }

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
  
  synchronized ItemListMessage registerAgent(AgentInfoMessage message, AuctionHouseClient auctionHouseClient)
  {
    auctionHouseClients.put(message.getBiddingKey(), auctionHouseClient);
    return new ItemListMessage(items.getAuctionHouseItemList(), publicID);
  }

  synchronized BidResultMessage placeBid(int itemID, int biddingKey, int amount)
  {
    Item item = items.getAuctionHouseItemList().get(itemID);
    if(item == null) return new BidResultMessage(BidResultMessage.BidResult.NOT_IN_STOCK);
    else if(item.getHighestBid() > amount) return new BidResultMessage(BidResultMessage.BidResult.BID_IS_TOO_LOW);
    else
    {
      BlockFundsResultMessage msg = central.sendBlockFundsMessage(new ModifyBlockedFundsMessage(biddingKey, amount, ModifyBlockedFundsMessage.TransactionType.Add, UUID.randomUUID()));
      if(msg != null)
      {
        if(msg.getResult()) return new BidResultMessage(BidResultMessage.BidResult.SUCCESS);
        else return new BidResultMessage(BidResultMessage.BidResult.INSUFICIENT_FUNDS);
      }
    }
    
    return null;
  }
  
  public void printInfo()
  {
    try
    {
      System.out.println("Server Ip: " + InetAddress.getLocalHost());
      System.out.println("Server host name: " + InetAddress.getLocalHost().getHostName());
    }
    catch (UnknownHostException e)
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
  
  String getAddress()
  {
    return address;
  }
  
  int getPort()
  {
    return PORT;
  }

  public void storeInfo(AuctionHouseInfoMessage message)
  {
    publicID = message.getPublicID();
    secretKey = message.getSecretKey();
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