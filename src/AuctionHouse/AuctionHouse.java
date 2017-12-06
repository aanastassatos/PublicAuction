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
    new Thread(() -> checkItem()).start();
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

  private void checkItem()
  {
    while(true)
    {
      HashMap<Integer, Item> itemList = new HashMap<Integer, Item>(items.getCurrentHouseItems());
      Queue<Item> itemsToRemove = new LinkedList<Item>();
      items.getCurrentHouseItems().keySet().stream()
              .map(itemList::get)
              .filter(Objects::nonNull)
              .filter(i -> i.isTimeUp())
              .forEach(itemsToRemove::add);

      itemsToRemove.forEach(current ->
              bidSucceeded(current.getItem(), current.getID(), current.getHighestBid(), current.getHighestBidderKey()));
      itemsToRemove.forEach(it -> items.removeItem(it.getID()));

//      while (iter.hasNext())
//      {
//        Map.Entry pair = (Map.Entry) iter.next();
//        Item current = itemList.get(pair.getKey());
//        if (current.isTimeUp())
//        {
//
//          bidSucceeded(current.getItem(), current.getID(), current.getHighestBid(), current.getHighestBidderKey());
//        }
//      }
    }
  }

  //CENTRAL
  AuctionHouseConnectionInfoMessage getConnectionInfo()
  {
    return new AuctionHouseConnectionInfoMessage(address, PORT);
  }
  
  // AGENTS
  void bidSucceeded(String itemName, int itemID, int amount, int biddingKey)
  {
    central.requestMoney(biddingKey, amount);

    //If there are more things to sell, update the list, else close
    if(!items.noMoreItemToSell()) items.updateItemList();
    else
    {
      sendMessageToClients(new NoItemLeftMessage());
      central.closeConnection();
    }
    sendMessageToClients(new ItemSoldMessage(itemID, itemName, amount));
    System.out.println("HOORAY. New item just arrived and added to the list" + new ItemListMessage(items.getCurrentHouseItems(), publicID));
    System.out.println("Congratulations! Bidding key number: " +biddingKey+ "has won "+items.getCurrentHouseItems().get(itemID)+
                        "with bidding amount of: " +amount);
    auctionHouseClients.get(biddingKey).sendMessage(new SuccessfulBidMessage(itemID, amount, biddingKey));
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

  synchronized ItemListMessage registerAgent(AgentInfoMessage message, AuctionHouseClient auctionHouseClient)
  {
    auctionHouseClients.put(message.getBiddingKey(), auctionHouseClient);
    return new ItemListMessage(items.getCurrentHouseItems(), publicID);
  }

  synchronized BidResultMessage placeBid(int itemID, int biddingKey, int amount)
  {
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
// GUI
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