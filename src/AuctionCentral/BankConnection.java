package AuctionCentral;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class BankConnection
{
  private final int BANKPORT = 5555;
  
  
  BankConnection(String address) throws UnknownHostException, IOException
  {
    Socket socket = new Socket(address, BANKPORT);
  }
}
