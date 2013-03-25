package mavericksoft.clinicmate;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

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
    private Date dateOfBirth;

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
                   Date dateOfBirth) {
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
                   Date dateOfBirth) {
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
        // Make sure the tables are initialized
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");
            PreparedStatement st = null;

            // If a record does not exist with this id, create it
            if (!this.exists()) {
                st = conn.prepareStatement("INSERT INTO patients VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                st.setString(0, this.username);
                st.setString(1, this.firstName);
                st.setString(2, this.lastName);
                st.setString(3, this.passwordHash);
                st.setString(4, this.passwordSalt);
                // Convert the internal java.util.Date to a java.sql.Date for storage
                st.setTimestamp(5, this.createdAt);
                st.setObject(6, this.patientID);
                st.setString(7, this.gender);
                st.setString(8, this.address);
                st.setString(9, this.insuranceProvider);
                st.setString(10, this.primaryPhoneNumber);
                st.setString(11, this.secondaryPhoneNumber);
                st.setDate(12, new java.sql.Date(this.dateOfBirth.getTime()));

                // Execute the insertion and record the success
                success = st.execute();
            } else {
                // If it does already exist, update it with the current values
                st = conn.prepareStatement("UPDATE patients SET username='?', first_name='?', last_name='?', password_hash='?', " +
                        " password_salt='?', created_at='?', patient_id='?', gender='?', address='?', insurance_provider='?', " +
                        " primary_phone='?', secondary_phone='?', date_of_birth='?' WHERE patient_id='?'");

                st.setString(0, this.username);
                st.setString(1, this.firstName);
                st.setString(2, this.lastName);
                st.setString(3, this.passwordHash);
                st.setString(4, this.passwordSalt);
                // Convert the internal java.util.Date to a java.sql.Date for storage
                st.setTimestamp(5, this.createdAt);
                st.setObject(6, this.patientID);
                st.setString(7, this.gender);
                st.setString(8, this.address);
                st.setString(9, this.insuranceProvider);
                st.setString(10, this.primaryPhoneNumber);
                st.setString(11, this.secondaryPhoneNumber);
                st.setDate(12, new java.sql.Date(this.dateOfBirth.getTime()));
                st.setObject(13, this.patientID);

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
        // Make sure the tables are initialized
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            // Get a connection for the H2 database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            // Prepare a DELETE statement
            PreparedStatement st = conn.prepareStatement("DELETE FROM patients WHERE patient_id='?'");
            st.setObject(0, this.patientID);
            st.execute();

            // Delete from the mapping database as well
            st = conn.prepareStatement("DELETE FROM person_map WHERE pat_id='?'");
            st.setObject(0, this.patientID);
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
        Person.checkTables();

        Connection conn = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE patient_id=?");
            st.setObject(0, this.patientID);

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
        // Make sure the tables are initialized
        Person.checkTables();

        Connection conn = null;
        Patient value = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            // Retrieve the rows from the patients database where the id
            // matches the UUID
            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE patient_id='?'");
            st.setObject(0, id);
            ResultSet rs = st.executeQuery();

            // Get a row from the ResultSet -- If there is no result, then
            // this UUID has no saved Patient associated with it
            if (rs.next()) {
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
                        rs.getString("primary_phone_number"),
                        rs.getString("secondary_phone_number"),
                        rs.getDate("date_of_birth"));
            } else {
                // If the ResultSet was empty, throw an exception
                throw new NonexistentRecordException("This Patient does not exist");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a Patient by id");
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
     * This method creates a full Patient object from the provided username
     * by pulling the data from the patients database.
     *
     * @param username This is the username for the Patient to pull
     * @return         The Patient object created from the username
     * @throws NonexistentRecordException
     */
    public static Patient getByUsername(String username) throws NonexistentRecordException {
        // Make sure the tables are initialized for the personal records
        Person.checkTables();

        Connection conn = null;
        Patient value = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            // Retrieve the rows from the patients database where the id
            // matches the UUID
            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE username='?'");
            st.setString(0, username);
            ResultSet rs = st.executeQuery();

            // Get a row from the ResultSet -- If there is no result, then
            // this UUID has no saved Patient associated with it
            if (rs.next()) {
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
                        rs.getString("primary_phone_number"),
                        rs.getString("secondary_phone_number"),
                        rs.getDate("date_of_birth"));
            } else {
                // If the ResultSet was empty, throw an exception
                throw new NonexistentRecordException("This Patient does not exist");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a Patient by id");
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

    public Date getDateOfBirth() {
        return dateOfBirth;
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
