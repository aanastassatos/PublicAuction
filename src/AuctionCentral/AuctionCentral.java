/**
 * Created by Alex Anastassatos
 *
 * Creates the Auction Central and handles accepting clients, storing information,
 * and directing messages.
 */

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
  public static final int PORT = 55556; //Static port number for AuctionCentral

  static Random rand = new Random();
  static String BANK_ADDRESS;  //Stores the bank's address for later use
  
  private static BufferedReader reader;
  
  /**
   * This creates an AuctionCentral and instantiates it with the address needed to connect to the bank.
   */
  public static void main(String[] args)
  {
    System.out.print("Enter the bank address (use localhost if server is on same computer as this client): ");
    try{
      reader = new BufferedReader(new InputStreamReader(System.in));
      String bank_address;
      if(args.length > 0 && args[0].equals("test")) bank_address = "";
      else bank_address = reader.readLine();
      new AuctionCentral(bank_address).start();
      System.out.println("Connected to bank at " + (bank_address.equals("") ? "localhost" : bank_address));
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
  
  private ServerSocket auctionCentralSocket;  //Server port for AuctionCentral
  private AuctionCentralGui gui;
  
  /**
   * Instantiates AuctionCentral, stores the bank address for use in auction client and makes the server socket.
   * @param bank_address
   * @throws IOException
   */
  public AuctionCentral(final String bank_address) throws IOException
  {
    BANK_ADDRESS = bank_address;
//    gui = new AuctionCentralGui();
    auctionCentralSocket = new ServerSocket(PORT);
    printInfo();
  }
  
  private final HashMap<Integer, String> auctionHouseNames = new HashMap<>(); //Stores the auction house names with the public ID of the auction house
  private final HashMap<Integer, Integer> auctionHouseKeys = new HashMap<>(); //Stores the secret keys of the auction houses with the public ID
  private final HashMap<Integer, AuctionHouseConnectionInfo> auctionHouseConnections = new HashMap<>(); //Stores the connection info of the auction houses with the secret key.
  private final HashMap<Integer, AuctionClient> auctionHouseClients = new HashMap<>();  //Stores the clients of the auction house with the secret keys
  private final HashMap<Integer, String> agentNames = new HashMap<>();  //Stores the agent's names with the bidding key
  private final HashMap<Integer, Integer> agentBankKeys = new HashMap<>();  //Stores the Bank keys  with the bidding key
  private final HashMap<Integer, AuctionClient> agentClients = new HashMap<>(); //Stores the clients with the bidding key
  
  /**
   * Continuously loops, accepting client sockets and putting them in an AuctionClient
   */
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
  
  /**
   * Stores the name, auctionClient, address, and port number of auction houses for later use and returns an info message to be
   * sent to the auction house that just registered.
   * @param name
   * @param auctionHouse
   * @param address
   * @param port
   * @return
   */
  synchronized AuctionHouseInfoMessage registerAuctionHouse(final String name, final AuctionClient auctionHouse, final String address, final int port)
  {
    int publicID = name.hashCode();
    int secretKey = rand.nextInt(Integer.MAX_VALUE);
    AuctionHouseInfoMessage auctionHouseInfo = new AuctionHouseInfoMessage(publicID, secretKey);
    AuctionHouseConnectionInfo connectionInfo = new AuctionHouseConnectionInfo(address, port);
    auctionHouseNames.put(publicID, name);
    auctionHouseKeys.put(publicID, secretKey);
    auctionHouseClients.put(secretKey, auctionHouse);
    auctionHouseConnections.put(secretKey, connectionInfo);
    System.out.println("Auction house "+name+" has registered under the public publicID "+publicID);
    return auctionHouseInfo;
  }
  
  /**
   * Removes all information regarding to the auction house, and returns a result message to be sent to the
   * auction house.
   * @param publicID
   * @param secretKey
   * @return
   */
  synchronized DeregisterAuctionHouseResultMessage deRegisterAuctionHouse(final int publicID, final int secretKey)
  {
    boolean result = false;
    
    if(auctionHouseNames.get(publicID) != null)
    {
      String auctionHouse = auctionHouseNames.get(publicID);
      auctionHouseNames.remove(publicID);
      auctionHouseKeys.remove(publicID);
      auctionHouseClients.remove(secretKey);
      auctionHouseConnections.remove(secretKey);
      System.out.println("Auction house " + auctionHouse + " has deregistered from the public publicID " + publicID);
      result = true;
    }
    return new DeregisterAuctionHouseResultMessage(result);
  }
  
  /**
   * Stores the name, bankKey, and AuctionClient of the agent for later use and returns a message
   * containing the info to be sent to the agent.
   * @param name
   * @param bankKey
   * @param agent
   * @return
   */
  synchronized AgentInfoMessage registerAgent(final String name, final int bankKey, final AuctionClient agent)
  {
    int biddingKey = name.hashCode()*rand.nextInt();
    AgentInfoMessage agentInfo = new AgentInfoMessage(biddingKey);
    agentNames.put(biddingKey,name);
    agentBankKeys.put(biddingKey, bankKey);
    agentClients.put(biddingKey, agent);
    System.out.println("Agent "+name+" registered under the bidding key "+biddingKey+" and the bank key "+bankKey);
    return agentInfo;
  }
  
  /**
   * Returns a message containing a hashmap of auction house publicIDs and names.
   * @return
   */
  synchronized AuctionHouseListMessage getAuctionHouseList()
  {
    return new AuctionHouseListMessage(auctionHouseNames);
  }
  
  /**
   * Returns a message with the info required to connect to a requested auction house.
   * @param msg
   * @return
   */
  synchronized AuctionHouseConnectionInfoMessage connectClientToAuctionHouse(final RequestConnectionToAuctionHouseMessage msg)
  {
    AuctionHouseConnectionInfo connectionInfo = auctionHouseConnections.get(auctionHouseKeys.get(msg.getAuctionHouseID()));
    return new AuctionHouseConnectionInfoMessage(connectionInfo.getAddress(), connectionInfo.getPort());
  }
  
  /**
   * Changes the bidding key in the received "ModifyBlockedFundsMessage" and replaces it with its corresponding
   * bank key. Returns the changed message.
   * @param msg
   * @return
   */
  synchronized ModifyBlockedFundsMessage modifyBlockedFunds(final ModifyBlockedFundsMessage msg)
  {
    return new ModifyBlockedFundsMessage(agentBankKeys.get(msg.getAccountNumber()), msg.getAmount(), msg.getType(), msg.getTransactionId());
  }
  
  /**
   * Changes the bidding key in the received "WithdrawFundsMessage" and replaces it with its corresponding
   * bank key. Returns the changed message.
   * @param msg
   * @return
   */
  synchronized WithdrawFundsMessage withdrawFunds(final WithdrawFundsMessage msg)
  {
    return new WithdrawFundsMessage(agentBankKeys.get(msg.getAccountNumber()), msg.getAmount());
  }
  
  /**
   * Prints the information of the computer that AuctionCentral is running on.
   */
  String printInfo()
  {
    String infoString = null;
    try
    {
      infoString = "Server Ip: " + InetAddress.getLocalHost() +"\n"+ "Server host name: " + InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
    return infoString;
  }
}
