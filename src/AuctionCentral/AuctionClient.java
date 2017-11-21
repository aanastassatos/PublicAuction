package AuctionCentral;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AuctionClient extends Thread
{
  private final AuctionCentral auctionCentral;
  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;
  private final Socket socket;
  
  AuctionClient(final Socket socket, final AuctionCentral auctionCentral)
  {
    this.socket = socket;
    this.auctionCentral = auctionCentral;
    try
    {
      objectInputStream = new ObjectInputStream(socket.getInputStream());
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
