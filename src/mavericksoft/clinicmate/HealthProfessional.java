package mavericksoft.clinicmate;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The HealthProfessional class represents a Doctor, Nurse, or Admin in the
 * ClinicMate program. Each one has a List of associated patients that they
 * manage.
 *
 * @author Grant Marshall
 */
class HealthProfessional extends Person {
    private UUID employeeID;
    private boolean admin;
    private boolean nurse;
    private boolean doctor;
    private ArrayList<UUID> patientIDs;

    /**
     * This constructor is for creating a HealthProfessional that does not
     * currently exist in the database. It requires most of the field to be
     * provided.
     *
     * @param username  The username for the professional's account
     * @param firstName The first name of the professional
     * @param lastName  The last name of the professional
     * @param password  The password for the professional's account
     * @param admin     Whether the professional has admin priveledges
     * @param nurse     Whether the professional has nurse priveledges
     * @param doctor    Whether the professional has doctor priveledges
     */
    public HealthProfessional(String username, String firstName, String lastName,
                              String password, boolean admin, boolean nurse,
                              boolean doctor) {
        super(username, firstName, lastName, password);

        // Generate a UUID
        this.employeeID = UUID.randomUUID();

        // Fill in the rest of the fields
        this.admin = admin;
        this.nurse = nurse;
        this.doctor = doctor;

        // A new HealthProfessional has no Patients
        this.patientIDs = new ArrayList<UUID>();
    }

    /**
     * This constructor creates a HealthProfessional object based on an
     * existing database record.
     *
     * @param username     The professional's username
     * @param firstName    The first name of the professional
     * @param lastName     The last name of the professional
     * @param passwordHash The hash of the professional's password
     * @param passwordSalt The salt usef on the professional's password
     * @param createdAt    When the professional's account was created
     * @param employeeID   The professional's UUID
     * @param admin        Whether the professional is an admin
     * @param nurse        Whether the professional is a nurse
     * @param doctor       Whether the professional is a doctor
     * @param patientIDs   The ArrayList of the patients assigned to this pro
     */
    public HealthProfessional(String username, String firstName, String lastName,
                              String passwordHash, String passwordSalt, Timestamp createdAt,
                              UUID employeeID, boolean admin, boolean nurse, boolean doctor,
                              ArrayList<UUID> patientIDs) {
        super(username, firstName, lastName, passwordHash, passwordSalt, createdAt);

        // Set some of the basic properties of the HealthProfessional
        this.employeeID = employeeID;
        this.admin = admin;
        this.nurse = nurse;
        this.doctor = doctor;
        this.patientIDs = patientIDs;
    }

    // TODO
    boolean save() {
        return false;
    }

    /**
     * This method deletes the HealthProfessional from the database and removes
     * references to it from the patient-professional mapping table.
     *
     * @return Whether the delete was successful
     */
    boolean delete() {
        Connection conn = null;
        boolean success = false;

        try {
            // Get a connection for the H2 database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            // Prepare a DELETE statement
            PreparedStatement st = conn.prepareStatement("DELETE FROM professionals WHERE id='?'");
            st.setString(0, this.employeeID.toString());
            success = st.execute();

            // Delete from the mapping database as well
            st = conn.prepareStatement("DELETE FROM person_map WHERE pro_id='?'");
            st.setString(0, this.employeeID.toString());
            success = success & st.execute();
        } catch (SQLException sqle) {
            System.out.println("Failed to open a connection to the database.");
            sqle.printStackTrace();
        } finally {
            // Close the connection and return true
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("");
                    sqle.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * This method returns an Array of Patient objects containing all of the
     * Patients that this HealthProfessional is assigned to.
     *
     * @return An array of all Patients for this HealthProfessional
     */
    public Patient[] getPatients() {
        ArrayList<Patient> patients = new ArrayList<Patient>();

        for (UUID patientID : patientIDs) {
            try {
                patients.add(Patient.getById(patientID));
            } catch (NonexistentRecordException nre) {
                System.out.println("Illegal Patient ID " + patientID +
                        " assigned to " + this.firstName + " " + this.lastName);
            }
        }

        // Convert the ArrayList to an Array and return it
        return patients.toArray(new Patient[0]);
    }

    // The following are a series of getters and setters
    public ArrayList<UUID> getPatientIDs() {
        return this.patientIDs;
    }

    public boolean isNurse() {
        return this.nurse;
    }

    public void setNurse(boolean nurse) {
        this.nurse = nurse;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDoctor() {
        return this.doctor;
    }

    public void setDoctor(boolean doctor) {
        this.doctor = doctor;
    }

    public UUID getEmployeeID() {
        return this.employeeID;
    }
}
