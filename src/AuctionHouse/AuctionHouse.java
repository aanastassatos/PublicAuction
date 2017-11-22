package AuctionHouse;

import Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class AuctionHouse
{
  private Socket socket;
  private final int maxNumOfItems = 10;
  private int secretKey;
  private int publicID;
  private Random r = new Random();

  public AuctionHouse(String address, int port, String name) throws UnknownHostException, IOException
  {
    HouseItems houseItems = new HouseItems(r.nextInt(maxNumOfItems));
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

  int getSecretKey()
  {
    return secretKey;
  }

  int getPublicID()
  {
    return publicID;
  }
}
