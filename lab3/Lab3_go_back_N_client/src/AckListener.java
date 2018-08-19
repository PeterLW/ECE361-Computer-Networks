import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class AckListener implements Runnable
{
	BufferedReader socketInput = null; // Input from the server
	int numberOfPackets = 0;
	
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
		// Keep reading acks until done
		while(true)
		{
			try
			{
				if(socketInput.ready())
				{
					int response = socketInput.read();
					System.out.println("Received reply from server: " + response);
					client.setAckNum(response);
					
					// Check if done
					if(response == numberOfPackets)
					{
						client.finish();
						break;
					}
				}
			}
			catch(IOException e)
			{
				System.out.println("Error on ack listener:");
				e.printStackTrace();
			}
		}
		
		System.out.println("Finishing ack listener thread");
	}
}
