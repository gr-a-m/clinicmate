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
 * DoctorPage, and PatientPage
 * 
 * @author Amy Baldwin
 * 
 */

public class HealthRecordController 
{
	private HealthRecord healthRecord;
	private Patient patient;
	
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
	
	public boolean createPatientRecord(UUID patientID, int dia, int sys, Date date, int glucose, int weight, String comment)
	{
		boolean created = false;
		HealthRecord hr = new HealthRecord(patientID, date, dia, sys, glucose, weight);
		created = hr.save();
		hr.addComment(comment);
		return created;
	}
	
	public boolean deletePatientRecord(UUID patientID, UUID recordID) throws NonexistentRecordException
	{
		boolean deleted = false;
		patient = Patient.getById(patientID);
		if(patient.exists())
		{
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
		return deleted;
	}
	
	public boolean updatePatientRecord(UUID recordID, UUID patientID, Date date, int diaBloodPressure, int sysBloodPressure, int glucose, int weight, ArrayList<String> comments, Date createdAt) throws NonexistentRecordException
	{
		boolean updated = false;
		if(Patient.getById(patientID).exists())
		{
		HealthRecord hr = new HealthRecord(recordID, patientID, date, diaBloodPressure, sysBloodPressure, glucose, weight, comments, createdAt);
		hr.save();
		updated = true;
		}
		
		return updated;
	}

}
