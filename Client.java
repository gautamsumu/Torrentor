import java.io.*;
import java.net.*;

public class Client{
	public static int socketsOpen;
	public static void main(String[] args) throws IOException{

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String fileName = args[0];
		int chunks = Integer.parseInt(args[1]);
		String serverIP = args[2];
		int serverPort = Integer.parseInt(args[3]);
		long startTime, endTime;

		try{
			kkSocket = new Socket(serverIP,serverPort);
			
			System.out.println("Client IP: "+ InetAddress.getLocalHost().toString());
    	System.out.println("Required file is "+fileName+" in "+chunks+" chunks.");
			
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		}catch (UnknownHostException e){
			System.err.println("Don't know about host");
			System.exit(1);
		}catch (IOException e){
			System.err.println("Couldn't get I/O for the connection to the host");
			System.exit(1);
		}
		startTime = System.currentTimeMillis();
		System.out.println(in.readLine());
		out.println(fileName);
		out.println(chunks);
		
		System.out.println(in.readLine());
		
		int fileSize=Integer.parseInt(in.readLine());
		
		ReceiverSocket[] receive=new ReceiverSocket[chunks];
		socketsOpen=chunks;
		int i;
		for(i=0;i<chunks;i++){
			int port=Integer.parseInt(in.readLine());
			int chunkSize=Integer.parseInt(in.readLine());
			receive[i]=new ReceiverSocket(serverIP,port,chunkSize);
			receive[i].start();
		}
		
		while (socketsOpen > 0){}
		System.out.println("File Received.");
		endTime = System.currentTimeMillis();
		long totalTime= endTime - startTime;
		System.out.println("Downloaded in "+totalTime+ " milliseconds.");
    	System.out.println("Average Download Speed is " + fileSize*1000/(endTime - startTime) + " bytes/s.");
		
		System.out.print("Give file name : ");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fName=stdIn.readLine();
		File targetFile = new File(fName);
		if (targetFile.exists()) {
			System.out.println("Target file already exists. Delete target file.");
			System.exit(0);
		}
		BufferedOutputStream outFile = new BufferedOutputStream(new FileOutputStream(targetFile));		

		for(i=0;i<chunks;i++)
			outFile.write(receive[i].bytes);
		
		outFile.close();
		out.close();
		stdIn.close();
		in.close();
		kkSocket.close();
	}
}
