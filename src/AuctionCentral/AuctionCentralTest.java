package AuctionCentral;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class AuctionCentralTest extends Thread
{
  public static void main(String [] args)
  {
    final String hostname = "localhost";
    
    new Thread(() -> testAgent(hostname)).start();
    new Thread(() -> testAuctionHouse(hostname)).start();
  }
  
  
  private static void testAgent(String hostName)
  {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    try
    {
      Socket socket = new Socket(hostName, 7777);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      oos.writeObject(new RegisterAgentMessage("Bob", 12345));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void testAuctionHouse(String hostName)
  {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    try
    {
      Socket socket = new Socket(hostName, 7777);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      oos.writeObject(new RegisterAuctionHouseMessage("AuctionHouseBob"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
