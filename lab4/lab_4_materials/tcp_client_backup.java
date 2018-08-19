import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class tcp_client
{
	static int lastAck = 0;

	static int cwndSize = 1;
	static Queue<Long> packetTimeouts = new LinkedList<Long>();
	
	static boolean slowStart = true;
	
	// Used by AckListener to update latest ACK#
	public static void setAckNum(int ackNum)
	{
		if(ackNum > lastAck) lastAck = ackNum;
		packetTimeouts.remove(); // Pop first item
		if(slowStart) cwndSize += 1;
		else cwndSize += 1 / cwndSize;
		//System.out.println("Setting last ACK to " + lastAck);
	}
	
	// Used by AckListener to indicate it is finished
	static boolean finished = false;
	
	public static void finish()
	{
		finished = true;
	}
	
	// Main function
	public static void main(String[] args)
	{
		// Open the network socket
		String host = "localhost";
		int port = 9876;
		Socket socket = null;
		try
		{
			socket = new Socket(host, port);
		}
		catch(Exception e)
		{
			System.out.println("Error at socket creation:");
			e.printStackTrace();
		}
		
		// Create input and output streams
		//BufferedReader socketInput = null; // Input from the server
		DataOutputStream socketOutput = null; // From client to server 
		Scanner terminalInput = new Scanner(System.in); // Scan a number from user input
		
		try
		{
			//socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketOutput = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			System.out.println("Error at data output stream creation:");
			e.printStackTrace();
		}
		
		// Read number of packets, probError
		int numberOfPackets = terminalInput.nextInt();
		System.out.println("Number of packets: " + numberOfPackets);
		
		// Send number of packets to server
		try
		{
			socketOutput.write(numberOfPackets);
		}
		catch (IOException e1)
		{
			System.out.println("Error at writing initial data to socket:");
			e1.printStackTrace();
		}
		
		// Variables for TCP control and packet window
		int ssthresh = 8;
		int EstimatedRTT = 1000; // Milliseconds
		int DevRTT = 0; // Milliseconds
		int TimeOut = EstimatedRTT + 4 * DevRTT + 500; // Milliseconds
		long currentTime = 0;
		int startOfCwnd = 1;
		int currentPacket = 1;
		
		// Create thread for receiving ACKs
		Thread thread = new Thread(new AckListener(socket, numberOfPackets));
		thread.start();
		
		// Process all packets
		while(true)
		{
			// Check if we can send another packet
			try
			{
				// Update window position based on ACKs received
				startOfCwnd = lastAck;
				System.out.print(""); // Required to make it work for some reason
				
				// Check if we passed the threshold
				if(cwndSize >= ssthresh) slowStart = false;
				else slowStart = true;
				
				// If we can send more packets, do so
				//System.out.println("currentPacket " + currentPacket + " < " + "startOfCwnd " + startOfCwnd + " + 4");
				if(currentPacket <= startOfCwnd + cwndSize && currentPacket <= numberOfPackets)
				{
					System.out.println("Sending packet #" + currentPacket + " \t | window size: " + cwndSize);
					socketOutput.write(currentPacket);
					currentPacket++;
					
					// Set timeout
					Long currentTimeout = System.currentTimeMillis();
					packetTimeouts.add(currentTimeout);
				}
				
				// Check for a timeout
				currentTime = System.currentTimeMillis();
				if(packetTimeouts.size() > 0 && currentTime - packetTimeouts.peek() > TimeOut)
				{
					// Reset to start of window
					currentPacket = startOfCwnd;
					ssthresh /= 2;
					packetTimeouts.clear();
					cwndSize = 1;
					System.out.println("Timeout occurred, setting current packet to " + currentPacket);
				}
			}
			catch(IOException e)
			{
				System.out.println("Error at writing array data to socket:");
				e.printStackTrace();
				break;
			}
			
			// Check if we are done sending all packets
			if(currentPacket > numberOfPackets && lastAck >= numberOfPackets)
			{
				System.out.println("Done writing all data, exiting");
				break;
			}
		}
		
		// Wait until other threads are done
		System.out.println("Finishing main thread, waiting for ACK listener thread to finish");
		while(!finished)
		{
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
				e.printStackTrace();
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
			System.out.println("Error at closing connections:");
			e.printStackTrace();
		}
		
		System.out.println("All done");
	}
}
