package AuctionHouse;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuctionHouse
{
  private Socket socket;
  private int secretKey;
  private int publicID;

  public AuctionHouse(String address, int port, String name) throws UnknownHostException, IOException
  {
    try
    {
      socket = new Socket(address, port);
      final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
      oos.writeObject(new RegisterAuctionHouseMessage(name));
      Object o = ois.readObject();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public int getSecretKey()
  {
    return secretKey;
  }

  public int getPublicID()
  {
    return publicID;
  }

}
