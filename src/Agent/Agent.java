package Agent;

import java.io.*;
import java.net.Socket;

public class Agent extends Thread
{
  private Socket socket;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  public static void main(String[] args) throws Exception
  {
    String ip = "localhost";
    int port = 9999;
    Socket socket = new Socket(ip, port);

    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
    PrintWriter printWriter = new PrintWriter(osw);

  }
}
