package mavericksoft.clinicmate;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * This class represents a Patient and operates near the database level. From
 * this class, Patients can be retrieved and created. More advanced analyses
 * can be generated from a Patient's records as well.
 *
 * The main purpose of this class is to provide an interface between the
 * database and the rest of the software for handling Patients between
 * sessions.
 *
 * @author Grant Marshall
 */
class Patient extends Person {
    private UUID patientID;
    private String gender;
    private String address;
    private String insuranceProvider;
    private String primaryPhoneNumber;
    private String secondaryPhoneNumber;
    private java.util.Date dateOfBirth;

    /**
     * This is the main constructor to create a "new" Patient. It will generate
     * a new UUID and createdAt for the Patient, so only use this if the
     * intention is to create a Patient that is not already present in the
     * database.
     *
     * @param username             The username for the Patient's account
     * @param firstName            The Patient's first name
     * @param lastName             The Patient's last name
     * @param password             The password to set for the Patient
     * @param gender               The Patient's gender
     * @param address              The Patient's home address
     * @param insuranceProvider    The Patient's insurance provider
     * @param primaryPhoneNumber   The Patient's primary phone number
     * @param secondaryPhoneNumber The Patient's secondary phone number
     * @param dateOfBirth          The Patient's date of birth
     */
    public Patient(String username, String firstName, String lastName, String password,
                   String gender, String address, String insuranceProvider,
                   String primaryPhoneNumber, String secondaryPhoneNumber,
                   java.util.Date dateOfBirth) {
        // Call the constructor of the Person superclass
        super(username, firstName, lastName, password);

        // Generate a random UUID to use for this person
        this.patientID = UUID.randomUUID();

        // Fill the rest of the field in the class
        this.gender = gender;
        this.address = address;
        this.insuranceProvider = insuranceProvider;
        this.primaryPhoneNumber = primaryPhoneNumber;
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * This is the constructor for creating a Patient object from a preexisting
     * record, one that is already in the database. If you want to create a new
     * Patient that doesn't exist already, use the other constructor.
     *
     * @param username             The patient's username
     * @param firstName            The first name of the patient
     * @param lastName             The last name of the patient
     * @param passwordHash         The hash for the patient's password + salt
     * @param passwordSalt         The salt for the patient's password hash
     * @param createdAt            The date the record was created at
     * @param patientID            A UUID assigned to the patient
     * @param gender               The patient's gender
     * @param address              The patient's address
     * @param insuranceProvider    The patient's insurance provider
     * @param primaryPhoneNumber   The primary contact phone number
     * @param secondaryPhoneNumber This is an alternate phone number to contact
     *                             the patient at
     * @param dateOfBirth          The patient's date of birth
     */
    public Patient(String username, String firstName, String lastName, String passwordHash,
                   String passwordSalt, Timestamp createdAt, UUID patientID,
                   String gender, String address, String insuranceProvider,
                   String primaryPhoneNumber, String secondaryPhoneNumber,
                   java.util.Date dateOfBirth) {
        super(username, firstName, lastName, passwordHash, passwordSalt, createdAt);

        // The rest of the object is simple to set up
        this.patientID = patientID;
        this.gender = gender;
        this.address = address;
        this.insuranceProvider = insuranceProvider;
        this.primaryPhoneNumber = primaryPhoneNumber;
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * This method saves the Patient object into the database
     *
     * @return This indicates whether the save was successful
     */
    public boolean save() {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            // If a record does not exist with this id, create it
            if (!this.exists()) {
                st = conn.prepareStatement("INSERT INTO patients VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                st.setString(1, this.username);
                st.setString(2, this.firstName);
                st.setString(3, this.lastName);
                st.setString(4, this.passwordHash);
                st.setString(5, this.passwordSalt);
                // Convert the internal java.util.Date to a java.sql.Date for storage
                st.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
                st.setObject(7, this.patientID);
                st.setString(8, this.gender);
                st.setString(9, this.address);
                st.setString(10, this.insuranceProvider);
                st.setString(11, this.primaryPhoneNumber);
                st.setString(12, this.secondaryPhoneNumber);
                st.setDate(13, new java.sql.Date(this.dateOfBirth.getTime()));

                // Execute the insertion and record the success
                st.execute();
                
                success = true;
            } else {
                // If it does already exist, update it with the current values
                st = conn.prepareStatement("UPDATE patients SET username=?, first_name=?, last_name=?, password_hash=?, " +
                        " password_salt=?, created_at=?, patient_id=?, gender=?, address=?, insurance_provider=?, " +
                        " primary_phone=?, secondary_phone=?, date_of_birth=? WHERE patient_id=?");

                st.setString(1, this.username);
                st.setString(2, this.firstName);
                st.setString(3, this.lastName);
                st.setString(4, this.passwordHash);
                st.setString(5, this.passwordSalt);
                // Convert the internal java.util.Date to a java.sql.Date for storage
                st.setTimestamp(6, new Timestamp(this.createdAt.getTime()));
                st.setObject(7, this.patientID);
                st.setString(8, this.gender);
                st.setString(9, this.address);
                st.setString(10, this.insuranceProvider);
                st.setString(11, this.primaryPhoneNumber);
                st.setString(12, this.secondaryPhoneNumber);
                st.setDate(13, new java.sql.Date(this.dateOfBirth.getTime()));
                st.setObject(14, this.patientID);

                st.execute();
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to save the Patient");
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
     * This method deletes the Patient object from the database if it's present
     *
     * Note that this is a permanent deletion. Only call this method if you
     * mean to permanently delete the patient from the records. Patient
     * objects will naturally be deleted by the garbage collector.
     *
     * @return This indicates whether the delete was successful
     */
    public boolean delete() {
        Connection conn = null;
        boolean success = false;

        try {
            // Get a connection for the H2 database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // Prepare a DELETE statement
            PreparedStatement st = conn.prepareStatement("DELETE FROM patients WHERE patient_id=?");
            st.setObject(1, this.patientID);
            st.execute();

            // Delete from the mapping database as well
            st = conn.prepareStatement("DELETE FROM person_map WHERE pat_id=?");
            st.setObject(1, this.patientID);
            st.execute();
            success = true;
        } catch (SQLException sqle) {
            System.out.println("Failed to open a connection to the database.");
            sqle.printStackTrace();
        } finally {
            // Close the connection and return true
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to close the connection in delete");
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
        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE patient_id=?");
            st.setObject(1, this.patientID);

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
     * This method retrieves a preexisting Patient record from the database,
     * creates a Patient object, and returns it.
     *
     * If there is an exception when the record is being retrieved, this
     * method will return null.
     *
     * @param id This is the ID to search the database for
     * @return   The return value is a Patient object based on the Patient's
     *           database record.
     */
    public static Patient getById(UUID id) throws NonexistentRecordException {
        Connection conn = null;
        Patient value = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // Retrieve the rows from the patients database where the id
            // matches the UUID
            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE patient_id=?");
            st.setObject(1, id);
            ResultSet rs = st.executeQuery();

            // Get a row from the ResultSet -- If there is no result, then
            // this UUID has no saved Patient associated with it
            while (rs.next()) {
                // This long new statement generates a new Patient object from
                // this result row
                value = new Patient(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password_hash"),
                        rs.getString("password_salt"),
                        rs.getTimestamp("created_at"),
                        (UUID) rs.getObject("patient_id"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("insurance_provider"),
                        rs.getString("primary_phone"),
                        rs.getString("secondary_phone"),
                        new java.util.Date(rs.getDate("date_of_birth").getTime()));
            }

            if (value == null) {
                throw new NonexistentRecordException("Failed to get Patient by ID");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Failed to get a Patient by id");
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
     * This method creates a full Patient object from the provided username
     * by pulling the data from the patients database.
     *
     * @param username This is the username for the Patient to pull
     * @return         The Patient object created from the username
     * @throws NonexistentRecordException
     */
    public static Patient getByUsername(String username) throws NonexistentRecordException {
        Connection conn = null;
        Patient value = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // Retrieve the rows from the patients database where the id
            // matches the UUID
            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE username=?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // Get a row from the ResultSet -- If there is no result, then
            // this UUID has no saved Patient associated with it
            while (rs.next()) {
                // This long new statement generates a new Patient object from
                // this result row
                value = new Patient(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password_hash"),
                        rs.getString("password_salt"),
                        rs.getTimestamp("created_at"),
                        (UUID) rs.getObject("patient_id"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("insurance_provider"),
                        rs.getString("primary_phone"),
                        rs.getString("secondary_phone"),
                        new java.util.Date(rs.getDate("date_of_birth").getTime()));
            }

            if (value == null) {
                throw new NonexistentRecordException("Failed to get Patient by username");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a Patient by username");
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
     * This method gets all of the HealthRecords for this Patient
     *
     * @return An array of HealthRecords for this patient
     */
    public HealthRecord[] getHealthRecords() {
        Connection conn = null;
        LinkedList<HealthRecord> records = new LinkedList<HealthRecord>();
        HashMap<UUID, ArrayList<String>> comments = new HashMap<UUID, ArrayList<String>>();

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");

            // Get the UUIDs for the patient's records
            PreparedStatement st = conn.prepareStatement("SELECT record_id FROM records WHERE patient_id=?");
            st.setObject(1, this.patientID);
            ResultSet rs = st.executeQuery();

            // Enter a UUID into the map for each record the patient has
            while (rs.next()) {
                comments.put((UUID) rs.getObject("record_id"), new ArrayList<String>());
            }

            // For each record id, get the comments
            for (UUID id : comments.keySet()) {
                st = conn.prepareStatement("SELECT comment FROM comments WHERE record_id=?");
                st.setObject(1, id);
                rs = st.executeQuery();

                // Iterate through the record's comments and add them
                while (rs.next()) {
                    comments.get(id).add(rs.getString("comment"));
                }
            }

            // Prepare a statement that pulls all records for this patient's id
            st = conn.prepareStatement("SELECT * FROM records WHERE patient_id=?");
            st.setObject(1, this.patientID);
            rs = st.executeQuery();

            // Create a new object for each result
            while (rs.next()) {
                records.add(new HealthRecord(
                        (UUID) rs.getObject("record_id"),
                        (UUID) rs.getObject("patient_id"),
                        rs.getDate("date"),
                        rs.getInt("dia_blood_pressure"),
                        rs.getInt("sys_blood_pressure"),
                        rs.getInt("glucose"),
                        rs.getInt("weight"),
                        comments.get((UUID)rs.getObject("record_id")),
                        new Date(rs.getTimestamp("created_at").getTime())
                ));
            }
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

        return records.toArray(new HealthRecord[0]);
    }

    /**
     * This method gets the HealthProfessionals that are allowed to interact
     * with this patient. Try to avoid this method if possible -- it is
     * preferential to look up patients from their HealthProfessionals and
     * not the other way around.
     *
     * @return An array of HealthProfessionals
     */
    public HealthProfessional[] getProfessionals() {
        // Make an ArrayList to hold the HealthProfessionals as they're retrieved
        ArrayList<HealthProfessional> pros = new ArrayList<HealthProfessional>();

        // Make a call to the database to pull all associated Professionals
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            st = conn.prepareStatement("SELECT * FROM person_map WHERE pat_id=?");
            st.setObject(1, this.patientID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                try {
                    pros.add(HealthProfessional.getById((UUID) rs.getObject("pro_id")));
                } catch (NonexistentRecordException nre) {
                    System.out.println("Illegal Patient ID " + rs.getString("pro_id") +
                            " assigned to " + this.firstName + " " + this.lastName);
                }
            }
        } catch (SQLException sqle) {
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

        // Convert the ArrayList to an Array and return it
        return pros.toArray(new HealthProfessional[0]);
    }

    // A series of getters and setters
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UUID getPatientID() {
        return patientID;
    }

    public java.util.Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }
}
