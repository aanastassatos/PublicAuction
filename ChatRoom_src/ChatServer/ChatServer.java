package ChatServer;

import ChatClient.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends Thread{

	//This creates a server and instantiates it with a port number given by the user.
	public static void main(String[] args) {
		System.out.println("Enter port number: ");
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line = reader.readLine();
			reader.close();
			new ChatServer(Integer.parseInt(line)).start();
		}catch(NumberFormatException e){
			System.out.println("You must enter a number!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ServerSocket serverSocket;
	// keep a list of currently connected remote clients
	private List<RemoteClient> clients;
	
	public ChatServer(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		clients = new ArrayList<>();
		InetAddress ip;
		String hostName;
		try {
			ip = InetAddress.getLocalHost();
				hostName = ip.getHostName();
				System.out.println("Current IP: " + ip);
				System.out.println("Current hostname: " + hostName);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Listening for clients on port: "+serverSocket.getLocalPort());
		while(true)
		{
			try {
				// .accept() is blocking
				Socket socket = serverSocket.accept();
				System.out.println("Client connected from address: "+socket.getRemoteSocketAddress());
				RemoteClient client = new RemoteClient(socket,this);
				// welcome client to the server
				client.sendMessage(new Message("SERVER","Welcome to the server!"));
				client.start();
				addClient(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void addClient(RemoteClient c)
	{
		clients.add(c);
	}
	public synchronized void sendMessageToClients(Message m)
	{
		List<RemoteClient> deadClients = new ArrayList<RemoteClient>();
		
		for(RemoteClient c: clients)
		{
			try {
				c.sendMessage(m);
			} catch (IOException e) {
				deadClients.add(c);
			}
		}
		for(RemoteClient c: deadClients)
			clients.remove(c);
	}
}
