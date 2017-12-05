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
      Random r = new Random();
      char c = (char)(r.nextInt(26) + 'A');
      String name = Character.toString(c)+Integer.toString(r.nextInt(1000)+1);
      AuctionHouse auctionHouse = new AuctionHouse(centralAddress, AuctionCentral.PORT, name, PORT);
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

  //private void startTimer(int itemID, int bidAmount, int biddingKey)
  //if(biddingTimeLeft == 0) bidSucceeded(itemID, bidAmount, biddingKey);
  private void startTimer(Item item)
  {
    this.timer.scheduleAtFixedRate(new TimerTask()
    {
      @Override
      public void run()
      {
        tick();
        if(biddingTimeLeft == 0) bidSucceeded(item.getID(), item.getHighestBid(), item.getHighestBidderKey());
      }
    }, 0, 1000);
  }

  private void tick()
  {
    this.biddingTimeLeft--;
  }

  //CENTRAL
  AuctionHouseConnectionInfoMessage getConnectionInfo()
  {
    return new AuctionHouseConnectionInfoMessage(address, PORT);
  }
  
  // AGENTS
  synchronized SuccessfulBidMessage bidSucceeded(int itemID, int amount, int biddingKey)
  {
    //TRANSACTION ID???

    //Should have another list for this?
    // THE AUCTION HOUSE ONLY AUCTIONS AT MOST 3 ITEMS AT A TIME
    // ONCE IT'S REMOVE THE ITEM THEN IT CAN ADD NEW ONES

    central.requestMoney(biddingKey, amount);
    items.removeItem(itemID);

    //If there are more things to sell, update the list, else close
    if(!items.noMoreItemToSell()) items.updateItemList();
    else
    {
      sendMessageToClients(new NoItemLeftMessage());
      central.closeConnection();
    }

    System.out.println("HOORAY. New item just arrived and added to the list" + new ItemListMessage(items.getCurrentHouseItems(), publicID));
    System.out.println("Congratulations! Bidding key number: " +biddingKey+ "has won "+items.getCurrentHouseItems().get(itemID)+
                        "with bidding amount of: " +amount);
    return new SuccessfulBidMessage(itemID, amount, biddingKey);
  }

  public synchronized void sendMessageToClients(Object m)
  {
    List<Map.Entry> deadClients = new ArrayList<>();
    Iterator iter = auctionHouseClients.entrySet().iterator();
    while (iter.hasNext())
    {
      Map.Entry pair = (Map.Entry) iter.next();
      auctionHouseClients.get(pair.getKey()).sendMessage(m);
    }
    for(Map.Entry c: deadClients)
      auctionHouseClients.remove(c);
  }

  //HAVE TO UPDATE THE LIST OF ITEMS AND SEND TO THE AGENT WHEN AN ITEM IS SOLD AND A NEW ONE IS PLACED
  //HAVE TO PRINT OUT A NEW PRICE WHEN A HIGHER BID IS PLACED
  synchronized ItemListMessage registerAgent(AgentInfoMessage message, AuctionHouseClient auctionHouseClient)
  {
    auctionHouseClients.put(message.getBiddingKey(), auctionHouseClient);
    return new ItemListMessage(items.getCurrentHouseItems(), publicID);
    //return new ItemListMessage(items.getAuctionHouseItemList(), publicID);
  }

  synchronized BidResultMessage placeBid(int itemID, int biddingKey, int amount)
  {
    //Item item = items.getAuctionHouseItemList().get(itemID);
    Item item = items.getCurrentHouseItems().get(itemID);
    if(item == null) return new BidResultMessage(BidResultMessage.BidResult.NOT_IN_STOCK);
    else if(item.getHighestBid() > amount) return new BidResultMessage(BidResultMessage.BidResult.BID_IS_TOO_LOW);
    else
    {

      BlockFundsResultMessage msg = central.sendBlockFundsMessage(new ModifyBlockedFundsMessage(biddingKey, amount, ModifyBlockedFundsMessage.TransactionType.Add, UUID.randomUUID()));
      if(msg != null)
      {
        if(msg.getResult())
        {
          //set the highestBid to be the current bid and the bidding key to the highest bidder
          item.setHighestBid(amount);
          item.setHighestBidderKey(biddingKey);
          //if the new bid is placed by someone, reset the time
          biddingTimeLeft = BIDDING_TIME;
          //startTimer(itemID, amount, biddingKey);
          startTimer(item);
          System.out.println("Bidding key number: " +biddingKey+ "has bidded on item "+items.getCurrentHouseItems().get(itemID)+
                              "with the amount of: "+amount);
          sendMessageToClients(new HigherBidPlacedMessage(itemID, amount));
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
  
  String getAddress()
  {
    return address;
  }
  
  int getPort()
  {
    return PORT;
  }
  
  void storeInfo(AuctionHouseInfoMessage message)
  {
    publicID = message.getPublicID();
    secretKey = message.getSecretKey();
  }
}

// WHAT IS THE POINT OF SECRET KEY??? ALSO PUBLIC ID
// TIMER
// SUCCESSFUL BID
// MULTIPLE HOUSES
// 3 ITEMS AT A TIME
// UPDATE/REMOVE ITEMS
// PRINT LIST OF ITEM with the ITEMID(hashmap) and CURRENT BID FROM EACH AUCTION HOUSE
// IF ALL ITEMS ARE SOLD, CLOSE

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