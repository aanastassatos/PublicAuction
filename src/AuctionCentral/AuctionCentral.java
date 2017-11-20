package AuctionCentral;

import java.io.BufferedReader;
import AuctionHouse.AuctionHouse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
  
//  private ArrayList <AuctionHouseClient> auctionHouses;
//  private ArrayList <AgentClient> agents;
  
  private ServerSocket auctionCentralSocket;
  
  public AuctionCentral(int port) throws IOException
  {
    auctionCentralSocket = new ServerSocket(port);
    printInfo();
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
