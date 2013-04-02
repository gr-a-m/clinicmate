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
 *
 */

public class PersonController 
{
	private HealthProfessional hp;
	private Patient patient;
	private Permissions permission;
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
	public boolean createPatient(String username, String firstName, String lastName, String password, String gender, String address, String insuranceProvider, String primaryPhone, String secondaryPhone, Date dob)
	{
		boolean created = false;
		// Check if current user is granted access to create a new patient record
		if((PermissionsController.getInstance()).currentUserPermissions()==Permissions.ADMIN || (PermissionsController.getInstance()).currentUserPermissions()==Permissions.DOCTOR)
		{
			Patient newPatient = new Patient(username, firstName, lastName, password, gender, address, insuranceProvider, primaryPhone, secondaryPhone, dob);
			created = newPatient.save();
		}
		return created;
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
		
		// Allow an Admin or Doctor to add a Patient to an employee's list
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.ADMIN || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
		{
			hp = HealthProfessional.getById(employeeID);
			if(hp.exists())
			{
				added = hp.addPatient(patientID);
			}
		}
		return added;
	}
	
	// Allow a Nurse or Doctor to delete a Patient from database if they have
	// access to the specified Patient.
	public boolean deletePatient(UUID patientID) throws NonexistentRecordException
	{
		boolean deleted = false;
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
		{
			/*
			 * Must check if patient is on the current user's patient list here. Need to implement getAccessList() method in Person to determine 
			 * which patient IDs the current Person object may access.
			 */
			patient = Patient.getById(patientID);
			if(patient.exists())
			{
				deleted = patient.delete();
			}
		}
		return deleted;
	}
	
	// Allow an Admin to delete a specified employee from the database using delete() function
	// in HealthProfessional class. 
	public boolean deleteEmployee(UUID employeeID) throws NonexistentRecordException
	{
		boolean deleted = false;
		// Check if current permissions are set to ADMIN
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.ADMIN)
		{
			hp = HealthProfessional.getById(employeeID);
			if(hp.exists())
			{
				deleted = hp.delete();
			}
		}
		return deleted;
	}
	
	// Check if a username and password pair is valid. Return true if valid, false otherwise.
	public boolean checkPassword(String userName, String password) throws NonexistentRecordException
	{
		boolean valid = false;
		permission = Person.getPermissions(userName);
		if(permission == Permissions.ADMIN || permission == Permissions.DOCTOR || permission == Permissions.NURSE)
		{
			hp = HealthProfessional.getByUsername(userName);
			valid = hp.checkPassword(password);
		}
		else if(permission == Permissions.PATIENT)
		{
			patient = Patient.getByUsername(userName);
			valid = patient.checkPassword(password);
		}
		
		return valid;
	}
}
