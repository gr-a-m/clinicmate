package mavericksoft.clinicmate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.UUID;

/**
 * This class represents a Patient and operates near the database level. From
 * this class, Patients can be retrieved and created. More advanced analyses
 * can be generated from a Patient's records as well.
 *
 * @author Grant Marshall
 */
public class Patient extends Person {
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
     * @return This indicates whether the delete was successful
     */
    public boolean delete() {
        // TODO: Implement Deletion
        return false;
    }
}
