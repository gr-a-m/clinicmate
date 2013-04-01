package mavericksoft.clinicmate;
/**
 * The PermissionsController class manages permissions of
 * doctors, nurses, patients and administrators that are 
 * currently logged into the Clinicmate system. There is a
 * login function that checks if a username and password 
 * combination is valid. If so, it updates the current user
 * so the current user's permissions can be verified for
 * use in other classes.
 * 
 * @author Amy Baldwin
 *
 */


public class PermissionsController 
{
	private static Permissions permission;
	private HealthProfessional hp;
	private Patient p;
	private Person currentUser;
	private static PermissionsController instance;
	
	public static PermissionsController getInstance()
	{
		if(instance == null)
		{
			instance = new PermissionsController();
			return instance;
		}
		else
			return instance;
	}

	// return a true if a username and password combination results in a successful login
	public boolean logon(String userName, String password) throws NonexistentRecordException
	{
		boolean logged = false;
		
		//if username and password pair is valid, set logged to true and set current user's permissions
		if((PersonController.getInstance()).checkPassword(userName, password))
		{
			logged = true;
			
			// set current permissions of user logged in
			permission = Person.getPermissions(userName);
		}
		
		return logged;
		
	}
	
	// Return permissions of the current user logged in. Permissions were set when the user logged in.
	public Permissions currentUserPermissions()
	{
		return permission;
	}
}
