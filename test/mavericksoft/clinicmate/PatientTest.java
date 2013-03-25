package mavericksoft.clinicmate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * This class tests the functionality of the Patient class using JUnit. It
 * tests most of the major functions (not every getter/setter) at some point.
 *
 * @author Grant Marshall
 */
public class PatientTest {
    private Patient test1;
    private Patient test2;
    private HealthRecord record1;
    private HealthRecord record2;

    /**
     * Set up the tests by creating a few sample Patient objects -- Note that
     * these have not been saved yet, so they should not exist in the database
     * yet.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Load the H2 database driver
        Class.forName("org.h2.Driver");

        // Create the tables
        Person.checkTables();

        // Create some test
        this.test1 = new Patient("test1", "test" , "1", "password", "male",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());

        this.test2 = new Patient("test2", "test" , "2", "password2", "female",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());

        // Add 2 records to test1
        this.record1 = new HealthRecord(test1.getPatientID(), new Date(), 0, 0, 0, 0);
        this.record2 = new HealthRecord(test1.getPatientID(), new Date(), 1, 1, 1, 1);
    }

    /**
     * The patient shouldn't exist until it's saved and should cease existing
     * after deletion.
     */
    @Test
    public void testSaveAndDelete() {
        assert(!this.test1.exists());
        this.test1.save();
        assert(this.test1.exists());
        this.test1.delete();
        assert(!this.test1.exists());
    }

    /**
     * This test saves test1 to the database, ensures it exists, then tries to
     * get it again by UUID from the database and asserts that the UUID of the
     * retrieved record matches the original.
     */
    @Test
    public void testGetById() {
        assert(!this.test1.exists());
        UUID test1Id = this.test1.getPatientID();
        this.test1.save();
        assert(this.test1.exists());

        try {
            Patient test1Copy = Patient.getById(this.test1.getPatientID());
            assert(test1Copy.exists());
            assert(test1Copy.getPatientID().equals(test1Id));
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
        }
    }

    /**
     * This test is similar to the previous test, but with usernames
     */
    @Test
    public void testGetByUsername() {
        assert(!this.test2.exists());
        String test2Username = this.test2.getUsername();
        this.test2.save();
        assert(this.test2.exists());

        try {
            Patient test2Copy = Patient.getByUsername(test2.getUsername());
            assert(test2Copy.exists());
            assert(test2Copy.getUsername().equals(test2Username));
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
        }
    }

    /**
     * This test adds records to a particular Patient then verifies that those
     * records are associated with the Patient.
     */
    @Test
    public void testPatientRecordRelations() {
        assert(!this.test1.exists());
        this.test1.save();
        assert(this.test1.exists());
        assert(!record1.exists() && !record2.exists());
        record1.save();
        assert(record1.exists() && !record2.exists());
        record2.save();
        assert(record1.exists() && record2.exists());
        assert(test1.getHealthRecords().length == 2);
    }

    /**
     * This method deletes the data files created during the tests.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        File dbFile = new File("./data/clinicmate.h2.db");
        dbFile.delete();
        dbFile = new File("./data/clinicmate.trace.db");
        dbFile.delete();
    }
}
