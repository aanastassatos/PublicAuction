package Bank;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class BankTest
{
  public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException
  {
    final Scanner in = new Scanner(System.in);
    System.out.print("Enter bank host name: ");
    final String hostname = in.nextLine();

    for(char i = 'A'; i < 'R'; i++)
    {
      final String name = Character.toString(i);
      new Thread(() -> test(hostname, name)).start();
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
      final Random rand = new Random();
      final int initialBalance = 1000;

      int amount = getVal(rand, initialBalance);

      // create an account with the bank
      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      // read reply to previous action, i.e. a message containing the resulting account information
      Object o = ois.readObject();
      // extract account information from message
      int accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      int secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      System.out.println("Name: " + name + "\nAccount number: " + accountNumber + "\nSecret Key: " + secretKey);

      Thread.sleep(getVal(rand, 5000));

      // send bank a message requesting a block be placed on given account for given amount, specifying that a block
      // is being added
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Add));
      // read reply to previous action, whether the block was successful or failed
      o = ois.readObject();
      // extract result
      boolean succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      Thread.sleep(getVal(rand, 5000));

      // invalid block request
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, initialBalance + 1,
              ModifyBlockedFundsMessage.TransactionType.Add));
      o = ois.readObject();
      // in this case the message returns a false as the block request was too large
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      // block remove
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Remove));
      o = ois.readObject();
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to unblock " + amount + " " + (succeeded ? "succeeded" : "failed"));

      Thread.sleep(getVal(rand, 5000));

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
