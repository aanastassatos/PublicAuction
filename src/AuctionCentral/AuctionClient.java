/**
 * Created by Alex Anastassatos
 *
 * Serves as a proxy between AuctionCentral and whatever client is currently connected.
 */

package AuctionCentral;

import Bank.Bank;
import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionClient extends Thread
{
  private final AuctionCentral auctionCentral;  //A reference to AuctionCentral in order to use the methods within it
  private ObjectInputStream client_ois;   //Object input stream to the client.
  private ObjectOutputStream client_oos;  //Object output stream to the client.
  private ObjectInputStream bank_ois = null;  //Object input stream to the bank (only used if client is an auction house)
  private ObjectOutputStream bank_oos = null; //Object output stream to the bank (only used if client is an auction house)
  private final Socket socket;  //Socket made by client to connect to AuctionCentral
  
  /**
   * Instantiates AuctionClient with a socket and a reference to AuctionCentral
   * @param socket
   * @param auctionCentral
   */
  AuctionClient(final Socket socket, final AuctionCentral auctionCentral)
  {
    this.auctionCentral = auctionCentral;
    this.socket = socket;
    try
    {

      client_oos = new ObjectOutputStream(socket.getOutputStream());
      client_ois = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Loops continuously, reading messages that come through the Object Input Stream, and handling messages.
   */
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
  
  /**
   * Closes the connection to a client, and (if client is an auction house) to the bank
   */
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
  
  /**
   * Handles the RegisterAgentMessage
   * @param msg
   */
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
  
  /**
   * Handles the RegisterAuctionHouseMessage
   * @param msg
   */
  private void handleMessage(final RegisterAuctionHouseMessage msg)
  {
    try
    {
      Socket bankSocket = new Socket(AuctionCentral.BANK_ADDRESS, Bank.PORT);
      bank_oos = new ObjectOutputStream(bankSocket.getOutputStream());
      bank_ois = new ObjectInputStream(bankSocket.getInputStream());
      client_oos.writeObject(auctionCentral.registerAuctionHouse(msg.getName(), this, msg.getAddress(), msg.getPort()));
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Handles the DeregisterAuctionHouseMessage
   * @param msg
   */
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
  
  /**
   * Handles the RequestAuctionHouseListMessage
   * @param msg
   */
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
  
  /**
   * Handles the ModifyBlockedFundsMessage
   * @param msg
   */
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
  
  /**
   * Handles the BlockFundsResultMessage
   * @param msg
   */
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
  
  /**
   * Handles the WithdrawFundsMessage
   * @param msg
   */
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
