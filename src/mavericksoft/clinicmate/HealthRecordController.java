package mavericksoft.clinicmate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.math3.stat.regression.SimpleRegression;

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

        // Only nurses and doctors can comment on a patient's record
        //if((PermissionsController.getInstance()).currentUserPermissions() == Permissions.NURSE || (PermissionsController.getInstance()).currentUserPermissions() == Permissions.DOCTOR)
        //{
            HealthRecord hr = HealthRecord.getById(recordID);

            hr.addComment(comment);
            hr.save();
            added = true;
        //}

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

    /**
     * This method generates a linear regression model for the given patient.
     * The resulting model will have x=0 represent the day that is given for
     * startDate and each increment of x represent 1 day. The y value is the
     * measurement of that metric for a given day.
     *
     * @param patientID The ID of the patient to generate a regression for.
     * @param startDate The beginning date of the model generated.
     * @param endDate   The end date of the model generated.
     * @param type      The variable to generate the regression based on.
     * @return          A SimpleRegression model that can be displayed by the
     *                  UI in graphical form.
     */
    public SimpleRegression linearRegression(UUID patientID,
                                             Date startDate,
                                             Date endDate,
                                             RegressionTypes type)
            throws NonexistentRecordException {
        // Create an apache commons regression object
        SimpleRegression regression = new SimpleRegression();

        int beginDay = (int) (startDate.getTime() / (1000*60*60*24));

        // Get the HealthRecords, then only pick the ones between the start and
        // end dates for the regression.
        HealthRecord[] records = getInstance().getRecordsForPatient(patientID);
        ArrayList<HealthRecord> validRecords = new ArrayList<HealthRecord>();

        for (HealthRecord record : records) {
            if (record.getDate().getTime() >= startDate.getTime() &&
                record.getDate().getTime() <= endDate.getTime()) {
                validRecords.add(record);
            }
        }

        // Add the records to the model, with x being the number of days past
        // the start and y being the measurement of the specified record.
        for (HealthRecord record : validRecords) {
            Calendar recCalendar = Calendar.getInstance();
            recCalendar.setTime(record.getDate());
            int day = (int) ((record.getDate().getTime()) / (1000*60*60*24));

            // Add different values depending on what type of regression is
            // needed
            switch (type) {
                case GLUCOSE:
                    regression.addData(day - beginDay, record.getGlucose());
                    break;
                case SYS:
                    regression.addData(day - beginDay, record.getSysBloodPressure());
                    break;
                case DIA:
                    regression.addData(day - beginDay, record.getDiaBloodPressure());
                    break;
                case WEIGHT:
                    regression.addData(day - beginDay, record.getWeight());
                    break;
                default:
                    System.out.println("[ERROR]: Invalid regression type " +
                            "requested.");
                    break;
            }
        }

        return regression;
    }
}
