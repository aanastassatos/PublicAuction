package AuctionCentral;

import Bank.Bank;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionClient extends Thread
{
  private final AuctionCentral auctionCentral;
  private ObjectInputStream client_ois;
  private ObjectOutputStream client_oos;
  private ObjectInputStream bank_ois = null;
  private ObjectOutputStream bank_oos = null;
  private final Socket socket;
  
  AuctionClient(final Socket socket, final AuctionCentral auctionCentral)
  {
    this.auctionCentral = auctionCentral;
    this.socket = socket;
    try
    {
      client_ois = new ObjectInputStream(socket.getInputStream());
      client_oos = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  @Override
  public void run()
  {
    Object o;
    while(true)
    {
      o = null;
      try
      {
        o = client_ois.readObject();
      } catch (Exception e)
      {
        e.printStackTrace();
        return;
      }
      
      if(o instanceof RegisterAgentMessage) handleMessage((RegisterAgentMessage) o);
      else if(o instanceof RegisterAuctionHouseMessage) handleMessage((RegisterAuctionHouseMessage) o);
      else if(o instanceof DeregisterAuctionHouseMessage) handleMessage((DeregisterAuctionHouseMessage) o);
      else if(o instanceof RequestAuctionHouseListMessage) handleMessage((RequestAuctionHouseListMessage) o);
      else if(o instanceof RequestConnectionToAuctionHouseMessage) handleMessage((RequestConnectionToAuctionHouseMessage) o);
      else if(o instanceof ModifyBlockedFundsMessage) handleMessage((ModifyBlockedFundsMessage) o);
      else if(o instanceof BlockFundsResultMessage) handleMessage((BlockFundsResultMessage) o);
      else if(o instanceof WithdrawFundsMessage) handleMessage((WithdrawFundsMessage) o);
      else if(o instanceof CloseConnectionMessage)
      {
        closeConnection();
        return;
      }
      else throw new RuntimeException("Received unknown message");
    }
  }
  
  synchronized AuctionHouseConnectionInfoMessage requestConnection(RequestConnectionToAuctionHouseMessage msg)
  {
    try
    {
      client_oos.writeObject(msg);
      Object o = client_ois.readObject();
      return (AuctionHouseConnectionInfoMessage) o;
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  private void closeConnection()
  {
    try
    {
      client_oos.writeObject(new CloseConnectionMessage());
      client_ois.close();
      if(bank_ois != null)
      {
        bank_oos.writeObject(new CloseConnectionMessage());
        bank_ois.close();
      }
      socket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RegisterAgentMessage msg)
  {
    try
    {
      client_oos.writeObject(auctionCentral.registerAgent(msg.getName(), msg.getBankKey(), this));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RegisterAuctionHouseMessage msg)
  {
    try
    {
      Socket bankSocket = new Socket(AuctionCentral.BANK_ADDRESS, Bank.PORT);
      bank_oos = new ObjectOutputStream(bankSocket.getOutputStream());
      bank_ois = new ObjectInputStream(bankSocket.getInputStream());
      client_oos.writeObject(auctionCentral.registerAuctionHouse(msg.getName(), this));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final DeregisterAuctionHouseMessage msg)
  {
    try
    {
      client_oos.writeObject(auctionCentral.deRegisterAuctionHouse(msg.getPublicID(), msg.getSecretKey()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RequestAuctionHouseListMessage msg)
  {
    try
    {
      client_oos.writeObject(auctionCentral.getAuctionHouseList());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final RequestConnectionToAuctionHouseMessage msg)
  {
    try
    {
      client_oos.writeObject(auctionCentral.connectClientToAuctionHouse(msg, msg.getAuctionHouseID()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final ModifyBlockedFundsMessage msg)
  {
    try
    {
      bank_oos.writeObject(auctionCentral.modifyBlockedFunds(msg));
      client_oos.writeObject(bank_ois.readObject());
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final BlockFundsResultMessage msg)
  {
    try
    {
      client_oos.writeObject(msg);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void handleMessage(final WithdrawFundsMessage msg)
  {
    try
    {
      bank_oos.writeObject(auctionCentral.withdrawFunds(msg));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
