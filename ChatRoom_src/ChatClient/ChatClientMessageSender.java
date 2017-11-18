package ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClientMessageSender extends Thread{
	
	/**
	 * The purpose of this class is to listen for input from user to send to server
	 */
	
	private Socket socket;
	private ObjectOutputStream oos;
	private String name;
	private BufferedReader reader;
	private String line;
	private boolean bool = false;

	public ChatClientMessageSender(Socket socket, String name) throws IOException
	{
		this.socket = socket;
		this.name = name;
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public ChatClientMessageSender(Socket socket, String name, BufferedReader reader) throws IOException
	{
		this.socket = socket;
		this.reader = reader;
		oos = new ObjectOutputStream(socket.getOutputStream());
		this.name = name;
	}

	@Override
	public void run() {
		// The try-catch is outside of the while to prevent infinite loop on server die
		try{
			while(true)
			{

				// this read is blocking
        if (reader != null)
        {
          line = reader.readLine();
        }

        Message m = new Message(name,line);
        line = null;
        oos.writeObject(m);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setLine(String string)
  {
    line = string;
    bool = true;
  }
	
}
