import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
public class server {
	public static void main(String[] args)
	{
		int port = 9877;
		ServerSocket serverSocket = null;
		Socket socket = null;
		try
		{
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// Create a input and output streams
		BufferedReader socketInput = null; // Input from the client
		DataOutputStream socketOutput = null; // From server to client 
		try
		{
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketOutput = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		//Declaring a variable to store number of packets
		int numberOfPackets = 0;
		try {
			numberOfPackets = socketInput.read();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		// Keep processing input and output
		int lastAck = 0;
		while(true)
		{
			// Waiting for the input
			try {
				while(!socketInput.ready());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try
			{
				if(socketInput.ready())
				{
					int numberFromClient = socketInput.read();
					if(numberFromClient == lastAck+1)
					{
						socketOutput.write(numberFromClient);
						lastAck++;
						System.out.println(lastAck);
					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}			
			
			if(lastAck >= numberOfPackets)
			{
				break;
			}
		}
		
		// Close the socket
		try
		{
			Thread.sleep(500);
			socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
