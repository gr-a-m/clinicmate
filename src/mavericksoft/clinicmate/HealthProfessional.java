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
                              String passwordHash, String passwordSalt, Date createdAt,
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

    /**
     * This method either saves changes to the HealthProfessional or creates
     * a new record for them if it doesn't already exist in the database.
     *
     * @return Whether the save was successful
     */
    boolean save() {
        // Make sure tables are initialized
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            // If a record does not exist with this id, create it
            if (!this.exists()) {
                st = conn.prepareStatement("INSERT INTO professionals VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                st.setString(1, this.username);
                st.setString(2, this.firstName);
                st.setString(3, this.lastName);
                st.setString(4, this.passwordHash);
                st.setString(5, this.passwordSalt);
                st.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
                st.setObject(7, this.employeeID);
                st.setBoolean(8, this.admin);
                st.setBoolean(9, this.nurse);
                st.setBoolean(10, this.doctor);

                st.execute();

                success = true;
            } else {
                // If it does already exist, update it with the current values
                st = conn.prepareStatement("UPDATE professionals SET username=?, first_name=?, last_name=?, password_hash=?, " +
                        " password_salt=?, created_at=?, professional_id=?, admin=?, nurse=?, doctor=? WHERE professional_id=?");
                st.setString(1, this.username);
                st.setString(2, this.firstName);
                st.setString(3, this.lastName);
                st.setString(4, this.passwordHash);
                st.setString(5, this.passwordSalt);
                st.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
                st.setObject(7, this.employeeID);
                st.setBoolean(8, this.admin);
                st.setBoolean(9, this.nurse);
                st.setBoolean(10, this.doctor);
                st.setObject(11, this.employeeID);

                st.execute();

                success = true;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to save the Professional");
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
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // Prepare a DELETE statement
            PreparedStatement st = conn.prepareStatement("DELETE FROM professionals WHERE professional_id=?");
            st.setObject(1, this.employeeID);
            success = st.execute();

            // Delete from the mapping database as well
            st = conn.prepareStatement("DELETE FROM person_map WHERE pro_id=?");
            st.setString(1, this.employeeID.toString());
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
     * This is an implementation of the exists class. It checks the database to
     * see if this object is present.
     *
     * @return Whether the object is found in the database
     */
    public boolean exists() {
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa" ,"");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM professionals WHERE professional_id=?");
            st.setObject(1, this.employeeID);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                success = true;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to open a connection to the database.");
            sqle.printStackTrace();
        } finally {
            // Close the connection and return true
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to check for the existence");
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

    /**
     * This method gets a HealthRecord object for the provided UUID.
     *
     * @param id This is the UUID to make an object from
     * @return   The HealthRecord object for the provided UUID
     * @throws NonexistentRecordException
     */
    public static HealthProfessional getById(UUID id) throws NonexistentRecordException {
        Person.checkTables();

        Connection conn = null;
        HealthProfessional value = null;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            ArrayList<UUID> ids = new ArrayList<UUID>();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM person_map WHERE pro_id=?");
            st.setObject(1, id);
            ResultSet rs = st.executeQuery();

            // Iterate through each relation and add the associated patient id
            // to the list
            while (rs.next()) {
                ids.add((UUID) rs.getObject("pat_id"));
            }

            st = conn.prepareStatement("SELECT * FROM professionals WHERE professional_id=?");
            st.setObject(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                value = new HealthProfessional(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password_hash"),
                        rs.getString("password_salt"),
                        new Date(rs.getTimestamp("created_at").getTime()),
                        (UUID) rs.getObject("professional_id"),
                        rs.getBoolean("admin"),
                        rs.getBoolean("nurse"),
                        rs.getBoolean("doctor"),
                        ids);
            } else {
                throw new NonexistentRecordException("This ID has no matching HealthProfessional");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a HealthProfessional by id");
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to close the database.");
                    sqle.printStackTrace();
                }
            }
        }

        return value;
    }

    /**
     * This method gets a HealthRecord object for the provided username
     *
     * @param username The username to search the database for
     * @return         The HealthRecord object formed from the database.
     * @throws NonexistentRecordException
     */
    public static HealthProfessional getByUsername(String username) throws NonexistentRecordException {
        Person.checkTables();

        Connection conn = null;
        HealthProfessional value = null;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            ArrayList<UUID> ids = new ArrayList<UUID>();

            PreparedStatement st = conn.prepareStatement("SELECT professional_id FROM professionals WHERE username=?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            UUID id = null;

            if (rs.next()) {
                id = (UUID) rs.getObject("professional_id");
            } else {
                throw new NonexistentRecordException("No record found for user " + username);
            }

            st = conn.prepareStatement("SELECT * FROM person_map WHERE pro_id=?");
            st.setObject(1, id);
            rs = st.executeQuery();

            // Iterate through each relation and add the associated patient id
            // to the list
            while (rs.next()) {
                ids.add((UUID) rs.getObject("pat_id"));
            }

            st = conn.prepareStatement("SELECT * FROM professionals WHERE username=?");
            st.setString(1, username);
            rs = st.executeQuery();

            if (rs.next()) {
                value = new HealthProfessional(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password_hash"),
                        rs.getString("password_salt"),
                        new Date(rs.getTimestamp("created_at").getTime()),
                        (UUID) rs.getObject("professional_id"),
                        rs.getBoolean("admin"),
                        rs.getBoolean("nurse"),
                        rs.getBoolean("doctor"),
                        ids);
            } else {
                throw new NonexistentRecordException("This ID has no matching HealthProfessional");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a HealthProfessional by id");
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to close the database.");
                    sqle.printStackTrace();
                }
            }
        }

        return value;
    }

    /**
     * This method adds the patient with the provided id to this
     * HealthProfessional's list of patients.
     *
     * @param id The patient ID to add to the professional
     * @return   Whether the addition was successful
     */
    public boolean addPatient(UUID id) {
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            PreparedStatement st = conn.prepareStatement("INSERT INTO person_map VALUES (?, ?)");
            st.setObject(1, id);
            st.setObject(2, this.employeeID);
            st.execute();
            success = true;

            this.patientIDs.add(id);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to close the database.");
                    sqle.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * This method gets the Permissions type of this HealthProfessional. That
     * depends on a series of boolean values of the object.
     *
     * @return The Permissions enum value
     */
    public Permissions getPermissions() {
        if (this.isAdmin())
            return Permissions.ADMIN;
        else if (this.isNurse())
            return Permissions.NURSE;
        else
            return Permissions.DOCTOR;
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
