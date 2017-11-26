package AuctionCentral;

import Messages.BlockFundsResultMessage;
import Messages.CloseConnectionMessage;
import Messages.ModifyBlockedFundsMessage;
import Messages.WithdrawFundsMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class BankConnection extends Thread
{
  private static final int BANKPORT = 55555;
  private final ObjectInputStream ois;
  private final ObjectOutputStream oos;
  private final Socket socket;
  private final AuctionCentral auctionCentral;
  
  
  BankConnection(final String address, final AuctionCentral auctionCentral) throws UnknownHostException, IOException
  {
    socket = new Socket(address, BANKPORT);
    this.auctionCentral = auctionCentral;
    oos = new ObjectOutputStream(socket.getOutputStream());
    ois = new ObjectInputStream(socket.getInputStream());
  }
  
  @Override
  public void run()
  {
    while(!Thread.currentThread().isInterrupted())
    {
      Object o;
      try
      {
        // read message in
        o = ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      if(o instanceof ModifyBlockedFundsMessage) handleMessage((ModifyBlockedFundsMessage)o);
      else if(o instanceof BlockFundsResultMessage) handleMessage((BlockFundsResultMessage) o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
    }
  }
  
  private void closeConnection()
  {
    try
    {
      ois.close();
      socket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final ModifyBlockedFundsMessage msg)
  {
    try
    {
      oos.writeObject(msg);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final BlockFundsResultMessage msg)
  {
    System.out.println(msg.getResult());
  }
}
