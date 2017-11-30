package AuctionCentral;

import Bank.Bank;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class AuctionCentralTest extends Thread
{
  private static Socket bankSocket;
  private static Socket auctionCentralSocket;
  
  public static void main(String [] args)
  {
    final String hostname = "localhost";
    try
    {
      bankSocket = new Socket(hostname, Bank.PORT);
      auctionCentralSocket = new Socket(hostname, AuctionCentral.PORT);
    }catch (IOException e){
      e.printStackTrace();
    }
    
    //new Thread(() -> testAgent()).start();
    new Thread(() -> testAuctionHouse(hostname)).start();
  }
  
  
  private static void testAgent()
  {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    try
    {
      oos = new ObjectOutputStream(bankSocket.getOutputStream());
      ois = new ObjectInputStream(bankSocket.getInputStream());
      oos.writeObject(new CreateBankAccountMessage("Bob", 1000));
      Object o = ois.readObject();
  
      int accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      int secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      
      oos = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
      ois = new ObjectInputStream(auctionCentralSocket.getInputStream());
      
      oos.writeObject(new RegisterAgentMessage("Bob", secretKey));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  private static void testAuctionHouse(String hostName)
  {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    try
    {
      System.out.println("a");
      oos = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
      ois = new ObjectInputStream(auctionCentralSocket.getInputStream());
      oos.writeObject(new RegisterAuctionHouseMessage("AuctionHouseBob"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
