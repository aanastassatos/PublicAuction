package Agent;

import Bank.Bank;

import java.io.*;
import java.net.Socket;
import Messages.*;
import java.util.Scanner;

public class AgentBankAccount extends Thread
{
  private static int accountNumber;
  private String hostname;
  private String name;
  private static volatile Integer secretKey = null;

  int getAccountNumber() { return accountNumber; }

  int getSecretKey()
  {
    while (secretKey == null)
    {

    }
    return secretKey;
  }

  String getHostName() { return hostname; }

  String getAgentName() { return name; }

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
      secretKey = ((BankAccountInfoMessage)o).getSecretKey();
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
    this.hostname = in.nextLine();
    this.name = setName(in);

    final int balance = setAmount(in);

    new Thread(() -> connectToBank(hostname, name, balance)).start();
  }
}
