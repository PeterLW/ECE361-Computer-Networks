import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class AckListener implements Runnable
{
	BufferedReader socketInput = null; // Input from the server
	int numberOfPackets = 0;
	
	// Constructor, initializing socket
	public AckListener(Socket socket, int nPackets)
	{
		System.out.println("Starting ack listener thread");
		numberOfPackets = nPackets;
		
		try
		{
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		// Keep reading ACKs until done
		while(true)
		{
			try
			{
				// If the server has sent a message, read it
				if(socketInput.ready())
				{
					// Read the message
					int response = socketInput.read();
					System.out.println("Received reply from server: " + response);
					tcp_client.setAckNum(response);
					
					// Check if done
					if(response == numberOfPackets)
					{
						tcp_client.finish();
						break;
					}
				}
			}
			catch(IOException e)
			{
				System.out.println("Error on ACK listener:");
				e.printStackTrace();
			}
		}
		
		System.out.println("Finishing ACK listener thread");
	}
}
