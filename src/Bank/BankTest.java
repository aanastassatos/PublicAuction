package Bank;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BankTest
{
  public static void main(String args[]) throws IOException, ClassNotFoundException
  {
    final Scanner in = new Scanner(System.in);
    System.out.print("Enter bank host name: ");
    final String hostname = in.nextLine();

    for(char i = 'A'; i < 'z'; i++)
    {
      final String name = Character.toString(i);
      final int balance = i;
      new Thread(() -> test(hostname, name, balance * 100)).start();
    }
  }

  private static void test(final String hostname, final String name, final int initialBalance)// throws IOException, ClassNotFoundException
  {
    try
    {
      final Socket s = new Socket(hostname, Bank.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      Object o = ois.readObject();
      int accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      System.out.println("Name: " + name + "\nAccount number: " + accountNumber);

      int amount = 12;

      oos.writeObject(new ModifyBlockedFundsMessage(accountNumber, amount, ModifyBlockedFundsMessage.TransactionType.Add));
      o = ois.readObject();
      boolean succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      amount = 100000;

      oos.writeObject(new ModifyBlockedFundsMessage(accountNumber, amount, ModifyBlockedFundsMessage.TransactionType.Add));
      o = ois.readObject();
      succeeded = ((BlockFundsResultMessage)o).getResult();
      System.out.println("Attempt to block " + amount + " " + (succeeded ? "succeeded" : "failed"));

      amount = 5;

      oos.writeObject(new WithdrawFundsMessage(accountNumber, amount));
      oos.writeObject(new CloseConnectionMessage());

//      s.close();
    }catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
