package dcr.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Jared Dembrun
 * Main Client object for DembrunChatRoom project
 * 
 * A graphical interface for the Dembrun Chat Room.
 * Currently only receives input data for initial connection
 */
public class ClientMain
{
	/**
	 * Declare Variables for this class
	 * frame - the JFrame which appears on startup, used to connect
	 * chatRoom - the JFrame subclass which appears after connection, used for interaction
	 * addressLabel - the JLabel that says "Server Address:"
	 * usernameLabel - the JLabel that says "Desired Username:"
	 * addressField - the JTextField for the server address
	 * usernameField - the JTextField for the desired username 
	 * submit - the JButton for submitting this data
	 * addedWarning - boolean to determine if warning has been added
	 */
	private static final JFrame frame = new JFrame("DembrunChatRoom Client Setup");
	private static final JLabel addressLabel = new JLabel("Server Address:");
	private static final JLabel usernameLabel = new JLabel("Desired Username:");
	private static final JTextField addressField = new JTextField(15);
	private static final JTextField usernameField = new JTextField(15);
	private static final JButton submit = new JButton("Connect to Server");
	private static boolean addedWarning = false;

	
	/**
	 * @param args
	 * Initialize a chat room client with a JFrame
	 * Upon receiving both a username and a server address,
	 * connects to server and opens a new JFrame.
	 * Then, close the first JFrame
	 */
	public static void main(String[] args) 
	{
		//Set content pane to gridlayout on a JPanel
		frame.setContentPane(new JPanel(new GridLayout(4, 2, 0, 10)));
		
		//Add the components to the initial JFrame
		frame.getContentPane().add(addressLabel);
		frame.getContentPane().add(addressField);
		frame.getContentPane().add(usernameLabel);
		frame.getContentPane().add(usernameField);
		
		//Add a spacer label and the button
		frame.getContentPane().add(new JLabel(""));
		frame.getContentPane().add(submit);
		
		//Add an ActionListener
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{				
				//Get data
				String addr = addressField.getText();
				String user = usernameField.getText();
				
				//Check both are not default state
				if(!addr.trim().equals("") && !user.trim().equals(""))
				{
					connect(addr, user);
				}
				else if(!addedWarning) //Notify user they must not be null
				{
					frame.getContentPane().add(new JLabel("Please enter username and address"));
					frame.pack();
					addedWarning = true;
				}
			}
		});
		
		//Final frame setup
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
	
	/**
	 * 
	 * @param addr - server address
	 * @param user - desired username
	 * 
	 * Creates a new Chatroom object, which handles its own JFrame
	 * Closes the JFrame in this class once finished
	 */
	public static void connect(String addr, String user)
	{
		new ChatRoom(addr, user);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
