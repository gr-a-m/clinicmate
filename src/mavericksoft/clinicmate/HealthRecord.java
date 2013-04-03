package mavericksoft.clinicmate;

import java.sql.*;
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
class HealthRecord {
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

        // Set the comments to empty initially
        this.comments = new ArrayList<String>();
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
     * This method saves the Record to the database, including any comments.
     *
     * @return Whether the save was successful
     */
    public boolean save() {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            // If this doesn't exist, make a new record, otherwise update
            if (!this.exists()) {
                st = conn.prepareStatement("INSERT INTO records VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                st.setObject(1, this.recordID);
                st.setObject(2, this.patientID);
                st.setDate(3, new java.sql.Date(this.date.getTime()));
                st.setInt(4, this.diaBloodPressure);
                st.setInt(5, this.sysBloodPressure);
                st.setInt(6, this.glucose);
                st.setInt(7, this.weight);
                st.setTimestamp(8, new Timestamp(this.createdAt.getTime()));

                st.execute();

                // Now add any comments
                for (String comment : this.comments) {
                    st = conn.prepareStatement("INSERT INTO comments VALUES(?, ?)");
                    st.setObject(1, this.recordID);
                    st.setString(2, comment);
                    st.execute();
                }

                success = true;
            } else {
                st = conn.prepareStatement("UPDATE records SET record_id=?, patient_id=?, " +
                        "date=?, dia_blood_pressure=?, sys_blood_pressure=?, glucose=?, " +
                        "weight=?, created_at=? WHERE record_id=?");
                st.setObject(1, this.recordID);
                st.setObject(2, this.patientID);
                st.setDate(3, new java.sql.Date(this.date.getTime()));
                st.setInt(4, this.diaBloodPressure);
                st.setInt(5, this.sysBloodPressure);
                st.setInt(6, this.glucose);
                st.setInt(7, this.weight);
                st.setTimestamp(8, new Timestamp(this.createdAt.getTime()));
                st.setObject(9, this.recordID);

                // Remove any existing comments
                st = conn.prepareStatement("DELETE FROM comments WHERE record_id=?");
                st.setObject(1, this.recordID);
                st.execute();

                // Add the current comments
                for (String comment : this.comments) {
                    st = conn.prepareStatement("INSERT INTO comments VALUES(?, ?)");
                    st.setObject(1, this.recordID);
                    st.setString(2, comment);
                    st.execute();
                }

                success = true;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to delete the HealthRecord");
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("The database connection could not be closed");
                    sqle.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * This method deletes the record from the database
     *
     * @return Whether the delete was successful
     */
    public boolean delete() {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // If this doesn't exist, return false
            if (!this.exists()) {
                success = false;
            } else {
                // Delete this object from the database
                PreparedStatement st = conn.prepareStatement("DELETE FROM records WHERE record_id=?");
                st.setObject(1, this.recordID);
                st.execute();

                // Delete any related comments
                st = conn.prepareStatement("DELETE FROM comments WHERE record_id=?");
                st.setObject(1, this.recordID);
                st.execute();

                success = true;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to delete the HealthRecord");
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("The database connection could not be closed");
                    sqle.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * This method checks to see if the record already exists in the database
     *
     * @return Whether the record was found in the database
     */
    public boolean exists() {
        Connection conn = null;
        boolean found = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM records WHERE record_id=?");
            st.setObject(1, this.recordID);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                found = true;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to check for a record");
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("The database connection could not be closed");
                    sqle.printStackTrace();
                }
            }
        }

        return found;
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
