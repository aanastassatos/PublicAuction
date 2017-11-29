package Bank;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class BankTest
{
  private final static Random rand = new Random();
  public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException
  {
    final Scanner in = new Scanner(System.in);
    System.out.print("Enter bank host name: ");
    final String hostname = in.nextLine();

    for(char i = 'A'; i < 'Z'; i++)
    {
      final String name = Character.toString(i);
      new Thread(() -> test(hostname, name)).start();
      Thread.sleep(getVal(rand, 1));
    }
  }

  private static int getVal(final Random rand, final int bound)
  {
    return Math.abs(rand.nextInt() % bound);
  }

  private static void test(final String hostname, final String name)// throws IOException, ClassNotFoundException
  {
    try
    {
      // create socket connecting to bank
      final Socket s = new Socket(hostname, Bank.PORT);
      // output stream to which objects that are to be sent to the bank are written
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      // input stream from which objects sent from the bank are received
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      final int initialBalance = getVal(rand, 5000);
      final int timeBound = 2500;

      int amount = getVal(rand, initialBalance);

      final UUID transactionId = UUID.randomUUID();

      // create an account with the bank
      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      // read reply to previous action, i.e. a message containing the resulting account information
      Object o = ois.readObject();
      // extract account information from message
      int accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      int secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      System.out.println("Name: " + name + "\nAccount number: " + accountNumber + "\nSecret Key: " + secretKey);

      Thread.sleep(getVal(rand, timeBound));

      // send bank a message requesting a block be placed on given account for given amount, specifying that a block
      // is being added
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Add, transactionId));
      // read reply to previous action, whether the block was successful or failed
      o = ois.readObject();
      // extract result
      boolean succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      Thread.sleep(getVal(rand, timeBound));

      // invalid block request
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, initialBalance - amount + 1,
              ModifyBlockedFundsMessage.TransactionType.Add, transactionId));
      o = ois.readObject();
      // in this case the message returns a false as the block request was too large
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + (initialBalance - amount + 1) + " " + (succeeded ? "succeeded" : "failed"));


      Thread.sleep(getVal(rand, timeBound));
      // block remove
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Remove, transactionId));
      o = ois.readObject();
      succeeded = ((BlockFundsResultMessage) o).getResult();
      System.out.println("Attempt to unblock " + amount + " " + (succeeded ? "succeeded" : "failed"));


      Thread.sleep(getVal(rand, timeBound));

      amount = getVal(rand, initialBalance);

      // withdraw funds, cannot fail as to have an overdraw at this point indicates a logic failure
      // throws exception in case of overdraw
      oos.writeObject(new WithdrawFundsMessage(secretKey, amount));

      // close connection when finished, ie allowing the server to close the connection rather than the other way
      oos.writeObject(new CloseConnectionMessage());
    }catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
