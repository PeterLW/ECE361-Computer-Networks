//client
import java.net.*;

import java.io.*;

public class Lab1_practical3 {


    public static void main(String[] args) throws UnknownHostException, IOException {

        // TODO Auto-generated method stub

        System.out.println("Hello World");

        try{           

            Socket socket = new Socket("localhost",10003);
            BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            String str = socket_reader.readLine();
            System.out.println(str);
            while(true){

            str = reader.readLine();           
            long time1 = System.currentTimeMillis();
            writer.writeBytes(str + "\r\n");
            long time2 = System.currentTimeMillis();
            time1 = time2 - time1; 
            System.out.println("Time: " + time1 + "ms.");
            str = socket_reader.readLine();
			
            if(str.equalsIgnoreCase("quit"))
            {   
				break;
			}
            System.out.println("Server: " + str);
            }
			
            socket.close();
        }catch(Exception e){e.getStackTrace();}

    }

}

/*

//server
import java.net.*;

import java.io.*;

public class Server{


    public static void main(String[] args) throws UnknownHostException, IOException {

        try{

            System.out.println("1");    

            ServerSocket serverSocket = new ServerSocket(10003);

            System.out.println("2");
            
            Socket socket = serverSocket.accept();

            System.out.println("3");
        
            BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            

            String welcomeMessage = "Welcome!";

            writer.writeBytes(welcomeMessage + "\r\n");

            while(true){                

                String str1 = socket_reader.readLine();

                if(str1.equalsIgnoreCase("quit"))

                {
                    break;
                    }

                System.out.println("client: " + str1);

                String str= reader.readLine();

                System.out.println(str);

                //str = reader.readLine();

                writer.writeBytes(str + "\r\n");
            }

            serverSocket.close();

            socket.close();

        }catch(Exception e){e.getStackTrace();}

    }

}

*/
