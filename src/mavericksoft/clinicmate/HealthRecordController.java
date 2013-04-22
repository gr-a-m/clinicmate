package mavericksoft.clinicmate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * The HealthRecordController class manages patient health
 * records that are stored in Clinicmate's database system.
 * This controller allows to create, update, or delete patient
 * records from the database based on a user's permissions.
 * 
 * HealthRecordController interacts with HealthRecord, NursePage,
 * DoctorPage, PatientPage, and PermissionsController
 * 
 * @author Amy Baldwin, Grant Marshall
 */

public class HealthRecordController 
{
	// The static instance object
    private static HealthRecordController instance;

    /**
     * This method returns the static instance of this class or initializes it
     * if it does not yet exist.
     *
     * @return The static instance of the class
     */
	public static HealthRecordController getInstance()
	{
		if(instance == null)
		{
			instance = new HealthRecordController();
			return instance;
		}
		else
			return instance;
	}

    /**
     * This method adds a comment to the provided record
     *
     * @param recordID The record to comment on
     * @param comment  The comment to add
     * @return         Whether the record was commented on successfully
     */
    public boolean addComment(UUID recordID, String comment)
    {
        boolean added = false;

        if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
        {
            HealthProfessional currentProfessional = (HealthProfessional) PermissionsController.getInstance().getCurrentUser();
            HealthRecord hr = HealthRecord.getById(recordID);
            if(currentProfessional.getPatientIDs().contains(hr.getPatientID()))
            {
                hr.addComment(comment);
                added = hr.save();
            }
        }

        return added;
    }
}
