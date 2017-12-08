package AuctionHouse;

import AuctionCentral.AuctionCentral;
import Messages.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

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
  private final static int PORT = rand.nextInt((60000 - 50000) + 1) + 50000;
  private static String address;

  private int secretKey;
  private int publicID;

  private ServerSocket auctionHouseSocket;

  private AuctionHouseCentral central;

  private final HashMap<Integer, AuctionHouseClient> auctionHouseClients = new HashMap<>();

  public static void main(String[] args)
  {
    try
    {
      String centralAddress = "localhost";
      address = "localhost";
      Random r = new Random();
      char c = (char) (r.nextInt(26) + 'A');
      String name = Character.toString(c) + Integer.toString(r.nextInt(1000) + 1);
      AuctionHouse auctionHouse = new AuctionHouse(centralAddress, AuctionCentral.PORT, name, PORT);
      auctionHouse.start();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private HouseItems items;

  private AuctionHouse(String centralAddress, int centralPort, String name, int port) throws IOException
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

  //************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - This loops through the current house item list and check if there is
  //      any item that time is up.
  // - If there is an time up item then remove the item from the list
  //     and inform the winner by calling bidSucceeded method
  // ***********************************************************************
  private void checkItem()
  {
    while (true)
    {
      HashMap<Integer, Item> itemList = new HashMap<Integer, Item>(items.getCurrentHouseItems());
      Queue<Item> itemsToRemove = new LinkedList<Item>();
      items.getCurrentHouseItems().keySet().stream()
              .map(itemList::get)
              .filter(Objects::nonNull)
              .filter(i -> i.isTimeUp())
              .forEach(itemsToRemove::add);

      itemsToRemove.forEach(it -> items.removeItem(it.getID()));
      itemsToRemove.forEach(current ->
              bidSucceeded(current.getItem(), current.getID(), current.getHighestBid(), current.getHighestBidderKey()));
    }
  }

  //CENTRAL
  //***************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - This send the auction house info with the address and PORT when called
  // **************************************************************************
  AuctionHouseConnectionInfoMessage getConnectionInfo()
  {
    return new AuctionHouseConnectionInfoMessage(address, PORT);
  }
  
  // AGENTS
  //************************************************************************
  //Each parameter's type and name: String itemName, int itemID, int amount, int biddingKey
  //Method's return value : void
  //Description of what the method does.
  // - When the time for an item is up, this method will request the money
  //     equals to the cost of the item from Auction Central
  // - There are 2 lists that holds an auction house items, 1 that only have 3 items
  //     that being auctioned and the other list holds the rest of the items
  //   + Checks if the rest of item list is empty, if is not empty, then update currentList
  //   + Check if there is no items left in both lists, send message to all clients and close
  //   + If there are only items left in currentList, do nothing
  // - Send a message to all clients to inform that the item has been sold with the amount.
  // - Send a message to the agent that won the bid
  // ***********************************************************************
  private void bidSucceeded(String itemName, int itemID, int amount, int biddingKey)
  {
    central.requestMoney(biddingKey, amount);

    if(!items.noMoreNewItem())
    {
      items.updateItemList();
      System.out.println("New list is: " + items.getCurrentHouseItems());
    }

    else if(items.allItemsAreSold())
    {
      Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Go home. You have no more item to sell.").showAndWait());
      System.out.println("Go home. You have no more item to sell.");
      sendMessageToClients(new NoItemLeftMessage());
      central.closeConnection();
    }
    System.out.println("Current list is: " + items.getCurrentHouseItems());
    sendMessageToClients(new ItemSoldMessage(itemID, itemName, amount));
    Item newAddedItem = items.getNewItem();
    System.out.println("From Auction House number " +publicID+". Bidding key number: " +biddingKey+ " has won " +itemName+
    " with bidding amount of: " +amount+"\n\nItem " +newAddedItem.getItem()+ " just arrived and added to the list. HURRAY!");
    Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "From Auction House number "
            + publicID + ". Bidding key number: " +biddingKey+ " has won "+itemName+
            " with bidding amount of: " +amount+"\n\nAlso, item " + newAddedItem.getItem()+ " just arrived and added to the list. HOORAY!!").showAndWait());

    auctionHouseClients.get(biddingKey).sendMessage(new SuccessfulBidMessage(itemID, amount, biddingKey));
  }

  //************************************************************************
  //Each parameter's type and name: Object m
  //Method's return value : void
  //Description of what the method does.
  // - A place holder to send message to clients
  // ***********************************************************************
  private synchronized void sendMessageToClients(Object m)
  {
    Iterator iter = auctionHouseClients.entrySet().iterator();
    while (iter.hasNext())
    {
      Map.Entry pair = (Map.Entry) iter.next();
      auctionHouseClients.get(pair.getKey()).sendMessage(m);
    }
  }

  //************************************************************************************************
  //Each parameter's type and name: AgentInfoMessage message, AuctionHouseClient auctionHouseClien
  //Method's return value : void
  //Description of what the method does.
  // - add the agent to the agent map with the bidding key
  // - send the item list message to the agent with list of items being auctioned
  // ***********************************************************************************************
  synchronized ItemListMessage registerAgent(AgentInfoMessage message, AuctionHouseClient auctionHouseClient)
  {
    auctionHouseClients.put(message.getBiddingKey(), auctionHouseClient);
    return new ItemListMessage(items.getCurrentHouseItems(), publicID);
  }

  //************************************************************************
  //Each parameter's type and name: int itemID, int biddingKey, int amount
  //Method's return value : BidResultMessage
  //Description of what the method does.
  // - This handle the bid that was placed by the agent.
  //     if the item isnt in the list anymore, send message inform that item is not in stock
  //     if the bid placed is lower than current highest bid, send message bid too low
  //     else send message to central to block a fund in the agent account
  //         wait for the result, if the blocked fund went through:
  //            reset highest bid and bidderKey,
  //            send message to clients with updated bid amount
  //            send message to agent to inform the bid is successfully placed
  //         if blocked fund failed, send message insufficient funds
  // ***********************************************************************
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

          Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, ("Bidding key number: " +biddingKey+ "has successfully bidded on item "+items.getCurrentHouseItems().get(itemID)+
                              "with the amount of: "+amount)).showAndWait());

          sendMessageToClients(new HigherBidPlacedMessage(itemID, amount));
          return new BidResultMessage(BidResultMessage.BidResult.SUCCESS);
        }
        else return new BidResultMessage(BidResultMessage.BidResult.INSUFFICIENT_FUNDS);
      }
    }
    return null;
  }

  //************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // -Print the server ip and host name
  // ***********************************************************************
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
  
  String getAddress()
  {
    return address;
  }
  
  int getPort()
  {
    return PORT;
  }

  //************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - Store the info of the auction house got from central when register
  // ***********************************************************************
  void storeInfo(AuctionHouseInfoMessage message)
  {
    publicID = message.getPublicID();
    secretKey = message.getSecretKey();
  }
}
