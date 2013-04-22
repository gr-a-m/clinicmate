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

class HealthRecordController {
	// The static instance object
    private static HealthRecordController instance;

    /**
     * This method returns the static instance of this class or initializes it
     * if it does not yet exist.
     *
     * @return The static instance of the class
     */
	public static HealthRecordController getInstance() {
		if(instance == null) {
			instance = new HealthRecordController();
			return instance;
		} else {
			return instance;
        }
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
            System.out.println("1");
            HealthProfessional currentProfessional = (HealthProfessional) PermissionsController.getInstance().getCurrentUser();
            HealthRecord hr = HealthRecord.getById(recordID);
            if(currentProfessional.getPatientIDs().contains(hr.getPatientID()))
            {
                System.out.println("2");
                hr.addComment(comment);
                added = hr.save();
            }
        }

        return added;
    }

    /**
     * This method creates a new HealthRecord with the provided information
     * and returns the resulting object if successful.
     *
     * @param patientID        The ID of the Patient these measurements are for
     * @param diaBloodPressure The diastolic blood pressure on this day
     * @param sysBloodPressure The systolic blood pressure on this day
     * @param glucose          The glucose levels on this day
     * @param weight           The patient's weight on this day
     * @return                 The new HealthRecord if successful, null otherwise
     */
    public HealthRecord addRecord(UUID patientID, Date date, int diaBloodPressure,
                             int sysBloodPressure, int glucose, int weight) {
        HealthRecord added = null;

        Permissions permissions = PermissionsController.getInstance().currentUserPermissions();

        // Handle the method depending on who's logged in
        switch (permissions) {
            // Make sure this record is for this Patient
            case PATIENT:
                Patient patient = (Patient) PermissionsController.getInstance().getCurrentUser();
                if (patient.getPatientID().equals(patientID)) {
                    HealthRecord newRecord = new HealthRecord(patientID, date, diaBloodPressure,
                            sysBloodPressure, glucose, weight);
                    newRecord.save();
                    added = newRecord;
                }
                break;
            // A nurse can make records for any Patient
            case NURSE:
                added = new HealthRecord(patientID, date, diaBloodPressure,
                        sysBloodPressure, glucose, weight);
                added.save();
                break;
            // A doctor should only add records for their own patients (it should usually be a nurse)
            case DOCTOR:
                HealthProfessional doctor = (HealthProfessional) PermissionsController.getInstance().getCurrentUser();
                ArrayList<UUID> patientList = doctor.getPatientIDs();

                // Go through the list of Patients and add the record if the patient is found
                for (UUID id : patientList) {
                    if (id.equals(patientID)) {
                        added = new HealthRecord(patientID, date, diaBloodPressure,
                                sysBloodPressure, glucose, weight);
                        added.save();
                        break;
                    }
                }
                break;
            // If the currently logged in person doesn't match any of the
            // previous types, they may not add a HealthRecord to this patient.
            default:
                System.out.println("User does not possess the correct " +
                                   "permissions to add a record.");
                break;
        }

        return added;
    }

    /**
     * This method gets all of the HealthRecords for a given Patient (based on
     * the patient's ID) and returns them in an array.
     *
     * @param patientID The ID of the patient to pull up records for
     * @return          An array containing that patient's HealthRecords
     */
    public HealthRecord[] getRecordsForPatient(UUID patientID) throws NonexistentRecordException {
        Patient patient = Patient.getById(patientID);
        return patient.getHealthRecords();
    }
}
