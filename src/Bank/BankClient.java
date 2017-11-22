package Bank;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Each time the bank receives a new connection, a client is created to monitor the connection and
 * handle any messages sent, in some cases replying to messages as well
 */
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
        // read message in
        o = objectInputStream.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      // Bank only ever receives a handful of different types of messages
      if(o instanceof CreateBankAccountMessage) handleMessage((CreateBankAccountMessage)o);
      else if(o instanceof WithdrawFundsMessage) handleMessage((WithdrawFundsMessage)o);
      else if(o instanceof ModifyBlockedFundsMessage) handleMessage((ModifyBlockedFundsMessage)o);
      // close connection message allows the server to close the socket, rather than the other way, eliminating exceptions
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
    }
  }

  private void closeConnection()
  {
    try
    {
      objectInputStream.close();
      socket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  // Create bank account based on info in message, then return bank account info to sender
  private void handleMessage(final CreateBankAccountMessage message)
  {
    final BankAccountInfoMessage accountInfo = bank.openAccount(message.getName(), message.getInitialBalance());
    try
    {
      objectOutputStream.writeObject(accountInfo);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void handleMessage(final WithdrawFundsMessage message)
  {
    bank.withdrawFunds(message.getAccountNumber(), message.getAmount());
  }

  // Attempt to place block based on message, return a message indicating whether this succeeded to sender
  private void handleMessage(final ModifyBlockedFundsMessage message)
  {
    boolean succeeded = true;
    if(message.getType() == ModifyBlockedFundsMessage.TransactionType.Add)
    {
      succeeded = bank.blockFunds(message.getAccountNumber(), message.getAmount());
    }
    else bank.unblockFunds(message.getAccountNumber(), message.getAmount());
    try
    {
       objectOutputStream.writeObject(new BlockFundsResultMessage(succeeded));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
