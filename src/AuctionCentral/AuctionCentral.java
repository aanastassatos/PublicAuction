package AuctionCentral;

import Messages.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class AuctionCentral extends Thread
{
  public static Random rand = new Random();
  public final static int PORT = 7777;
  private static BankConnection bankConnection;
  
  //This creates an AuctionCentral and instantiates it with a port.
  public static void main(String[] args)
  {
//    System.out.println("Enter port number: ");
    try
    {
      AuctionCentral auctionCentral = new AuctionCentral(PORT);
      auctionCentral.start();
      String hostName = "localhost";
      bankConnection = new BankConnection(hostName, auctionCentral);
      bankConnection.start();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private ServerSocket auctionCentralSocket;
  
  public AuctionCentral(int port) throws IOException
  {
    auctionCentralSocket = new ServerSocket(port);
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
  
  synchronized AuctionHouseInfoMessage registerAuctionHouse(final String name, final AuctionClient auctionHouse)
  {
    int publicID = name.hashCode();
    int secretKey = rand.nextInt();
    AuctionHouseInfoMessage auctionHouseInfo = new AuctionHouseInfoMessage(publicID, secretKey);
    auctionHouseNames.put(publicID, name);
    auctionHouseKeys.put(publicID, auctionHouseInfo.getSecretKey());
    auctionHouseClients.put(auctionHouseKeys.get(publicID), auctionHouse);
    System.out.println("Auction house "+name+" has registered under the public publicID "+publicID);
    return auctionHouseInfo;
  }
  
  synchronized DeregisterAuctionHouseResultMessage deRegisterAuctionHouse(final int publicID, final int secretKey)
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
  
  synchronized AgentInfoMessage registerAgent(final String name, final int bankKey, final AuctionClient agent)
  {
    int biddingKey = name.hashCode()*rand.nextInt(8);
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentBankKeys.put(biddingKey, bankKey);
    agentClients.put(biddingKey, agent);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return agentInfo;
  }
  
  synchronized AuctionHouseListMessage getAuctionHouseList()
  {
    return new AuctionHouseListMessage(auctionHouseNames);
  }
  
  synchronized AuctionHouseConnectionInfoMessage connectClientToAuctionHouse(final int auctionHouseID)
  {
    return new AuctionHouseConnectionInfoMessage(auctionHouseClients.get(auctionHouseKeys.get(auctionHouseID)).getSocket());
  }
  
  synchronized void modifyBidderFunds(final BlockBidderFunds msg)
  {
    bankConnection.modifyBlockedFunds(new ModifyBlockedFundsMessage(agentBankKeys.get(msg.getBidderID()), msg.getAmount(), msg.getType()));
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
