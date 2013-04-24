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
	private Permissions permission;
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

	// Return a true if a username and password combination results in a successful login. Set current user and permissions.
	public boolean logon(String userName, String password) throws NonexistentRecordException
	{
		boolean logged = false;
		
		// Only allow a new user to log in if the previous user has logged out.
		//if(currentUser == null && permission == null)
		//{
			//if username and password pair is valid, set logged to true and set current user and current user's permissions
	
			permission = Person.getPermissions(userName);
			if(permission == Permissions.ADMIN || permission == Permissions.DOCTOR || permission == Permissions.NURSE)
			{
				try
				{
					currentUser = HealthProfessional.getByUsername(userName);
					HealthProfessional hp = HealthProfessional.getByUsername(userName);
					logged = hp.checkPassword(password);
				}
				catch(NonexistentRecordException e)
				{
					return false;
				}
			}
			else if(permission == Permissions.PATIENT)
			{
				try
				{
					currentUser = Patient.getByUsername(userName);
					Patient patient = Patient.getByUsername(userName);
					logged = patient.checkPassword(password);
				}
				catch(NonexistentRecordException e)
				{
					return false;
				}
			}
		//} // end of logging new user in
		
		return logged;
		
	}
	
	
	// Log the current user out of the system by setting permissions and current user to NULL
	public void logout()
	{
		currentUser = null;
		permission = null;
	}
	
	// Return permissions of the current user logged in. Permissions were set when the user logged in.
	public Permissions currentUserPermissions()
	{
		return permission;
	}

    /**
     * This method gets the person currently logged into the system. It will
     * get the newest information about that person from the database to
     * ensure it is using the most current data.
     *
     * @return The Person currently logged in.
     */
	public Person getCurrentUser()
	{
        try {
            switch (currentUserPermissions()) {
                case PATIENT:
                    currentUser = Patient.getById(((Patient) currentUser).getPatientID());
                    break;
                case DOCTOR:
                case NURSE:
                case ADMIN:
                    currentUser = HealthProfessional.getById(((HealthProfessional) currentUser).getEmployeeID());
                    break;
                default:
                    currentUser = null;
                    break;
            }
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
            System.out.println("[ERROR]: Failed to get current user.");
            currentUser = null;
        }

        return currentUser;
	}
}
