package Agent;

import Bank.Bank;

import java.io.*;
import java.net.Socket;
import Messages.*;
import java.util.Scanner;

public class AgentBankAccount extends Thread
{
  private static int accountNumber;
  private static String secretKey;

  int getAccountNumber() { return accountNumber; }

  String getSecretKey() { return secretKey; }

  private static void connectToBank(final String hostname, final String name, final int initialBalance)
  {
    try
    {
      final Socket s = new Socket(hostname, Bank.PORT);
      final ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

      oos.writeObject(new CreateBankAccountMessage(name, initialBalance));

      Object o = ois.readObject();
      accountNumber = ((BankAccountInfoMessage)o).getAccountNumber();
      System.out.println("Account number: " + accountNumber);

      oos.writeObject(new CloseConnectionMessage());

    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private String setName(Scanner scanner)
  {
    System.out.print("Enter name: ");
    return scanner.nextLine();
  }

  private int setAmount(Scanner scanner)
  {
    System.out.print("Deposit amount?: ");
    return scanner.nextInt();
  }

  AgentBankAccount()
  {
    final Scanner in = new Scanner(System.in);
    System.out.print("Enter bank host name: ");
    final String hostname = in.nextLine();

    final String name = setName(in);
    final int balance = setAmount(in);

    new Thread(() -> connectToBank(hostname, name, balance)).start();
  }
}
