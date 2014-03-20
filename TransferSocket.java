import java.io.*;
import java.net.*;


public class TransferSocket extends Thread{
	int port;
	String fileName;
	long start;
	long chunkSize;
	
	public TransferSocket(int p, String file, long s, long c){
		port = p;
		fileName = file;		
		start = s;
		chunkSize = c;		
	}

	public void run(){
		ServerSocket tSocket = null;
		Socket tClient = null;

		try{
			tSocket = new ServerSocket(port);
			tClient = tSocket.accept();
			System.out.println("Connected to client "+tClient.getLocalAddress().toString()+" at port number " + port); 
			
			byte[] bytes = new byte[(int)chunkSize];
			File file = new File(fileName);
			FileInputStream in = new FileInputStream(file);
			in.skip(start);
			in.read(bytes);			
			
			System.out.println("Port : "+port+" Transfering "+chunkSize+" bytes");
			
			DataOutputStream dOut = new DataOutputStream(tClient.getOutputStream());
			dOut.write(bytes);
			
//			String check=new String(bytes);
//			System.out.println(check);
			
			dOut.close();
			in.close();		
			tClient.close();
			tSocket.close();
			System.out.println("Transmission completed at port number "+ port);
		}
		catch(IOException e) {;}
	}
}
		
