package AuctionHouse;

import AuctionCentral.AuctionCentral;
import AuctionCentral.AuctionClient;
import Messages.AgentInfoMessage;
import Messages.PutHoldOnAccountMessage;

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

  //auction house only knows about the agents' bididng key and publicID to send to central
  private final HashMap<Integer, Integer> agentInfo = new HashMap<>();
  private int bidAmount;

  public static void main(String[] args)
  {
    try
    {
      String hostName = "localhost";
      String centralAddress = hostName;

      AuctionHouse auctionHouse= new AuctionHouse(centralAddress, AuctionCentral.PORT,"AA", PORT);
//      auctionHouse.start();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public AuctionHouse(String centralAddress, int centralPort, String name, int port) throws IOException
  {
    //auctionHouseSocket = new ServerSocket(port);
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

        //map the client to the list of clients, get their public ID
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  //send the message to central that an agent sent a bid
  synchronized PutHoldOnAccountMessage putHold(int publicID, int bidAmount)
  {
    return new PutHoldOnAccountMessage(agentInfo.get(publicID), bidAmount);
  }

  /*synchronized HigherBidPlacedMessage higherBidPlaced(int oldPublicID, int bidAmount, int newPublicID)
  {
    //put hold on account of new id
    //release the hold of the old public id
    for (HashMap<Integer,Integer> entry : map.entrySet()) {
      if (Objects.equals(value, entry.getValue())) {
        keys.add(entry.getKey());
      }
    }
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
