package dcr.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The main server class for the DembrunChatRoom
 * @author Jared Dembrun
 */
public class ServerMain 
{
	private static final int PORT = 2025;
	private static final List<PrintWriter> users = new ArrayList();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
	    ServerSocket listener = new ServerSocket(PORT);
	    try 
	    {
	        while (true) 
	        {
	            new Interface(listener.accept()).start();
	        }
	    } finally 
	    {
	        listener.close();
	    }
	}
	/********************Private Classes***********************/
	
	/**
	 * Interacts with the client on behalf of the server
	 * @author Jared Dembrun
	 */
	private static class Interface extends Thread 
    {
        private Socket socket;
        private String username;

        public Interface(Socket socket) 
        {
            this.socket = socket;
        }

        /**
         * Services this thread's client by receiving a username
         * from the client, storing it, and communicating with the 
         * client with messages from then on out
         */
        public void run() 
        {
            try 
            {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                username = in.readLine();
                users.add(out);
                
                while (true) 
                {
                    String input = in.readLine();
                    
                    //Send user message to all users if not null
                    if(input != null)
                    {
	                    for(PrintWriter pw : users)
	                    {
	                    	pw.println(username + ": " + input);
	                    }
                    }
                }
                
            } catch (IOException e) 
            {
                System.out.println("Error handling client");
            } finally 
            {
                try 
                {
                    socket.close();
                } catch (IOException e) 
                {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
            }
        }
    }
}
