package mavericksoft.clinicmate;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * The HealthRecord class represents the measurements for one day. Each
 * HealthRecord should be associated with a specific Patient on a specific
 * date.
 *
 * Where possible, only getters are defined because values should not be
 * changed after creation.
 *
 * @author Grant Marshall
 */
public class HealthRecord {
    private UUID recordID;
    private UUID patientID;
    private Date date;
    private int diaBloodPressure;
    private int sysBloodPressure;
    private int glucose;
    private int weight;
    private ArrayList<String> comments;
    private Date createdAt;

    /**
     * This constructor should be used when creating a new record that doesn't
     * already exist in the database. It generates a UUID for the record and
     * makes a createdAt value.
     *
     * @param patientID         The UUID of the Patient this record is for
     * @param date              The date the measurements for this record was
     *                          taken
     * @param diaBloodPressure  The diastolic blood pressure of the Patient on
     *                          this date
     * @param sysBloodPressure  The systolic blood pressure of the Patient on
     *                          this date
     * @param glucose           The glucose levels of the Patient on this date
     * @param weight            The patient's weight on this date
     */
    public HealthRecord(UUID patientID, Date date, int diaBloodPressure,
                        int sysBloodPressure, int glucose, int weight) {
        // Generate a UUID for this record
        this.recordID = UUID.randomUUID();

        // Fill in the rest of the fields
        this.patientID = patientID;
        this.date = date;
        this.diaBloodPressure = diaBloodPressure;
        this.sysBloodPressure = sysBloodPressure;
        this.glucose = glucose;
        this.weight = weight;

        // Set the createdAt
        this.createdAt = new Date();
    }

    /**
     * This is the constructor for creating a HealthRecord object from a record
     * that already exists in the database.
     *
     * @param recordID         The UUID of this record
     * @param patientID        The UUID of the patient from the database
     * @param date             The date the record is about
     * @param diaBloodPressure The patient's diastolic blood pressure on this date
     * @param sysBloodPressure The patient's systolic blood pressure on this date
     * @param glucose          The patient's glucose levels on this date
     * @param weight           The patient's weight on this date
     * @param comments         Any comments on this particular measurement
     * @param createdAt        When this record was created
     */
    public HealthRecord(UUID recordID, UUID patientID, Date date, int diaBloodPressure,
                        int sysBloodPressure, int glucose, int weight,
                        ArrayList<String> comments, Date createdAt) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.date = date;
        this.diaBloodPressure = diaBloodPressure;
        this.sysBloodPressure = sysBloodPressure;
        this.glucose = glucose;
        this.weight = weight;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    /**
     * This method adds a comment to this record's list of comments.
     *
     * @param comment This argument is the comment to add
     */
    public void addComment(String comment) {
        this.comments.add(comment);
    }

    // This is a simple list of getters and setters
    public UUID getRecordID() {
        return recordID;
    }

    public UUID getPatientID() {
        return patientID;
    }

    public Date getDate() {
        return date;
    }

    public int getDiaBloodPressure() {
        return diaBloodPressure;
    }

    public int getSysBloodPressure() {
        return sysBloodPressure;
    }

    public int getGlucose() {
        return glucose;
    }

    public int getWeight() {
        return weight;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
