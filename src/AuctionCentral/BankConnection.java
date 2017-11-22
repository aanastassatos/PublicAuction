package AuctionCentral;

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
  private final AuctionCentral auctionCentral;
  
  
  BankConnection(String address, AuctionCentral auctionCentral) throws UnknownHostException, IOException
  {
    Socket socket = new Socket(address, BANKPORT);
    this.auctionCentral = auctionCentral;
    ois = new ObjectInputStream(socket.getInputStream());
    oos = new ObjectOutputStream(socket.getOutputStream());
  }
  
  @Override
  public void run()
  {
    while(true)
    {
    
    }
  }
}
