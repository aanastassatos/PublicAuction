package AuctionCentral;

import Bank.Bank;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class AuctionCentralTest extends Thread
{
  private static Socket bankSocket;
  private static final String HOSTNAME = "localhost";
  
  public static void main(String [] args)
  {
    try
    {
      bankSocket = new Socket(HOSTNAME, Bank.PORT);
    }catch (IOException e){
      e.printStackTrace();
    }
    
    new Thread(() -> registerTestAgent("Bob", 5000)).start();
   // new Thread(() -> testAuctionHouse()).start();
  }
  
  
  private static void registerTestAgent(String name, int initBal)
  {
    ObjectInputStream bankois;
    ObjectOutputStream bankoos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    int accountNumber;
    int secretKey;
    try
    {
      bankoos = new ObjectOutputStream(bankSocket.getOutputStream());
      bankois = new ObjectInputStream(bankSocket.getInputStream());
      bankoos.writeObject(new CreateBankAccountMessage(name, initBal));
      Object o = bankois.readObject();
  
      accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      
      System.out.println(name + " opened an account with an initial balance of "+initBal+"\n" +
                         "Account number: "+accountNumber+"\n" +
                         "Secret key: "+secretKey);
      
      Socket socket =  new Socket(HOSTNAME, AuctionCentral.PORT);
      
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      
      oos.writeObject(new RegisterAgentMessage("Bob", secretKey));
      o = ois.readObject();
      int biddingID = ((AgentInfoMessage) o).getBiddingKey();
      System.out.println(name +" successfully registered with auction central with the biddingID "+biddingID);
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    
    while(true)
    {
    
    }
  }
  
  private static void testAuctionHouse()
  {
    ObjectOutputStream auctionhouseoos;
    ObjectInputStream auctionhouseois;
    ObjectOutputStream bankoos;
    ObjectInputStream bankois;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int accountNumber;
    int bankKey;
    String name = String.valueOf(AuctionCentral.rand.nextInt());
    int initBal = 5000;
    try
    {
      bankoos = new ObjectOutputStream(bankSocket.getOutputStream());
      bankois = new ObjectInputStream(bankSocket.getInputStream());
      bankoos.writeObject(new CreateBankAccountMessage(name, initBal));
      Object o = bankois.readObject();
  
      accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      bankKey = ((BankAccountInfoMessage)o).getSecretKey();
  
      System.out.println(name + " opened an account with an initial balance of "+initBal+"\n" +
          "Account number: "+accountNumber+"\n" +
          "Secret key: "+bankKey);
  
      Socket socket =  new Socket(HOSTNAME, AuctionCentral.PORT);
  
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
  
      oos.writeObject(new RegisterAgentMessage("Bob", bankKey));
      o = ois.readObject();
      int biddingID = ((AgentInfoMessage) o).getBiddingKey();
      System.out.println(name +" successfully registered with auction central with the biddingID "+biddingID);
      Socket auctionsocket =  new Socket(HOSTNAME, AuctionCentral.PORT);
      auctionhouseoos = new ObjectOutputStream(auctionsocket.getOutputStream());
      auctionhouseois = new ObjectInputStream(auctionsocket.getInputStream());
      auctionhouseoos.writeObject(new RegisterAuctionHouseMessage("AuctionHouseBob", "localhost", 55557));
      o = auctionhouseois.readObject();
      int publicID = ((AuctionHouseInfoMessage) o).getPublicID();
      int secretKey = ((AuctionHouseInfoMessage) o).getSecretKey();
      System.out.println("AuctionHouseBob registered with auction central with the public ID "+publicID+" and the secret key "+secretKey);
      
      auctionhouseoos.writeObject(new ModifyBlockedFundsMessage(biddingID, 50, ModifyBlockedFundsMessage.TransactionType.Add, UUID.randomUUID()));
      auctionhouseoos.writeObject(new ModifyBlockedFundsMessage(biddingID, 5000000, ModifyBlockedFundsMessage.TransactionType.Add, UUID.randomUUID()));
      auctionhouseoos.writeObject(new WithdrawFundsMessage(biddingID, 50));
      o = auctionhouseois.readObject();
      if(((BlockFundsResultMessage)o).getResult()) System.out.println("AuctionHouseBob successfully modified the funds of the bidding id "+biddingID);
      else System.out.println("something went wrong");
      
      o = auctionhouseois.readObject();
      if(((BlockFundsResultMessage)o).getResult()) System.out.println("AuctionHouseBob successfully modified the funds of the bidding id "+biddingID);
      else System.out.println("something went wrong");
      
      auctionhouseoos.writeObject(new DeregisterAuctionHouseMessage(publicID, secretKey));
      o = auctionhouseois.readObject();
      auctionhouseoos.writeObject(new CloseConnectionMessage());
      o = auctionhouseois.readObject();
      auctionhouseoos.close();
      auctionsocket.close();
      oos.writeObject(new CloseConnectionMessage());
      o = ois.readObject();
      oos.close();
      socket.close();
      bankoos.writeObject(new CloseConnectionMessage());
      bankoos.close();
      bankSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
