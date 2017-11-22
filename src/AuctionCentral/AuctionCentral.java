package AuctionCentral;

import java.io.BufferedReader;
import AuctionHouse.AuctionHouse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AuctionCentral extends Thread
{
  public final static int PORT = 7777;
  
  //This creates an AuctionCentral and instantiates it with a port.
  public static void main(String[] args)
  {
    System.out.println("Enter port number: ");
    try
    {
      new AuctionCentral(PORT).start();
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
  
  private final HashMap<Integer, AuctionHouseInfo> auctionHouses = new HashMap<>();
  private final HashMap<Integer, AgentInfo> agents = new HashMap<>();
  
  @Override
  public void run()
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
  
  AuctionHouseInfo registerAuctionHouse(final String name)
  {
    int publicID = name.hashCode();
    AuctionHouseInfo auctionHouseInfo = new AuctionHouseInfo(name, publicID);
    auctionHouses.put(publicID, auctionHouseInfo);
    System.out.println("Auction house "+name+" has registered under the public publicID "+publicID);
    return auctionHouseInfo;
  }
  
  void deRegisterAuctionHouse(final int publicID, final UUID secretKey)
  {
    if(auctionHouses.get(publicID).getSecretKey() == secretKey)
    {
      AuctionHouseInfo auctionHouse = auctionHouses.get(publicID);
      auctionHouses.remove(auctionHouses.get(publicID));
      System.out.println("Auction house "+auctionHouse.getName()+" has deregistered from the public publicID "+publicID);
    }
  }
  
  int registerAgent(final String name, final int bankKey)
  {
    int biddingKey = name.hashCode();
    AgentInfo agentInfo = new AgentInfo(name, bankKey);
    agents.put(biddingKey, agentInfo);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return biddingKey;
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
