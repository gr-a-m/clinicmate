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
    public Patient(String firstName, String lastName, String password,
                   String gender, String address, String insuranceProvider,
                   String primaryPhoneNumber, String secondaryPhoneNumber,
                   Date dateOfBirth) {
        // Call the constructor of the Person superclass
        super(firstName, lastName, password);

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
     * @param firstName
     * @param lastName
     * @param passwordHash
     * @param passwordSalt
     * @param createdAt
     * @param patientID
     * @param gender
     * @param address
     * @param insuranceProvider
     * @param primaryPhoneNumber
     * @param secondaryPhoneNumber
     * @param dateOfBirth
     */
    public Patient(String firstName, String lastName, String passwordHash,
                   String passwordSalt, Date createdAt, UUID patientID,
                   String gender, String address, String insuranceProvider,
                   String primaryPhoneNumber, String secondaryPhoneNumber,
                   Date dateOfBirth) {
        super(firstName, lastName, passwordHash, passwordSalt, createdAt);

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
        // TODO: Implement Saving
        return false;
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
        // TODO: Implement Deletion
        return false;
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

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate");

            // Retrieve the rows from the patients database where the id
            // matches the UUID
            PreparedStatement st = conn.prepareStatement("SELECT * FROM patients WHERE id='?'");
            st.setString(0, id.toString());
            ResultSet rs = st.executeQuery();

            // Get a row from the ResultSet -- If there is no result, then
            // this UUID has no saved Patient associated with it
            if (rs.next()) {
                // This long new statement generates a new Patient object from
                // this result row
                return new Patient(rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("password_hash"),
                        rs.getString("password_salt"),
                        rs.getDate("created_at"),
                        UUID.fromString(rs.getString("id")),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("insurance_provider"),
                        rs.getString("primary_phone_number"),
                        rs.getString("secondary_phone_number"),
                        rs.getDate("date_of_birth"));
            } else {
                throw new NonexistentRecordException("This Patient does not exist");
            }
        } catch (SQLException sqle) {
            System.out.println("Failed to get a Patient by id");
            sqle.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {
                    System.out.println("Failed to close the database.");
                    sqle.printStackTrace();
                    return null;
                }
            }
        }
    }
}
