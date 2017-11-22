package Bank;

import Messages.*;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
        return;
      }
      if(o instanceof CreateBankAccountMessage) handleMessage((CreateBankAccountMessage)o);
      else if(o instanceof WithdrawFundsMessage) handleMessage((WithdrawFundsMessage)o);
      else if(o instanceof ModifyBlockedFundsMessage) handleMessage((ModifyBlockedFundsMessage)o);
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

  private void handleMessage(final CreateBankAccountMessage message)
  {
    final Pair<Integer, Integer> accountInfo = bank.openAccount(message.getName(), message.getInitialBalance());
    try
    {
      objectOutputStream.writeObject(new BankAccountInfoMessage(accountInfo.getKey(), accountInfo.getValue()));
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
