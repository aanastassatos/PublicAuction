package AuctionCentral;

import Messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class AuctionCentral extends Thread
{
  public static final boolean DEBUG = true;
  public static Random rand = new Random();
  public static final int PORT = 7777;
  public static BufferedReader reader;
  static String BANK_ADDRESS;
  
  //This creates an AuctionCentral and instantiates it with a port.
  public static void main(String[] args)
  {
    System.out.print("Enter the bank address (use localhost if server is on same computer as this client): ");
    try{
      reader = new BufferedReader(new InputStreamReader(System.in));
      String bank_address = reader.readLine();
      new AuctionCentral(bank_address).start();
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
  
  private ServerSocket auctionCentralSocket;
  
  public AuctionCentral(final String bank_address) throws IOException
  {
    BANK_ADDRESS = bank_address;
    auctionCentralSocket = new ServerSocket(PORT);
    printInfo();
  }
  
  private final HashMap<Integer, String> auctionHouseNames = new HashMap<>();
  private final HashMap<Integer, Integer> auctionHouseKeys = new HashMap<>();
  private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();
  private final HashMap<Integer, Integer> agentBankKeys = new HashMap<>();
  private final HashMap<Integer, AuctionClient> agentClients = new HashMap<>();
  
  @Override
  public void run()
  {
    
    while(true)
    {
      try
      {
        Socket socket = auctionCentralSocket.accept();
        AuctionClient client = new AuctionClient(socket, this);
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  AuctionHouseInfoMessage registerAuctionHouse(final String name, final AuctionClient auctionHouse)
  {
    System.out.println("bing");
    int publicID = name.hashCode();
    int secretKey = rand.nextInt();
    AuctionHouseInfoMessage auctionHouseInfo = new AuctionHouseInfoMessage(publicID, secretKey);
    auctionHouseNames.put(publicID, name);
    auctionHouseKeys.put(publicID, auctionHouseInfo.getSecretKey());
    auctionHouseClients.put(auctionHouseKeys.get(publicID), auctionHouse);
    System.out.println("Auction house "+name+" has registered under the public publicID "+publicID);
    return auctionHouseInfo;
  }
  
  DeregisterAuctionHouseResultMessage deRegisterAuctionHouse(final int publicID, final int secretKey)
  {
    boolean result = false;
    
    if(auctionHouseNames.get(publicID) != null)
    {
      String auctionHouse = auctionHouseNames.get(publicID);
      auctionHouseNames.remove(publicID);
      auctionHouseKeys.remove(publicID);
      agentClients.remove(secretKey);
      System.out.println("Auction house " + auctionHouse + " has deregistered from the public publicID " + publicID);
      result = true;
    }
    return new DeregisterAuctionHouseResultMessage(result);
  }
  
  AgentInfoMessage registerAgent(final String name, final int bankKey, final AuctionClient agent)
  {
    int biddingKey = name.hashCode()*rand.nextInt(8);
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentBankKeys.put(biddingKey, bankKey);
    agentClients.put(biddingKey, agent);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return agentInfo;
  }
  
  AuctionHouseListMessage getAuctionHouseList()
  {
    return new AuctionHouseListMessage(auctionHouseNames);
  }
  
  AuctionHouseConnectionInfoMessage connectClientToAuctionHouse(final int auctionHouseID)
  {
    return new AuctionHouseConnectionInfoMessage(auctionHouseClients.get(auctionHouseKeys.get(auctionHouseID)).getSocket());
  }
  
  ModifyBlockedFundsMessage modifyBlockedFunds(final ModifyBlockedFundsMessage msg)
  {
    return new ModifyBlockedFundsMessage(agentBankKeys.get(msg.getAccountNumber()), msg.getAmount(), msg.getType(), msg.getTransactionId());
  }
  
  WithdrawFundsMessage withdrawFunds(final WithdrawFundsMessage msg)
  {
    return new WithdrawFundsMessage(agentBankKeys.get(msg.getAccountNumber()), msg.getAmount());
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
}
