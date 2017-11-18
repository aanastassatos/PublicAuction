package ChatServer;

import ChatClient.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteClient extends Thread{
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	/** a reference to the chat server is needed because if a message from this 
	 * client is received then we need to call sendMessageToClients to send the message
	 * to all other connected clients.
	 */
	private ChatServer server;
	
	public RemoteClient(Socket socket, ChatServer server) throws IOException
	{
		this.socket = socket;
		this.server = server;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	public void sendMessage(Message m) throws IOException
	{
		oos.writeObject(m);
	}
	/**
	 * Thread will auto terminate when client disconnects because IOException will be thrown
	 * so the while loop will be escaped.
	 */
	@Override
	public void run() {
		try{
			while(true)
			{
				// read is blocking
				Message m = (Message) ois.readObject();
				if(m.msg!=null && !m.msg.isEmpty())
					server.sendMessageToClients(m);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(socket.getRemoteSocketAddress()+" has disconnected");
		}
	}
	
}
