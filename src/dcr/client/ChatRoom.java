package dcr.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatRoom 
{
	/**
	 * Declare variables for this class
	 * frame - the JFrame for the chatroom
	 * in - input socket stream (write out to the server)
	 * out - output socket stream (read in from the server)
	 * messages - the JTextArea for chat messages
	 * comment - the JTextField for users to type their comments for submission
	 * send - the JButton for sending a comment
	 * PORT - the port to connect to
	 */
	private JFrame frame;
	private BufferedReader in;
    private PrintWriter out;
    private final String user;
	private final JTextArea messages = new JTextArea(35, 23);
	private final JTextField comment = new JTextField(20);
	private final JButton send = new JButton("Send");
	private final int PORT = 2025;

	/**
	 * Constructor for ChatRoom object
	 * @param addr - the address of the chat server
	 * @param user - the desired username
	 */
	public ChatRoom(String addr, String pass, String user) 
	{
		//Set user
		this.user = user;
		//Connect to server
		connect(addr, pass);
		
		//Build the JFrame
		frameSetupAndBuild();
		
		//Add action listeners
		comment.addActionListener(new ClientListener());
		send.addActionListener(new ClientListener());
		
		//Greet user and resolve username conflicts
		
		//Begin the main portion of the application
		new ServerListener();		
	}
	
	/************Private Helper Methods*****************************/
	
	/**
	 * Makes a connection to the remote server specified
	 * Prints an error message to stdout and exits if exception caught
	 * @param addr - the remote address to connect to
	 */
	private void connect(String addr, String pass)
	{
		Socket socket;
		try 
		{
			socket = new Socket(addr, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) 
		{
			System.out.println("Error connecting to server.");
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
		out.println(pass);
		out.println(user);
        
	}
	
	/**
	 * Helper method to setup and build the JFrame
	 */
	private void frameSetupAndBuild()
	{
		//Set Field values
		JScrollPane scroll = setFieldValues();
		
		//Create Grid Bag Constraints
		GridBagConstraints messageConstraints = new GridBagConstraints();
		messageConstraints.gridx = 0;
		messageConstraints.gridy = 0;
		messageConstraints.gridwidth = 2;
		GridBagConstraints commentConstraints = new GridBagConstraints();
		commentConstraints.gridx = 0;
		commentConstraints.gridy = 1;
		GridBagConstraints sendConstraints = new GridBagConstraints();
		sendConstraints.gridx = 1;
		sendConstraints.gridy = 1;
		
		//Create frame
		buildFrame(messageConstraints, commentConstraints, sendConstraints, scroll);
		
	}
	
	/**
	 * Set layout component values
	 * @return a JScrollPane to server as the message display area properly configured
	 */
	private JScrollPane setFieldValues()
	{
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scroll;
	}
	
	/**
	 * Add the components to the frame, pack it, and set it to visible
	 * @param messageConstraints - Constraints for the textarea serving as message display
	 * @param commentConstraints - Constraints for the area where a user writes a message
	 * @param sendConstraints - constraints for the button to send a message
	 * @param scroll - the scrollable version of the message display area
	 */
	private void buildFrame(GridBagConstraints messageConstraints,
			GridBagConstraints commentConstraints, GridBagConstraints sendConstraints,
			JScrollPane scroll)
	{
		frame = new JFrame("Dembrun Chat Room");
		frame.setContentPane(new JPanel(new GridBagLayout()));
		frame.getContentPane().add(scroll, messageConstraints);
		frame.getContentPane().add(comment, commentConstraints);
		frame.getContentPane().add(send, sendConstraints);
		
		//Final frame setup
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
	
	/**************************Private Classes**************************/
	
	/**
	 * Sends data to the remote server
	 * To be attached to comment and send variables
	 * @author Jared Dembrun
	 */
	private class ClientListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//Send the message to the server if not null
			String data = comment.getText();
			if(data != null && !data.trim().equals(""))
			{
				out.println(data);
				comment.setText("");
			}
		}
	}
	
	/**
	 * Updates the message area when new messages are received from the server
	 * This must be run in the background
	 * @author Jared Dembrun
	 */
	private class ServerListener
	{
		public ServerListener()
		{
			Thread listen = new Thread()
			{
				public void run()
				{
					String response = "initial string";
					
					
					while(true)
					{
						try 
						{
							response = in.readLine() + "\n";
						} catch (IOException e) 
						{
							//Be silent until real data is received
							continue;
						}
						if(response.equals("Invalid password\n"))
						{
							break;
						}
						messages.append(response);
					}
					System.exit(-1);
				}
			};
			listen.start();
		}
	}

}
