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
      }
      if(o instanceof CreateBankAccountMessage) handleMessage((CreateBankAccountMessage)o);
      else if(o instanceof WithdrawFundsMessage) handleMessage((WithdrawFundsMessage)o);
      else if(o instanceof ModifyBlockedFundsMessage) handleMessage((ModifyBlockedFundsMessage)o);
      else if(o instanceof CloseConnectionMessage)
      {
        try
        {
          objectInputStream.close();
          socket.close();
        } catch (IOException e)
        {
          e.printStackTrace();
        }
        return;
      }
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

  private void handleMessage(final WithdrawFundsMessage message)
  {
    bank.withdrawFunds(message.getAccountNumber(), message.getAmount());
  }

  private void handleMessage(final ModifyBlockedFundsMessage message)
  {
    if(message.getType() == ModifyBlockedFundsMessage.TransactionType.Add)
    {
      bank.blockFunds(message.getAccountNumber(), message.getAmount());
    }
    else bank.unblockFunds(message.getAccountNumber(), message.getAmount());
  }
}
