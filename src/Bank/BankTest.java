package Bank;

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

    new Thread(() -> test(hostname, "Dylan", 123)).start();
    new Thread(() -> test(hostname, "foo", 321)).start();
  }

  private static void test(final String hostname, final String name, final int initialBalance)// throws IOException, ClassNotFoundException
  {
    try
    {
      final Socket s = new Socket(hostname, Bank.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      Object n;

      n = ois.readObject();
      System.out.println("Name: " + name + "\nAccount number: " + n);

      oos.writeObject(new ModifyBlockedFundsMessage((int) n, 12, ModifyBlockedFundsMessage.TransactionType.Add));
      oos.writeObject(new ModifyBlockedFundsMessage((int) n, 12, ModifyBlockedFundsMessage.TransactionType.Remove));
      oos.writeObject(new WithdrawFundsMessage((int) n, 12));
      oos.writeObject(new CloseConnectionMessage());

      s.close();
    }catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
