package AuctionCentral;

import Messages.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;

public class AuctionCentral extends Thread
{
  public static Random rand = new Random();
  public final static int PORT = 7777;
  
  //This creates an AuctionCentral and instantiates it with a port.
  public static void main(String[] args)
  {
//    System.out.println("Enter port number: ");
    try
    {
      new AuctionCentral(PORT).start();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private ServerSocket auctionCentralSocket;
  private final BankConnection bankConnection;
  
  public AuctionCentral(int port) throws IOException
  {
    String hostName = "localhost";
    auctionCentralSocket = new ServerSocket(port);
    bankConnection = new BankConnection(hostName, this);
    bankConnection.start();
    printInfo();
  }
  
  private final HashMap<Integer, String> auctionHouseNames = new HashMap<>();
  private final HashMap<Integer, Integer> auctionHouseKeys = new HashMap<>();
  private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();
  private final HashMap<Integer, String> agentNames = new HashMap<>();
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
      } catch (IOException e)
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
    int biddingKey = name.hashCode();
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentNames.put(biddingKey, name);
    agentBankKeys.put(biddingKey, bankKey);
    agentClients.put(biddingKey, agent);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return agentInfo;
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
