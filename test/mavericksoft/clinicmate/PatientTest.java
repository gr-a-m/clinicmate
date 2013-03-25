package mavericksoft.clinicmate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.h2.Driver;

import java.util.Date;

/**
 * This class tests the functionality of the Patient class using JUnit
 *
 * @author Grant Marshall
 */
public class PatientTest {
    private Patient test1;
    private Patient test2;

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

    @After
    public void tearDown() throws Exception {

    }
}
