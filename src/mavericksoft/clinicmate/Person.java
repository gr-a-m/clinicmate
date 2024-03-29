package mavericksoft.clinicmate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Date;

/**
 * This abstract class represents a "Person" in the context of ClinicMate. The
 * different types of people all share some common features, and those
 * features are located in this class.
 *
 * @author Grant Marshall
 */
abstract class Person {
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String passwordHash;
    protected String passwordSalt;
    protected Date createdAt;

    /**
     * This constructor accepts that basic traits that define a person - the
     * first and last names and a password. It also generates the created_at,
     * and hashes the password with a random salt.
     *
     * This constructor is used if the Person being creates does not already
     * exist in the database - If you want to create a Person object based on
     * a previously created Person, use the constructor that allows you to
     *
     * @param username  The username for the person's account
     * @param firstName The person's first name
     * @param lastName  The person's last name
     * @param password  The password for this person's account
     */
    public Person(String username, String firstName, String lastName, String password) {
        // The first and last name are simply provided
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;

        // Created at is the current time at the instantiation of the class
        this.createdAt = new Date();

        // Create a random salt to append to the password by making a random,
        // large integer and convert it to an array of characters
        SecureRandom rand = new SecureRandom();
        this.passwordSalt = new BigInteger(130, rand).toString();

        // Hash the password + passwordHash with SHA and save it
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            // This line digests the password + hash then converts the bytes
            // into a string
            this.passwordHash = new String(md.digest((password + this.passwordSalt).getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException nsae) {
            // This should never occur as long as the Java JRE this runs on
            // has an implementation of the SHA algorithm
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
            // This should also never happen - UTF-8 works cross-platform
            uee.printStackTrace();
        }
    }

    /**
     * This alternative constructor allows you to specify when the person was
     * created. It is used when not making a "new" Person - it's used for when
     * the Person already exists in the database and needs to be turned into
     * a Java object to be manipulated.
     *
     * @param username     The username for the Person's record
     * @param firstName    The Person's first name
     * @param lastName     The Person's last name
     * @param passwordHash The Person's password hash
     * @param passwordSalt The Person's password salt
     * @param createdAt    When this Person was created (usually taken from
     *                     the database)
     */
    public Person(String username, String firstName, String lastName,
                  String passwordHash, String passwordSalt, Date createdAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
    }

    /**
     * This method changes based on the additional members of implementing
     * classes, but should save the Person to the database.
     *
     * @return Whether the save was successful
     */
    abstract boolean save();

    /**
     * This method deletes the person from the database if the person is
     * already saved there. The implementation of this changes depending on
     * what kind of person it is.
     *
     * @return Whether the person was found and deleted successfully
     */
    abstract boolean delete();

    /**
     * This method checks to see if the record exists already in the database.
     *
     * @return Whether this Person is already in the database or not
     */
    abstract boolean exists();

    /**
     * This method resets the password, provided the old password matches the
     * current password.
     *
     * @param oldPassword The old password - used for verification
     * @param newPassword The password to set for the person
     * @return Whether the password was set correctly or not
     */
    boolean resetPassword(String oldPassword, String newPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");

            // Verify the password matches the current password
            if (this.passwordHash.equals(new String(md.digest((oldPassword + this.passwordSalt).getBytes("UTF-8"))))) {
                // If it does, create a new salt and hash
                this.passwordSalt = new BigInteger(130, new SecureRandom()).toString(32);
                this.passwordHash = new String(md.digest((newPassword + this.passwordSalt).getBytes("UTF-8")));

                // Save the new password to the database
                this.save();
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException nsae) {
            // This should never occur as long as the Java JRE this runs on
            // has an implementation of the SHA algorithm
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
            // This should also never happen - UTF-8 works cross-platform
            uee.printStackTrace();
        }
        return false;
    }

    /**
     * This method checks if the provided password matches the hash and salt
     * stored in this person's properties.
     *
     * @param password The password to verify
     * @return         A boolean value representing whether the password matches
     */
    boolean checkPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");

            // Verify the password matches the current password
            if (this.passwordHash.equals(new String(md.digest((password + this.passwordSalt).getBytes("UTF-8"))))) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException nsae) {
            // This should never occur as long as the Java JRE this runs on
            // has an implementation of the SHA algorithm
            nsae.printStackTrace();
        } catch (UnsupportedEncodingException uee) {
            // This should also never happen - UTF-8 works cross-platform
            uee.printStackTrace();
        }
        return false;
    }

    /**
     * This method ensures that the tables are created and correctly formed
     * and fixes them if not.
     */
    protected static void checkTables() {
        File dbFile = new File("./data/clinicmate");

        // If the database file doesn't exist, make it
        if (!dbFile.exists()) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:h2:" + dbFile.toString(), "sa", "");
                String createString = "CREATE TABLE IF NOT EXISTS patients (" +
                        "username VARCHAR, " +
                        "first_name VARCHAR, " +
                        "last_name VARCHAR, " +
                        "password_hash VARCHAR, " +
                        "password_salt VARCHAR, " +
                        "created_at TIMESTAMP, " +
                        "patient_id UUID, " +
                        "gender VARCHAR, " +
                        "address VARCHAR, " +
                        "insurance_provider VARCHAR, " +
                        "primary_phone VARCHAR, " +
                        "secondary_phone VARCHAR, " +
                        "date_of_birth DATE)";

                // Make the patients table
                conn.createStatement().execute(createString);

                createString = "CREATE TABLE IF NOT EXISTS person_map (" +
                        "pat_id UUID, " +
                        "pro_id UUID)";

                // Make the mapping table
                conn.createStatement().execute(createString);

                createString = "CREATE TABLE IF NOT EXISTS professionals (" +
                        "username VARCHAR, " +
                        "first_name VARCHAR, " +
                        "last_name VARCHAR, " +
                        "password_hash VARCHAR, " +
                        "password_salt VARCHAR, " +
                        "created_at TIMESTAMP, " +
                        "professional_id UUID, " +
                        "admin BOOLEAN, " +
                        "nurse BOOLEAN, " +
                        "doctor BOOLEAN)";

                // Make the professionals table
                conn.createStatement().execute(createString);

                createString = "CREATE TABLE IF NOT EXISTS records (" +
                        "record_id UUID, " +
                        "patient_id UUID, " +
                        "date DATE, " +
                        "dia_blood_pressure SMALLINT, " +
                        "sys_blood_pressure SMALLINT, " +
                        "glucose SMALLINT, " +
                        "weight SMALLINT, " +
                        "created_at TIMESTAMP)";

                // Make the HealthRecords table
                conn.createStatement().execute(createString);

                createString = "CREATE TABLE IF NOT EXISTS comments (" +
                        "record_id UUID, " +
                        "comment TEXT)";

                // Make the comments table
                conn.createStatement().execute(createString);

                // If the users are empty, make a default admin
                createString = "SELECT count(*) FROM professionals";
                boolean empty = false;

                ResultSet rs = conn.createStatement().executeQuery(createString);
                if (rs.next()) {
                    if (rs.getInt("count(*)") == 0) {
                        empty = true;
                    }
                }

                // If there are no people in the database (it's new), create a
                // default admin
                if (empty) {
                    HealthProfessional defaultAdmin = new HealthProfessional(
                            "admin",
                            "default",
                            "admin",
                            "admin",
                            true,
                            false,
                            false
                    );
                    defaultAdmin.save();
                }
            } catch (SQLException sqle) {
                System.out.println("Failed to check database tables.");
                sqle.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException sqle) {
                        System.out.println("Failed to check database tables.");
                        sqle.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method checks to see if a Person exists with the provided username.
     * This works like a generalized exists() function.
     *
     * @param userName The username to search for
     * @return         Whether that username was found
     */
    public static boolean userExists(String userName) {
        Person.checkTables();
        boolean found = false;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            st = conn.prepareStatement("SELECT * FROM patients WHERE username=?");
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                found = true;
            }

            st = conn.prepareStatement("SELECT * FROM professionals WHERE username=?");
            st.setString(1, userName);
            rs = st.executeQuery();

            if (rs.next()) {
                found = true;
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

        return found;
    }

    /**
     * This method is used to get the permissions of a Person based on their
     * username. This is needed to find out what type of Person someone is
     * before instantiating them.
     *
     * @param username The Person's username
     * @return         The Permissions associated with that Person
     */
    public static Permissions getPermissions(String username) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:h2:./data/clinicmate", "sa", "");
            PreparedStatement st = null;

            st = conn.prepareStatement("SELECT * FROM patients WHERE username=?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // If this person is found in the patient database, they're a patient
            if (rs.next()) {
                return Permissions.PATIENT;
            }

            st = conn.prepareStatement("SELECT * FROM professionals WHERE username=?");
            st.setString(1, username);
            rs = st.executeQuery();

            // If this person is found in the pro db, they are one of three things
            if (rs.next()) {
                if (rs.getBoolean("nurse")) {
                    return Permissions.NURSE;
                } else if (rs.getBoolean("doctor")) {
                    return Permissions.DOCTOR;
                } else if (rs.getBoolean("admin")) {
                    return Permissions.ADMIN;
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

        // If we reach this point, the person wasn't found and has no permissions
        return null;
    }

    // Basic getters and setters for properties of the Person
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreatedAt() {
        // Converts a java.sql.Timestamp to a java.util.Date
        return new Date(createdAt.getTime());
    }
}
