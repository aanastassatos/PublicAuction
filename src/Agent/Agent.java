package Agent;

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
  
  void requestAuctionHouseList()
  {
    System.out.println("Requesting list of Auction Houses...");
    auctionCentral.sendMessage(new RequestAuctionHouseListMessage());
  }
  
  void storeBiddingInfo(int biddingKey)
  {
    System.out.println("Your bidding key is "+biddingKey+".");
    this.biddingKey = biddingKey;
  }
  
  void requestItemList()
  {
    auctionHouse.sendMessage(new RequestItemListMessage());
  }
  
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
  
//  int secretKey;
//  AgentAuctionHouse agentAuctionHouse;
//
//  AgentGUI agentGui;
//
//  public Agent()
//  {
//    new JFXPanel();
//    Platform.runLater(() -> agentGui = new AgentGUI(this));
//  }
//
//  String successMessage(BidResultMessage.BidResult bidResult)
//  {
//    switch(bidResult)
//    {
//      case BID_IS_TOO_LOW:
//        return "Bid is too low!";
//      case SUCCESS:
//        return "Bid Placed!";
//      case INSUFFICIENT_FUNDS:
//        return "Insufficient Funds in Your Account :(";
//      case NOT_IN_STOCK:
//        return "Item Not In Stock";
//      default:
//        return "Here Be Monsters";
//    }
//  }
//
//  public void activateBank(String hostname, String name, int deposit)
//  {
//    AgentBankAccount bankAccount = new AgentBankAccount(this);
//    new Thread(() -> bankAccount.connectToBank(hostname, name, deposit)).start();
//    secretKey = bankAccount.getSecretKey();
//    AgentAuctionCentral auctionCentral = new AgentAuctionCentral(hostname, name, secretKey, this);
//    HashMap<Integer, String> map = auctionCentral.getHouses();
//    agentGui.showAuctionHouses(auctionCentral);
//  }
//
//  public void activateHouse(AgentAuctionCentral auctionCentral)
//  {
//    int biddingKey = auctionCentral.getBiddingKey();
//    int port = auctionCentral.getPort();
//    String address = auctionCentral.getAddress();
//    System.out.println("Address = " + address + "port is: " + port);
//
//    agentAuctionHouse = new AgentAuctionHouse(biddingKey, address, port, this);
//    agentGui.openAuctionHouse();
//  }
//
//  AgentAuctionHouse getAgentAuctionHouse()
//  {
//    return agentAuctionHouse;
//  }
//
//  public static void main(String[] args) //throws Exception
//  {
//    Agent agent = new Agent();
//    agent.start();
//  }
//
////  void setItems(HashMap<Integer, Item> items)
////  {
////    this.auctionHouseItems = items;
////    //printItems();
////  }
////
////  void printItems()
////  {
////    for(Item item : auctionHouseItems.values())
////    {
////      System.out.println("Item Name: " + item.getItem() + " Item ID: " + item.getID() + " Price: " + item.getPrice());
////    }
////  }
////
////  Integer getItemToBidOn() { return itemToBidOn; }
////
////  int getAmountBid() { return amountToBid; }
//
//  @Override
//  public void run()
//  {
//    while(agentGui == null)
//    {
//
//    }
//    while(true)
//    {
//      if(agentGui.isFinished())
//      {
//        activateBank(agentGui.getHostname(), agentGui.getName(), agentGui.getDepositAmount());
//        agentGui.setFinished(false);
//      }
////      if(auctionHouseItems != null)
////      {
////        System.out.print("Which item to bid on?: ");
////        Scanner scanner = new Scanner(System.in);
////        itemToBidOn = scanner.nextInt();
////        System.out.print("How much?: ");
////        amountToBid = scanner.nextInt();
////      }
//    }
//  }
}
