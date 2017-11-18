package Bank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BankClient extends Thread
{
  private final Bank bank;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private final Socket socket;

  BankClient(final Socket socket, final Bank bank)
  {
    this.bank = bank;
    this.socket = socket;
    try
    {
      objectInputStream = new ObjectInputStream(socket.getInputStream());
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {
    while(true)
    {
      Object o = null;
      try
      {
        o = objectInputStream.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        break;
      }
      if(o instanceof CreateBankAccountMessage) handleMessage((CreateBankAccountMessage)o);
    }
  }

  private void handleMessage(final CreateBankAccountMessage message)
  {
    final int accountNumber = bank.openAccount(message.getName(), message.getInitialBalance());
    try
    {
      objectOutputStream.writeObject(accountNumber);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
