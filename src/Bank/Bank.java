package Bank;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * The bank is capable of performing three operations:
 * creating ann account for a new agent, requiring a name and a initial balance
 * placing a hold on a given account for a specified value, effectively lowering the accounts balance until the hold is lifted
 * withdrawing a specified value from an accounts balance
 */
public class Bank extends Thread
{

  public static void main(String agrs[])
  {
    Bank b = new Bank();
    b.printInfo();
    b.start();
  }

  // static port number
  public final static int PORT = 55555;

  private final HashMap<Integer, Fund> fundMap = new HashMap<>();
  private final HashMap<Integer, String> nameMap = new HashMap<>();

  private ServerSocket bankSocket;

  /**
   * Instantiates server
   */
  public Bank()
  {
    try
    {
      bankSocket = new ServerSocket(PORT);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void printInfo()
  {
    try
    {
      System.out.println("Bank Ip: " + InetAddress.getLocalHost());
      System.out.println("Bank host name: " + InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Continuously check for new connections, instantiating and starting a new BankClient for each one
   */
  @Override
  public void run()
  {
    while(true)
    {
      try
      {
        Socket socket = bankSocket.accept();
        BankClient client = new BankClient(socket, this);
        client.start();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Create a new account
   */
  int openAccount(final String name, final int initialBalance)
  {
    final int accountNumber = name.hashCode();
    if(fundMap.get(accountNumber) != null) throw new RuntimeException("Attempt to create multiple accounts for one name");
    nameMap.put(accountNumber, name);
    fundMap.put(accountNumber, new Fund(initialBalance));
    System.out.println("Created bank account " + accountNumber + " for " + name);
    return accountNumber;
  }

  void withdrawFunds(final int accountNumber, final int amount)
  {
    fundMap.get(accountNumber).withdraw(amount);
    System.out.println("Withdrew " + amount + " from account " + accountNumber + " Leaving " + fundMap.get(accountNumber).toString());
  }

  boolean blockFunds(final int accountNumber, final int amount)
  {
    if(fundMap.get(accountNumber).getAvailable() < amount)
    {
      System.out.println(nameMap.get(accountNumber) + " attempted to bid over current available funds");
      return false;
    }
    fundMap.get(accountNumber).addBlocked(amount);
    System.out.println("Blocked " + amount + " on account " + accountNumber + " Leaving " + fundMap.get(accountNumber).toString());
    return true;
  }

  void unblockFunds(final int accountNumber, final int amount)
  {
    fundMap.get(accountNumber).removeBlocked(amount);
    System.out.println("Unblocked " + amount + " on account " + accountNumber + " Leaving " + fundMap.get(accountNumber).toString());
  }


}
