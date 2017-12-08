package Agent;
/**
 * Agent Package:
 * Agent class: Runs agent thread for a perspective auction house
 * agent.  Creates a bank account, connects to auction central to
 * display available houses, and connects to a house to begin an
 * auction
 */
import AuctionHouse.Item;
import Bank.Bank;
import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Agent extends Thread
{
  private static BufferedReader reader;
  private String name;
  private int accountNumber = 0;
  private int bankKey = 0;
  private int biddingKey;
  private boolean bidding = false;

  /**
   * main method prompts user for bank hostname, auction central hostname,
   * their name, and their initial deposit .
   * Launches a new agent object with the provided information
   * @param args
   */
  public static void main(String [] args)
  {
    System.out.print("Enter the bank address (use localhost if server is on same computer as this client): ");
    try{
      reader = new BufferedReader(new InputStreamReader(System.in));
      String bank_address = reader.readLine();
      System.out.print("Enter the Auction Central address (use localhost if server is on same computer as this client): ");
      String auction_central_address = reader.readLine();
      System.out.print("Enter your name: ");
      String name = reader.readLine();
      System.out.print("Enter an initial deposit: ");
      int initDeposit = Integer.parseInt(reader.readLine());
      new Agent(name, auction_central_address, bank_address, initDeposit);
    }
    catch(NumberFormatException e)
    {
      System.out.println("You must enter a proper port number!");
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
    }
  }
  
  private AuctionCentralConnection auctionCentral;
  private AgentAuctionHouse auctionHouse;

  /**
   * Constructor builds a new Agent
   * @param name: users name
   * @param auction_central: hostname of auction central
   * @param bank_address: hostname of bank
   * @param initDeposit: agents deposit amount
   *                   creates agents bank account with specified
   *                   money, and creates a new auction central thread
   */
  public Agent(String name, String auction_central, String bank_address, int initDeposit)
  {
    this.name = name;
    makeBankAccount(bank_address, initDeposit);
    
    if(bankKey != 0)
    {
      auctionCentral = new AuctionCentralConnection(auction_central, name, bankKey, this);
      auctionCentral.start();
    } else {
      System.out.println("Something went wrong");
    }
  }

  /**
   * selectAuctionHouse method attempts to connect agent with desired auction house
   * @param auctionHouses: hashmap of all available auction houses
   * @return a new message to request connection to the specified auction house
   */
  RequestConnectionToAuctionHouseMessage selectAuctionHouse(HashMap<Integer, String> auctionHouses)
  {
    System.out.println("List Received!");
    List<String> nameList = new ArrayList<>(auctionHouses.values());
    List<Integer> IDList = new ArrayList<>(auctionHouses.keySet());
    int selection = 0;
    
    System.out.println("Enter the number next to the Auction House you want to connect to:");
    
    do
    {
      for (int i = 0; i < nameList.size(); i++)
      {
        System.out.println((i+1)+". "+nameList.get(i));
      }
      
      System.out.print("Enter selection: ");
      
      try
      {
        selection = Integer.parseInt(reader.readLine());
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      
      if(selection <= 0 || selection > nameList.size())
      {
       System.out.println("Invalid Selection. Please type something that makes sense.");
      }
    } while(selection <= 0 || selection > nameList.size());
    
    System.out.println("Connecting to "+nameList.get(selection-1)+"...");
    return new RequestConnectionToAuctionHouseMessage(IDList.get(selection-1), biddingKey);
  }

  /**
   * bidOnItems method returns a bidPlacedMessage
   * @param items: hashmap of current items at selected auction house
   * @return: message to place a bid on an item
   */
  synchronized BidPlacedMessage bidOnItems(HashMap<Integer, Item> items)
  {
    List<Item> itemList = new ArrayList<>(items.values());
    List<Integer> itemCodes = new ArrayList<>(items.keySet());
    
    int selection = 0;
    int bidAmount = 0;
    
    System.out.println("Select an item to bid on: ");
    for(int i = 0; i < items.size(); i++)
    {
      System.out.println((i+1)+". "+itemList.get(i).getItem()+" Current highest bid: $"+itemList.get(i).getHighestBid());
    }
    
    do
    {
      System.out.print("Enter the number next to the item you wish to bid on: ");
      try
      {
        selection = Integer.parseInt(reader.readLine());
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    } while(selection <= 0 || selection > items.size());
    
    do
    {
      System.out.print("Enter an amount to bid: ");
      try
      {
        bidAmount = Integer.parseInt(reader.readLine());
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }while(bidAmount <= 0);
  
    new Thread(() -> selectOption()).start();
    return new BidPlacedMessage(biddingKey, itemCodes.get(selection-1), bidAmount);
  }

  /**
   * connectToAuctionHouse method starts a new auction house thread based on the selected house
   * @param address: address of house to be connected to
   * @param port: port of house to be connected to
   */
  void connectToAuctionHouse(String address, int port)
  {
    System.out.print("Connecting to Auction House...");
    try
    {
      auctionHouse = new AgentAuctionHouse(address, port, biddingKey, this);
      auctionHouse.start();
      bidding = true;
      System.out.println("Connection to Auction House Successful!");
    }catch (IOException e)
    {
      System.out.println("Connection to Auction House Failed");
      e.printStackTrace();
    }
    
    new Thread(() -> selectOption()).start();
  }

  /**
   * requestAuctionHouseList method asks the current auction central
   * for a list of available auction houses
   */
  void requestAuctionHouseList()
  {
    System.out.println("Requesting list of Auction Houses...");
    auctionCentral.sendMessage(new RequestAuctionHouseListMessage());
  }

  /**
   * storeBiddingInfo method stores the agents current bidding key
   * @param biddingKey: the bidding key to be stored
   */
  void storeBiddingInfo(int biddingKey)
  {
    System.out.println("Your bidding key is "+biddingKey+".");
    this.biddingKey = biddingKey;
  }

  /**
   * requests an item list from the connected auction house
   */
  void requestItemList()
  {
    auctionHouse.sendMessage(new RequestItemListMessage());
  }

  /**
   * handleBidResult prints the result of a bid made
   * @param result: type BidResult enum from BidResultMessage class
   *              Lets the agent know the status of their bid
   */
  void handleBidResult(BidResultMessage.BidResult result)
  {
    String resultString;
    
    switch(result)
    {
      case BID_IS_TOO_LOW:
        resultString = "Bid is too low!";
        break;
      case SUCCESS:
        resultString = "Bid Placed!";
        break;
      case INSUFFICIENT_FUNDS:
        resultString = "Insufficient Funds in Your Account :(";
        break;
      case NOT_IN_STOCK:
        resultString = "Item Not In Stock";
        break;
      default:
        resultString = "Here Be Monsters";
        break;
    }
    
    System.out.println(resultString);
  }

  /**
   * When an agent enters an auction house, selectOption method
   * prompts them as to whether theyd like to leave the auction house
   * or bid on an item
   */
  private void selectOption()
  {
    System.out.print("What do you want to do?\n" +
        "1. Bid on an item\n" +
        "2. Exit Auction House\n");
  
    String selection = null;
    
    try
    {
      selection = reader.readLine();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  
    switch (selection)
    {
      case "1":
        requestItemList();
        break;
        
      case "2":
        bidding = false;
        System.out.println("Disconnecting from Auction House...");
        auctionHouse.closeConnection();
        auctionCentral.sendMessage(new CloseConnectionMessage());
        break;
        
      default:
        System.out.println("Invalid selection");
        break;
    }
  }

  /**
   * Takes the agents hostname and initial deposit to create a linked bank
   * account
   * @param bankAddress: hostname of bank
   * @param initDeposit: int deposit of agents funds
   */
  private void makeBankAccount(String bankAddress, int initDeposit)
  {
    System.out.println("Connecting to bank...");
    try
    {
      Socket socket = new Socket(bankAddress, Bank.PORT);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      
      System.out.println("Connection Successful!!!\n" +
          "Making a bank account for "+name);
      oos.writeObject(new CreateBankAccountMessage(name, initDeposit));
      
      Object o = ois.readObject();
      BankAccountInfoMessage info = (BankAccountInfoMessage) o;
      accountNumber = info.getAccountNumber();
      bankKey = info.getSecretKey();
      
      System.out.println("Account #: "+accountNumber);
      System.out.println("Bank key: "+bankKey);
      System.out.println("Disconnecting from Bank...");
      oos.writeObject(new CloseConnectionMessage());
      ois.close();
      oos.close();
      socket.close();
    } catch (Exception e)
    {
      System.out.println("Connection failed");
      e.printStackTrace();
    }
  }
}
