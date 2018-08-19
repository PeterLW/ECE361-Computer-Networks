import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
public class Client {
	public static void main(String[] args)
	{
		// Open the network socket
		String host = "localhost";
		int port = 9877;
		Socket socket = null;
		try
		{
			socket = new Socket(host, port);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// Create a input and output streams
		BufferedReader socketInput = null; // Input from the server
		DataOutputStream socketOutput = null; // From client to server 
		Scanner terminalInput = new Scanner(System.in); // Scan a number from user input
		try
		{
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketOutput = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		// Read number of packets
		int numberOfPackets = terminalInput.nextInt(); 
		//Sending number of packets to the server
		
		try {
			socketOutput.write(numberOfPackets);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		// Keep processing input and output
		int sent = 1;
		while(true)
		{
			try
			{
				socketOutput.write(sent);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			try {
				while(!socketInput.ready());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try
			{
				if(socketInput.ready())
				{
					int ackFromServer = socketInput.read();
					if(ackFromServer == sent)
					{
						sent++;
						System.out.println(sent);
					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			if(sent > numberOfPackets)
			{
				break;
			}
		}
		
		// Close the socket
		try
		{
			Thread.sleep(500);
			socket.close();
			terminalInput.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
