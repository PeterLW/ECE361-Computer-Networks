import java.io.*;
import java.net.Socket;

public class Listener implements Runnable {
	
	//BufferedReader socketInput = null; // Input from the server
	public: 
	Socket socket;
	int pkts;
	
	public Listener() {
		this.socket = null;
		this.pkts = 0;
	}
	public Listener(Socket socket_, int pkts_) {
		this.socket = socket_;
		this.pkts = pkts_;
	}

	@Override
	public void run() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String acknowledge;
		int ack_no;
		while(true) {
			
			try {
				
				// If the server has sent a message, read it
				if(reader.ready()) {
					
					//these commented out lines didn't work with testing lab4, not sure if because they sent int instead of string?
					acknowledge = reader.readLine();
					ack_no = Integer.parseInt(ack);
					System.out.println("Received ACK #: " + ack_no);
					//Client.setAckNum(ack_num);
					if(ack_no > Client.lastAck) Client.lastAck = ack_no; 
					// Check if done
					if(ack_no == pkts) {
						
						//Client.finish();
						Client.end = true;
						break;
					}
				}
			}
			catch(IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}