package mavericksoft.clinicmate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * The PersonController class directly interacts with Clinicmate's HealthProfessional
 * and Patient database and allows for the creation and deletion of patients, 
 * nurses, and doctors. A patient can also be added to a doctor/nurse's
 * patient list so they may be allowed to access the patient information.
 * 
 * PersonController interacts with Patient, HealthProfessional, Permissions and PermissionsController.
 * 
 * @author Amy Baldwin
 */

public class PersonController 
{
	private static PersonController instance;
	
	public static PersonController getInstance()
	{
		if(instance == null)
		{
			instance = new PersonController();
			return instance;
		}
		else
			return instance;
	}
	
	// Create a new Patient object with specified parameters by calling constructor of Patient class
	public Patient createPatient(String username, String firstName, String lastName, String password, String gender, String address, String insuranceProvider, String primaryPhone, String secondaryPhone, Date dob)
	{
		boolean created = false;
                Patient newPatient=null;
		// Check if current user is granted access to create a new patient record
		if((PermissionsController.getInstance()).currentUserPermissions()==Permissions.ADMIN || (PermissionsController.getInstance()).currentUserPermissions()==Permissions.NURSE)
		{
			newPatient = new Patient(username, firstName, lastName, password, gender, address, insuranceProvider, primaryPhone, secondaryPhone, dob);
			created = newPatient.save();
		}
                return newPatient;
		//return created;
	}
	
	// Create new Doctor or Nurse by calling constructor of HealthProfessional class
	public boolean createNurseOrDoctor(String username, String firstName, String lastName, String password, boolean admin, boolean nurse, boolean doctor)
	{
		boolean created = false;
		// Allow Nurse or Doctor to be created if current user logged in is an Admin
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.ADMIN)
		{
			HealthProfessional healthprof = new HealthProfessional(username, firstName, lastName, password, admin, nurse, doctor);
			created = healthprof.save();
		}
		return created;
	}
	
	// Find HealthProfessional using static getById method in HealthProfessional 
	// class and add patient to the HealthProfessional's patient list if they exist
	public boolean addPatient(UUID employeeID, UUID patientID) throws NonexistentRecordException
	{
		boolean added = false;
		
		// Allow a Doctor to add a Patient to an employee's list
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
		{
			HealthProfessional hp = HealthProfessional.getById(employeeID);
			if(hp.exists())
			{
				added = hp.addPatient(patientID);
			}
		}
		return added;
	}
	
	// Allow a Nurse or Doctor to delete a Patient from database if they have
	// access to the specified Patient.
	// Return true if successfully deleted, false otherwise.
	public boolean deletePatient(UUID patientID) throws NonexistentRecordException
	{
		boolean deleted = false;
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
		{
			/*
			 * Check if patient is on the current user's patient list here.
			 */
			HealthProfessional currentProfessional = (HealthProfessional) PermissionsController.getInstance().getCurrentUser();
			if(currentProfessional.getPatientIDs().contains(patientID))
			{
				Patient patient = Patient.getById(patientID);
				if(patient.exists())
				{
					deleted = patient.delete();
				}
			}
		}
		return deleted;
	}
	
	// Allow an Admin to delete a specified employee from the database using delete()
	// function in HealthProfessional class. 
	// Return true if successfully deleted, false otherwise.
	public boolean deleteEmployee(UUID employeeID) throws NonexistentRecordException
	{
		boolean deleted = false;
		// Check if current permissions are set to ADMIN
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.ADMIN)
		{
			HealthProfessional hp = HealthProfessional.getById(employeeID);
			if(hp.exists())
			{
				deleted = hp.delete();
			}
		}
		return deleted;
	}

    /**
     * This method gets all of the Doctors from the database.
     *
     * @return An array of HealthProfessionals representing the doctors in the
     *         system. If there are no doctors in the database, or an error
     *         occurs, it returns an empty array.
     */
    public HealthProfessional[] getAllDoctors() {
        try {
            return HealthProfessional.getAllDoctors();
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
            System.out.println("Failed to get all of the doctors.");
            return new HealthProfessional[0];
        }
    }
}
