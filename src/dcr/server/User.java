package dcr.server;

/**
 * Contains the information for a user in the system
 * In version 1.0, users are destroyed when they disconnect
 * @author Jared Dembrun
 */
public class User 
{
	private String username;
	private long userID;
	
	public User(String name, long id) 
	{
		username = name;
		userID = id;
	}

}
