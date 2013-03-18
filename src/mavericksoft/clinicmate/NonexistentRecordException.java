package mavericksoft.clinicmate;

/**
 * This is an exception that is used to indicate that a record was not found
 * in the database when calling a method that is supposed to search for a
 * specific piece of data.
 *
 * @author Grant Marshall
 */
public class NonexistentRecordException extends Exception {
    // This is the only constructor that's really used
    NonexistentRecordException(String message) {
        super(message);
    }
}
