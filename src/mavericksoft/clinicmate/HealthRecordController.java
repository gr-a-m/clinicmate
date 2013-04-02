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
 * @author Amy Baldwin
 * 
 */

public class HealthRecordController 
{
	private HealthRecord healthRecord;
	private Patient patient;
	private Person person;
	
	private static HealthRecordController instance;
	
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
	
	// Allow nurse or doctor to create a new patient record if the patient is on their patient list.
	// Return true if successfully created, false otherwise.
	public boolean createPatientRecord(UUID patientID, int dia, int sys, Date date, int glucose, int weight, String comment)
	{
		boolean created = false;
		if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
		{
			/*
			 * Must check if patient is on the current user's patient list here. Need to implement getAccessList() method in Person to determine 
			 * which patient IDs the current Person object may access.
			 */
			HealthRecord hr = new HealthRecord(patientID, date, dia, sys, glucose, weight);
			hr.addComment(comment);
			created = hr.save();
		}
		
		return created;
	}
	
	// Allow nurse or doctor to delete a patient's record if they are allowed access to the patient's files.
	// Return true if successfully deleted, false otherwise.
	public boolean deletePatientRecord(UUID patientID, UUID recordID) throws NonexistentRecordException
	{
		boolean deleted = false;
		patient = Patient.getById(patientID);
		if(patient.exists())
		{
			if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
			{
				/*
				 * Must check if patient is on the current user's patient list here. Need to implement getAccessList() method in Person to determine 
				 * which patient IDs the current Person object may access.
				 */
				HealthRecord[] patientRecords = patient.getHealthRecords();
				for(int i=0; i<patientRecords.length; i++)
				{
					if(patientRecords[i].getRecordID() == recordID)
					{
						deleted = patientRecords[i].delete();
						return deleted;
					}
				}
			}
		} 
		
		return deleted;
	}
	
	// Allow nurse or doctor to update patient's record if they are allowed access to the patient's files. 
	// Return true if successfully updated, false otherwise.
	public boolean updatePatientRecord(UUID recordID, UUID patientID, Date date, int diaBloodPressure, int sysBloodPressure, int glucose, int weight, ArrayList<String> comments, Date createdAt) throws NonexistentRecordException
	{
		boolean updated = false;
		if(Patient.getById(patientID).exists())
		{
			//If current logged in user is a nurse or doctor, allow them to update patient record if the patient is on their patient list.
			if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
			{
				/*
				 * Must check if patient is on the current user's patient list here. Need to implement getAccessList() method in Person to determine 
				 * which patient IDs the current Person object may access.
				 */
				HealthRecord hr = new HealthRecord(recordID, patientID, date, diaBloodPressure, sysBloodPressure, glucose, weight, comments, createdAt);
				hr.save();
				updated = true;
			}
		}
		
		return updated;
	}

}
