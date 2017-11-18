package ChatClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientMessageReciever extends Thread{

	/**
	 * This class is used by the chat client to listen for messages from the server
	 */
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss");
	
	private Socket socket;
	private ObjectInputStream ois;
	private String output;
	
	public ChatClientMessageReciever(Socket socket) throws IOException
	{
		this.socket = socket;
		 ois = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		// The try-catch is outside of the while to prevent infinite loop on server die
		try{
			while(true)
			{
				// this read is blocking
				Message m = (Message) ois.readObject();
				output = sdf.format(new Date(m.timestamp))+" - "+m.from+": "+m
                .msg;
				System.out.println(output);
			}
		}catch(Exception e) //  an exception is thrown when connection to the server is lost
		{
			System.out.println("Server has closed connection!");
		}
		finally{
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getOutput()
  {
    return output;
  }

  public void setNullOutput()
  {
    output = null;
  }
}
