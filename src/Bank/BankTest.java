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

    test(hostname, "asd");
    test(hostname, "qwe");
  }

  private static void test(final String hostname, final String name) throws IOException, ClassNotFoundException
  {
    final Socket s = new Socket(hostname, Bank.PORT);
    final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
    final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

    oos.writeObject(new CreateBankAccountMessage(name, 321));

    Object n;

    n = ois.readObject();
    System.out.println("Name: " + name + "\nAccount number: " + n);
  }
}
