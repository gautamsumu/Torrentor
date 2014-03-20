import java.io.*;
import java.net.*;

public class ReceiverSocket extends Thread{
	String serverIP;
	int serverPort;
	public String part;
	public byte[] bytes;
	long chunkSize;

	public ReceiverSocket(String IP, int port,int c){
		serverPort = port;
		serverIP = IP;
		chunkSize = c;
	}
	
	public void run(){
		try{
			System.out.println("Connecting to port "+ serverPort);
			Socket rSocket = new Socket(serverIP, serverPort);
			System.out.println("Connected to port "+ serverPort);
			
			InputStream in = rSocket.getInputStream();			
			
			bytes = new byte[(int)chunkSize];
			
			System.out.println("Receiving "+chunkSize+" bytes from server port "+serverPort);
			
			DataInputStream dIn = new DataInputStream(in);
			dIn.read(bytes);
			
			System.out.println("Received "+chunkSize+" bytes from port "+serverPort);
			
//			part = new String(bytes);
//			System.out.println(part);
			
			dIn.close();
			in.close();
			Client.socketsOpen--;
			rSocket.close();
		}
		catch(IOException e) {;}
	}
}			
