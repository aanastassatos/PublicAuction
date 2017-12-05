package AuctionHouse;

import AuctionCentral.AuctionCentral;
import Messages.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class AuctionHouse extends Thread
{
  private final int BIDDING_TIME = 30;
  private final int maxNumOfItems = 10;
  private final int minNumOfItems = 3;

  final static Random rand = new Random();
  private final static int PORT = rand.nextInt((60000-50000)+1)+50000;
  private static String address;

  private int secretKey;
  private int publicID;

  private ServerSocket auctionHouseSocket;

  private AuctionHouseCentral central;

  //The clients have the auctionHouseID and the auction client
  private final HashMap<Integer,AuctionHouseClient> auctionHouseClients = new HashMap<>();

  private Timer timer = null;
  private int biddingTimeLeft = BIDDING_TIME;

  HouseItems houseItems;

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
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private void startTimer()
  {
    this.timer.scheduleAtFixedRate(new TimerTask()
    {
      @Override
      public void run()
      {
        tick();
        if(biddingTimeLeft == 0) bidSucceeded();
      }
    }, 0, 1000);
  }

  private void tick()
  {
    this.biddingTimeLeft--;
  }

  //CENTRAL
  synchronized RequestMoneySentMessage requestMoney(int auctionHouseID, int agentID, int amount)
  {
    //request money to Central
    return new RequestMoneySentMessage(auctionHouseID, agentID, amount);
  }

  AuctionHouseConnectionInfoMessage getConnectionInfo()
  {
    return new AuctionHouseConnectionInfoMessage(address, PORT);
  }

  // AGENTS
  synchronized SuccessfulBidMessage bidSucceeded()
  {
    //if the bid is over, reset the time
    biddingTimeLeft = BIDDING_TIME;

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
        if(msg.getResult())
        {
          startTimer();
          return new BidResultMessage(BidResultMessage.BidResult.SUCCESS);
        }
        else return new BidResultMessage(BidResultMessage.BidResult.INSUFICIENT_FUNDS);
      }
    }
    return null;
  }
  
  private void printInfo()
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

  void storeInfo(AuctionHouseInfoMessage message)
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