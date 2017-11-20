package AuctionCentral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AuctionCentral extends Thread
{
  ServerSocket socket;
  
  //This creates a server and instantiates it with a port number given by the user.
  public static void main(String[] args) {
    System.out.println("Enter port number: ");
    try{
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = reader.readLine();
      reader.close();
      new AuctionCentral(Integer.parseInt(line)).start();
    }catch(NumberFormatException e){
      System.out.println("You must enter a number!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public AuctionCentral(int port) throws IOException
  {
    socket = new ServerSocket(port);
  }
}
