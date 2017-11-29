package AuctionHouse;

import AuctionCentral.AuctionClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class AuctionHouse extends Thread
{
  public final static int PORT = 1113;

  private ServerSocket auctionHouseSocket;
  private AuctionHouseCentral central;
  private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();

  public static void main(String[] args)
  {
    try
    {
      AuctionHouse auctionHouse= new AuctionHouse(args[0], Integer.parseInt(args[1]), args[2], PORT);

      auctionHouse.start();
      String hostName = "localhost";
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public AuctionHouse(String centralAddress, int centralPort, String name, int port) throws IOException
  {
    auctionHouseSocket = new ServerSocket(port);
    central = new AuctionHouseCentral(centralAddress, centralPort, name);
    printInfo();
  }

  @Override
  public void run()
  {
    while(true)
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

  /*synchronized AgentInfoMessage registerAgent(final String name, final int bankKey, final AuctionClient agent)
  {
    int biddingKey = name.hashCode();
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentNames.put(biddingKey, name);
    agentBankKeys.put(biddingKey, bankKey);
    agentClients.put(biddingKey, agent);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return agentInfo;
  }*/

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
