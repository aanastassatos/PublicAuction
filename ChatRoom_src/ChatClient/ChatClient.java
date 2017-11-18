package ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient{
	
	public static BufferedReader reader;
	
	public static void main(String[] args) {
		System.out.print("Enter server address (use localhost if server is on same computer as this client): ");
		try{
			reader = new BufferedReader(new InputStreamReader(System.in));
			String address = reader.readLine();
			System.out.print("Enter server port: ");
			String port = reader.readLine();
			System.out.print("Enter your name: ");
			String name = reader.readLine();
			new ChatClient(address,Integer.parseInt(port),name);
		}
		catch(NumberFormatException e)
		{
			System.out.println("You must enter a proper port number!");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private Socket socket;
	private ChatClientMessageReciever reciever;
	private ChatClientMessageSender sender;
	
	public ChatClient(String address, int port, String name) throws UnknownHostException, IOException
	{
		reader = new BufferedReader(new InputStreamReader(System.in));
		socket = new Socket(address, port);
		
		sender = new ChatClientMessageSender(socket,name,reader);
		reciever = new ChatClientMessageReciever(socket);
		
		sender.start();
		reciever.start();
	}

	public ChatClient(String address, int port, String name, boolean visual)
					throws IOException
	{
		socket = new Socket(address, port);

		sender = new ChatClientMessageSender(socket, name);
		reciever = new ChatClientMessageReciever(socket);

		sender.start();
		reciever.start();
	}

	public ChatClientMessageSender getSender()
	{
		return sender;
	}

	public ChatClientMessageReciever getReciever()
	{
		return reciever;
	}
	
}
