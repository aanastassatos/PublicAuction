package Bank;

import Messages.BankAccountInfoMessage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The bank is capable of performing three operations:
 * creating ann account for a new agent, requiring a name and a initial balance
 * placing a hold on a given account for a specified value, effectively lowering the accounts balance until the hold is lifted
 * withdrawing a specified value from an accounts balance
 */
public class Bank extends Thread
{
  public static void main(String args[])
  {
    Bank b = new Bank();
    b.printInfo();
    b.start();
  }

  // static port number
  public final static int PORT = 55555;

  private final HashMap<Integer, BankAccount> keyMap = new HashMap<>();

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
  synchronized BankAccountInfoMessage openAccount(final String name, final int initialBalance)
  {
    final int accountNumber = name.hashCode();
    final int secretKey = getKey(accountNumber);

    if(keyMap.get(secretKey) != null) throw new RuntimeException("Attempt to create multiple accounts for one name");
    BankAccount account = new BankAccount(new Fund(initialBalance), accountNumber, name);
    keyMap.put(secretKey, account);

    System.out.println("Created bank account " + accountNumber + " for " + name);
    return new BankAccountInfoMessage(accountNumber, secretKey);
  }

  synchronized void withdrawFunds(final int secretKey, final int amount)
  {
    keyMap.get(secretKey).getFund().withdraw(amount);
    System.out.println("Withdrew " + amount + " from account " + keyMap.get(secretKey).getAccountNumber()
            + " leaving " + keyMap.get(secretKey).getFund().toString());
  }

  synchronized boolean blockFunds(final int secretKey, final int amount)
  {
    if(keyMap.get(secretKey).getFund().getAvailable() < amount)
    {
      System.out.println(keyMap.get(secretKey).getAccountNumber() + " attempted to block more than current available funds");
      return false;
    }
    keyMap.get(secretKey).getFund().addBlocked(amount);
    System.out.println("Blocked " + amount + " on account " + keyMap.get(secretKey).getAccountNumber() +
            " leaving " + keyMap.get(secretKey).getFund().toString());
    return true;
  }

  synchronized void unblockFunds(final int secretKey, final int amount)
  {
    keyMap.get(secretKey).getFund().removeBlocked(amount);
    System.out.println("Unblocked " + amount + " on account " + keyMap.get(secretKey).getAccountNumber()
            + " Leaving " + keyMap.get(secretKey).getFund().toString());
  }

  private int getKey(final int accountNumber)
  {
    MessageDigest digest = null;
    try
    {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    assert digest != null;
    byte[] hash = digest.digest(ByteBuffer.allocate(4).putInt(accountNumber).array());
    return Arrays.hashCode(hash);
  }
}
