package AuctionCentral;

import Bank.Bank;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionCentralTest extends Thread
{
  private static Socket bankSocket;
  private static Socket auctionCentralSocket;
  private static int biddingID;
  
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
    
    new Thread(() -> registerTestAgent("Bob", 5000)).start();
    new Thread(() -> testAuctionHouse(hostname)).start();
  }
  
  
  private static void registerTestAgent(String name, int initBal)
  {
    ObjectInputStream ois;
    ObjectOutputStream oos;
    int accountNumber;
    int secretKey;
    try
    {
      oos = new ObjectOutputStream(bankSocket.getOutputStream());
      ois = new ObjectInputStream(bankSocket.getInputStream());
      oos.writeObject(new CreateBankAccountMessage(name, initBal));
      Object o = ois.readObject();
  
      accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      
      System.out.println(name + " opened an account with an initial balance of "+initBal+"\n" +
                         "Account number: "+accountNumber+"\n" +
                         "Secret key: "+secretKey);
      
      oos = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
      ois = new ObjectInputStream(auctionCentralSocket.getInputStream());
      
      oos.writeObject(new RegisterAgentMessage("Bob", secretKey));
      o = ois.readObject();
      biddingID = ((AgentInfoMessage) o).getBiddingKey();
      System.out.println(name +" successfully registered with auction central with the biddingID "+biddingID);
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  private static void testAuctionHouse(String hostName)
  {
    ObjectOutputStream oos;
    ObjectInputStream ois;
    try
    {
      oos = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
      ois = new ObjectInputStream(auctionCentralSocket.getInputStream());
      oos.writeObject(new RegisterAuctionHouseMessage("AuctionHouseBob"));
      Object o = ois.readObject();
      int publicID = ((AuctionHouseInfoMessage) o).getPublicID();
      int secretKey = ((AuctionHouseInfoMessage) o).getSecretKey();
      System.out.println("AuctionHouseBob");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
