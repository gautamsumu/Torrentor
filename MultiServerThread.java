import java.net.*;
import java.io.*;
import java.util.*;

public class MultiServerThread extends Thread{
	private Socket socket = null;

	public MultiServerThread(Socket socket){	
		super("MultiServerThread");
		this.socket = socket;
	}

	private boolean isPortAvailable(int port){	
		boolean portAvailable = true;
		ServerSocket socket = null;
		try{
			socket = new ServerSocket(port);
		}catch(IOException e){
			portAvailable = false;
		}
		try{
			if(socket != null) socket.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return portAvailable;
	}

	private int getAvailablePort() throws IOException{	
		int checkPort = 0;
		Random randomGenerator = new Random();
		do{
			checkPort = randomGenerator.nextInt(20000) + 10000;
		}while (!isPortAvailable(checkPort));
		return checkPort;
   }

	public void run(){		
		try{
			System.out.println("Connection Accepted.");
			System.out.println("Connected to a client "+socket.getLocalAddress().toString());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println("Connection Accepted.");
			String fileName = in.readLine();
			int chunks = Integer.parseInt(in.readLine());
			System.out.println("File required : "+fileName+" in "+chunks+" chunks");
			File sourceFile = new File(fileName);
			if (!sourceFile.exists()) {
				out.println("Sorry, Required file does not exist. ");
				System.exit(0);
			}
			else out.println("Sending file... ");
			
			long totalSize = sourceFile.length();
			out.println(totalSize);
			
			long chunkSize = totalSize/chunks;
			int i;
			long start = 0,end;
			
			TransferSocket[] transfer = new TransferSocket[chunks];
			
			for(i = 0 ; i < chunks ; i++){
				end = totalSize-chunkSize*(chunks-1-i);
				long i_chunkSize=end-start;
				int port = getAvailablePort();
				System.out.println("Port "+port+" opened");
				out.println(port);
				out.println(i_chunkSize);
				transfer[i] = new TransferSocket(port,fileName,start,i_chunkSize);
				transfer[i].start();
				System.out.println("Sending data from "+start+" to "+end+" bytes.");
				start=end;
			}

			in.close();
			out.close();
			socket.close();

		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
