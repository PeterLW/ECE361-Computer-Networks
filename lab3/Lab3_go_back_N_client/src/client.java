import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
public class client {
	static int lastAck = 0;
	
	public static void setAckNum(int ackNum)
	{
		lastAck = ackNum;
	}
	
	static boolean finished = false;
	
	public static void finish()
	{
		finished = true;
	}
	
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
			System.out.println("Error at data output stream creation:");
			e.printStackTrace();
		}
		
		// Read number of packets, probError
		int numberOfPackets = terminalInput.nextInt(); 
		int probError = terminalInput.nextInt(); 
		System.out.println("Number of packets: " + numberOfPackets + " | Probability of Error: " + probError);
		
		//Sending number of packets to the server
		try
		{
			socketOutput.write(numberOfPackets);
			socketOutput.write(probError);
		}
		catch (IOException e1)
		{
			System.out.println("Error at writing initial data to socket:");
			e1.printStackTrace();
		}
		
		//Declaring an array to store data and make a windowSize, last index client sends
		int array[] = new int[numberOfPackets];
		for(int i = 0; i < numberOfPackets; i++) array[i] = i + 1;
		int windowSize = 4;
		int currentIndex = 0;
		int sent = 1;
		
		// Create thread for receiving acks
		Thread thread = new Thread(new AckListener(socket, numberOfPackets));
		thread.start();
		
		// Keep processing output
		while(true)
		{
			try
			{
				currentIndex = lastAck;
				
				if(sent <= currentIndex + windowSize)
				{
					System.out.println("Sending packet #" + sent + ", data " + array[sent - 1] +
							" \t | window: " + currentIndex + " - " + (currentIndex + windowSize));
					socketOutput.write(array[sent - 1]);
					sent++;
				}
				
			}
			catch(IOException e)
			{
				System.out.println("Error at writing array data to socket:");
				e.printStackTrace();
				break;
			}
			
			if(sent > numberOfPackets)
			{
				System.out.println("Done writing all data, exiting");
				break;
			}
		}
		
		// Wait until other threads are done
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
	}

}
