import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class Client {
		
	public static void adjacent_matrix(double[][] matrix, List<Node> nodes, int length) {
		
		for(int i = 0; i < length; i++) {
			nodes.get(i).neighbors = new Edge[length];
			for(int j = 0; j < length; j++) {
				nodes.get(i).neighbors[j] = new Edge(nodes.get(j), matrix[i][j]);
			}
		}
	}
	
	public static void get_path(Node node_) {
		
		node_.set_minDistance(0);
		PriorityQueue<Node> nodes = new PriorityQueue<Node>();//NodeQueue
		nodes.add(node_);
		//修改>0
		while(nodes.size() > 0) {
			
			Node sourceNode = nodes.poll();
			
			for(int i = 0; i < sourceNode.neighbors.length; i++) {
				
				Node destination = sourceNode.neighbors[i].get_destination();
				double distance = sourceNode.get_minDistance() + sourceNode.neighbors[i].get_cost();
				
				if(distance < destination.get_minDistance()) {
					
					nodes.remove(destination);
					destination.set_minDistance(distance);
					destination.set_previous(sourceNode);
					nodes.add(destination);
				}
			}
		}
	}
	
	public static List<Integer> shortest_path_to(Node source, Node destination) {
		
		List<Integer> path = new ArrayList<Integer>();
		List<Integer> trace_path = new ArrayList<Integer>();
		path.add(destination.get_name());
		Node prev_node;
		
		if(destination.get_name() != source.get_name()) 
			prev_node = destination.get_previous_node();
		else 
			prev_node = null;
		
		while(prev_node != null) {
			
			if(prev_node.get_name() == source.get_name()) {
				
				path.add(prev_node.get_name());
				break;
			}
			path.add(prev_node.get_name());
			prev_node = prev_node.get_previous_node();
		}
		//把for loop用whileloop代替
		int ID=path.size() - 1;
		while(ID>=0)
		{
			trace_path.add(path.get(ID));
			ID--;
		}
	}

	public static double get_time(List<Node> nodes, List<Integer> path) 
	{
		double time=0;
		for(int a = 0; a < path.size()-1; a++) {		
			for(int b = 0; b < nodes.size(); b++) {	
				if(path.get(a) == nodes.get(b).get_name()) {		
					for(int c = 0; c < nodes.get(b).neighbors.length; c++) {			
					if(path.get(a+1) == nodes.get(b).neighbors[c].get_destination().get_name()) {
												
						time += nodes.get(b).neighbors[c].get_cost();
						break;
						}
					}
								break;
				}
			} 
		}
		return time;		
	}
	
	
	
	
	
	
	/**** THREAD STUFF ****/
	
	static int lastAck = 0; // Used by AckListener to update latest ACK#  lastAck
	static boolean end = false; // Used by AckListener to indicate it is finished	
	
	/*********************/
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		
		String current_mode;
		String host;
		int port_no;
		
		if(args.length <= 0) {
			
			current_mode = "client";
			host = "localhost";
			port = 9876;
		}
		else if(args.length == 1) {
			
			current_mode = args[0];
			host = "localhost";
			port = 9876;
		}
		//这个是用来干啥的？？在下面只有mode是client和server的情况，没有第三种,这个是client，为啥会有sever mode？？
		else if(args.length == 3) {
			
			current_mode = args[0];
			host = args[1];
			port = Integer.parseInt(args[2]);
		}
		else {
			
			return;
		}

		
		try {
			if(current_mode.equalsIgnoreCase("client")) {
				
				Socket socket = new Socket(host, port);
			}
			else if(current_mode.equalsIgnoreCase("server")) {
				
				ServerSocket serverSocket = new ServerSocket(port);
				socket = serverSocket.accept();
			}
			else {
				
				return;
			}
			
			System.out.println("Connected to : " + host + ":" + socket.getPort());
			
			//reader and writer:
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in)); //to get user input
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());	//for writing lines.
			
			
			/************************************** START BY IMPLEMENTING DIJKSTRA'S ********************************************/
			
			double travel_time = 0;//time_one_way
			//socket.isConnected()和socket.isClosed去掉也不会有影响吧？
			////while loop最后有个break，可以把之前的while用if代替吧？因为实际上没有循环
			if((socket != null)) {
				
				System.out.println("Enter number of nodes in the network from server ");
				String nodes_no_string = reader.readLine();
				int nodes_no = Integer.parseInt(nodes_no_string);	//noNodes		
				//The nodes are stored in a list, nodeList
				List<Node> nodes = new ArrayList<Node>();
						
				System.out.println(nodes_no + " nodes");
				System.out.println();
				// Send noNodes to the server, and read a String from it containing adjacency matrix
				//writer.write(noNodes);
				
				// Create an adjacent_matrix
				double[][] adjacent_matrix = new double[nodes_no][nodes_no];
				String line = reader.readLine();
				StringTokenizer str = new StringTokenizer(line);
				String cost ;
				
				System.out.println("ADJACENCY MATRIX");
				for (int i = 0; i < nodes_no; i++) {
				    
					// Use StringTokenizer to store the values read from the server in matrix					
					for(int j = 0; j < nodes_no; j++) {
						
						cost = str.nextToken();
						if(cost == "Infinity") adjacent_matrix[i][j] = Double.POSITIVE_INFINITY;
						else adjacent_matrix[i][j] = Double.parseDouble(next);
					}
				}
				
				for(int i = 0; i < nodes_no; i++) {
					for(int j = 0; j < nodes_no; j++) 
						System.out.print(adjacent_matrix[i][j] + " ");
					
					System.out.println();
				}
				System.out.println();
					

				//The nodes are stored in a list, nodeList
				for(int i = 0; i < nodes_no; i++) {
					
					nodes.add(new Node(i));
					nodes.get(i).set_previous(null);
				}
				
				adjacent_matrix(adjacent_matrix, nodes, adjacent_matrix.length); // Create edges from adjacency matrix
				
				//computePaths from node 0 only
				get_path(nodes.get(0));
				System.out.println("From Node 0");
					
				//getShortestPathTo
				for(int j = 0; j < nodes_no; j++){
						
					List<Integer> path = shortest_path_to(nodes.get(j), nodes.get(0));
					//提出来写成了一个function	
					double time = get_time(nodes, path);
					System.out.println("Total time to reach node " + j + ": " + time + " ms, Path: " + path);
					if(j == nodes_no - 1) {
						
						writer.writeBytes(path + "\r\n");
						travel_time = time;
					}
				}
				System.out.println();	
			}
			else
				return;
			/********************************************************************************************************************/
			
			
			
			
			/********************************************* NOW SEND THE PACKETS *************************************************/
			
			// Variables for TCP control and packet window
			double timeout = 2*travel_time + 50; //fixed timeout interval for simplicity  不是加200吗？？
			boolean slow_start = true;
			boolean drop = false;
			int cwnd = 1, RTT = 0, ssthresh = 128, startOfCwnd = 1, currentPacket = 1;
			long currentTime = 0, currentTimeOut = 0;
			long start_time, end_time;
			int seeker = 0; //can put this in listener section and make it global, with "..Location..": reset_point = 0;
			int numberOfPackets;
			BufferedInputStream file_reader = null;
			
			while(true) {
				
				// Wait for input from user with a file
				try {

					//request the file
					System.out.println("Please enter the file directory");
					String file_name = terminal.readLine();
					writer.writeBytes(file_name + "\r\n");
					if(file_name == "quit") {
						
						System.out.println("Quitting");
						break;
					}
					
					String current_directory = System.getProperty("user.dir");
					
					//FIGURE OUT DIRECTORY LATER??
					//file_reader = new BufferedReader(new FileReader(current_directory + "/src/" + file_name));
					//should just be able to do: = new FileInputStream(file_name);
					file_reader = new BufferedInputStream(new FileInputStream(current_directory + "/src/" + file_name));
					
					
					System.out.println("File exists, beginning transfer");
					
					//NEED TO GET FILE LENGTH
					//File file_name = File("JavaFileReader.java");
					//int fileLength = file_reader.length();
					File file = new File(current_directory + "/src/" + file_name);
					int fileLength = (int)file.length();
					System.out.println("Length of the file: " + String.valueOf(fileLength));
					
					numberOfPackets = (int)java.lang.Math.ceil(((double)fileLength)/1000);
					writer.writeBytes(String.valueOf(numberOfPackets) + "\r\n");
					System.out.println("Number of Packets: " + String.valueOf(numberOfPackets));
					
					
					//create thread for receiving ACKs
					Thread thread = new Thread(new Listener(socket, numberOfPackets));
					thread.start();
					start_time = System.currentTimeMillis();	
					
					// Process all packets
					while(true) {
						
						// Check if we can send another packet
						try {
							
							//update cwnd
							startOfCwnd = lastAck + 1;
							file_reader.mark(startOfCwnd*1000);
							
							//check if we passed the threshold
							if(cwnd >= ssthresh) slow_start = false;
							else slow_start = true;
							
							// Send all packets possible
							for(int i = currentPacket; i <= Math.min(startOfCwnd + cwnd - 1, numberOfPackets); i++, currentPacket++) {
								
								System.out.println("Sending packet #" + currentPacket); 
								
								try {
															
									byte[] bytes = new byte[1004]; 
									bytes[0] = (byte) (currentPacket >> 24);
									bytes[1] = (byte) (currentPacket >> 16);
									bytes[2] = (byte) (currentPacket >> 8);
									bytes[3] = (byte) (currentPacket);

									//only reading in 1000 bytes, because 4 bytes were appended to head
									int bytesRead = file_reader.read(bytes, 4, 1000);
									
									//update current location in file
									if(!drop){
										
										seeker += bytesRead;
									}else{
										//reset seeker back to original position
										file_reader.reset();
										seeker = startOfCwnd*1000;
										drop = false;
									}
									//send to server
									writer.write(bytes);
									writer.writeBytes("\r\n");
									
									//can't send more, wait to see if the ACKs come in
									if(seeker == fileLength) break;
								}
								catch (Exception e) {
								
									e.printStackTrace();
								}
								//if(lastAck == currentPacket) continue;
							}
							currentTimeOut = System.currentTimeMillis();
							currentTime = System.currentTimeMillis();
							
							// Wait for timeout or all ACKs
							while(((currentTime - currentTimeOut) < timeout) && ((lastAck + 1) < Math.min(startOfCwnd + cwnd, numberOfPackets))) {
								
								currentTime = System.currentTimeMillis();
							}
							
							// Check for a timeout
							if((currentTime - currentTimeOut) >= timeout) {
								
								//reset to start of window
								System.out.println("Timeout for packet #" + startOfCwnd + ". Restarting from " + (lastAck + 1));
								startOfCwnd = lastAck + 1; //isn't this the same value?
								currentPacket = startOfCwnd;
								ssthresh = ssthresh/2;
								cwnd = 1;
								file_reader.mark(startOfCwnd*1000);
								drop = true;
								RTT++;
							}
							
							// Check for all ACKs
							if((lastAck + 1) >= (startOfCwnd + cwnd)) {
								
								if(slow_start) cwnd = cwnd*2;
								else cwnd += 1;
								RTT++;
							}
						}
						catch(Exception e) {
							
							e.printStackTrace();
							break;
						}
						
						// Check if we are done sending all packets
						if((currentPacket > numberOfPackets) && (lastAck >= numberOfPackets)) {
							
							System.out.println("Done sending all data.");
							break;
						}
					}
					
					// Wait until other thread is done
					System.out.println("Finishing main thread, waiting for ACK listener thread to finish");
					while(!finished) {
						
						try {Thread.sleep(500);}
						catch(Exception e) {e.printStackTrace();}
					}
					end_time = System.currentTimeMillis();
					
					
					System.out.println("Total time to send all packets: " + (end_time - start_time));
					System.out.println("Total time in terms of RTT: " + RTT);
					
					file_reader.close();
				}
				catch (Exception e) {
			
					e.printStackTrace();
				}		
			}
			
			/********************************************************************************************************************/
			
			System.out.println("Quit");
			socket.close();
		} 
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}