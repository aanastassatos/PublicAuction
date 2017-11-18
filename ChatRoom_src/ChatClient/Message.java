package ChatClient;

import java.io.Serializable;
/**
 * Class for holding message details
 * For a class to be able to be sent over a socket is must be Serializable!
 * Also any other classes that are variables of this class must also be Serializable.
 * For example if I has a variable called metaData that was of class type Meta then that class 
 * would also need to implements Serializable.
 * More about serialization: https://docs.oracle.com/javase/tutorial/jndi/objects/serial.html
 */
public class Message implements Serializable{

	public String from;
	public String msg;
	public long timestamp;
	
	public Message(String from, String msg)
	{
		this.from = from;
		this.msg = msg;
		timestamp = System.currentTimeMillis();
	}
}
