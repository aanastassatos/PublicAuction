package Bank;

import Messages.BankAccountInfoMessage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

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
  /*
    Banks run in their own processes, independent of other components
   */
  public static void main(final String args[])
  {
    final Bank b = new Bank();
    b.printInfo();
    b.start();
  }

  // static port number
  public final static int PORT = 55555;

  // map of secret keys to user accounts, Secret keys are the only method of referencing accounts as it is the
  // only bank information given to AuctionCentral
  private final HashMap<Integer, BankAccount> keyMap = new HashMap<>();
  private BankGui gui;

  private ServerSocket bankSocket;

  /**
   * Instantiates server
   */
  private Bank()
  {
    try
    {
      bankSocket = new ServerSocket(PORT);
      new JFXPanel(); // init jfx
      Platform.runLater(() -> gui = new BankGui(this));
    } catch (IOException e)
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
    while(!isInterrupted())
    {
      try
      {
        // Continuously looking to accept new clients, either Agent or AuctionCentral
        Socket socket = bankSocket.accept();
        // instantiate client with reference to their socket and the bank
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
    // account number is only used for displaying account info,
    final int accountNumber = name.hashCode();
    // all functional ability resides in the secret key
    final int secretKey = getKey(accountNumber);

    // Secret keys are generated based on names, can't have secret key conflicts
    if(keyMap.get(secretKey) != null) throw new RuntimeException("Attempt to create multiple accounts for one name");
    // create bank account and populate map allowing account information and funds to be modified when given a key
    BankAccount account = new BankAccount(new Fund(initialBalance), accountNumber, name);
    keyMap.put(secretKey, account);
    Platform.runLater(() -> gui.addAccount(account));

    System.out.println("Created bank account " + accountNumber + " for " + name);
    return new BankAccountInfoMessage(accountNumber, secretKey);
  }

  /*
  Called only when a auction has been successful, in theory the nature of setting blocks should prevent
  attempted overdrawing at this point
   */
  synchronized void withdrawFunds(final int secretKey, final int amount)
  {
    keyMap.get(secretKey).getFund().removeBlocked(amount);
    keyMap.get(secretKey).getFund().withdraw(amount);
    System.out.println("Withdrew " + amount + " from account " + keyMap.get(secretKey).getAccountNumber()
            + " leaving " + keyMap.get(secretKey).getFund().toString());
  }

  /*
  When a agent places a bid on an auction, a block is placed on their funds for that amount
  If the auction ends with the agent winning, the block is removed, then the amount for the auction is withdrawn
  from the account. As the money being withdrawn should be made fully available right before by the block being
  released, the only place where insufficient funds should occur is when placing blocks.
   */
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

  void shutdown()
  {
    interrupt();
    try
    {
      bankSocket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void printInfo()
  {
    try
    {
      // Note that when running the entire application on the same machine only the port number is relevant
      System.out.println("Bank Ip: " + InetAddress.getLocalHost());
      System.out.println("Bank host name: " + InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  // When given an account number produce a unique key
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
    // int to byte array
    final byte[] hash = digest.digest(ByteBuffer.allocate(4).putInt(accountNumber).array());
    // byte array to int
    return Arrays.hashCode(hash);
  }
}
