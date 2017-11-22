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
  public static void main(String args[]) throws IOException, ClassNotFoundException
  {
    final Scanner in = new Scanner(System.in);
    System.out.print("Enter bank host name: ");
    final String hostname = in.nextLine();

    for(char i = 'A'; i < 'C'; i++)
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
      final Socket s = new Socket(hostname, Bank.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      final Random rand = new Random();
      final int initialBalance = 1000;

      int amount = getVal(rand, initialBalance);

      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      Object o = ois.readObject();
      int accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      int secretKey = ((BankAccountInfoMessage)o).getSecretKey();
      System.out.println("Name: " + name + "\nAccount number: " + accountNumber + "\nSecret Key: " + secretKey);

      // valid block request
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Add));
      o = ois.readObject();
      boolean succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      // invalid block request
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, initialBalance + 1,
              ModifyBlockedFundsMessage.TransactionType.Add));
      o = ois.readObject();
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      // block remove
      oos.writeObject(new ModifyBlockedFundsMessage(secretKey, amount, ModifyBlockedFundsMessage.TransactionType.Remove));
      o = ois.readObject();
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to unblock " + amount + " " + (succeeded ? "succeeded" : "failed"));

      amount = getVal(rand, initialBalance);

      oos.writeObject(new WithdrawFundsMessage(secretKey, amount));
      oos.writeObject(new CloseConnectionMessage());
    }catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
