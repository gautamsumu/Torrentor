import java.net.*;
import java.io.*;

public class MultiServer{

	public static void main(String[] args) throws IOException{
		ServerSocket serverSocket = null;
		boolean listening = true;

		try{
			serverSocket = new ServerSocket(4444);
		}catch (IOException e){
			System.err.println("Could not listen on port: 4444.");
			System.exit(-1);
		}
		
		System.out.println("Waiting for first Client...");

		while (listening)
			new MultiServerThread(serverSocket.accept()).start();

		serverSocket.close();
	}
}
