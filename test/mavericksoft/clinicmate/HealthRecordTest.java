package mavericksoft.clinicmate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * This class tests the functionalities of the HealthRecord class using JUnit.
 *
 * @author Grant Marshall
 */
public class HealthRecordTest {
    private HealthRecord rec1;
    private HealthRecord rec2;
    private Patient pat1;

    @Before
    public void setUp() throws Exception {
        this.pat1 = new Patient("test2", "test" , "2", "password2", "female",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());

        // Add 2 records to test1
        this.rec1 = new HealthRecord(pat1.getPatientID(), new Date(), 0, 0, 0, 0);
        this.rec2 = new HealthRecord(pat1.getPatientID(), new Date(), 1, 1, 1, 1);
    }

    /**
     * This test verifies that the basic save and delete methods work
     */
    @Test
    public void testSaveAndDelete() {
        assert(!rec1.exists());
        rec1.save();
        assert(rec1.exists());
        rec1.delete();
        assert(!rec1.exists());
    }

    /**
     * This test verifies that comment methods work
     */
    @Test
    public void testComments() {
        rec1.save();
        rec1.addComment("foo");
        rec1.addComment("bar");
        assert(rec1.getComments().size() == 2);
        rec1.delete();
        rec1 = new HealthRecord(pat1.getPatientID(), new Date(), 0, 0, 0, 0);
        rec1.addComment("baz");
        assert(rec1.getComments().size() == 1);
    }

    /**
     * This method deletes the databases created by the setUp
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
