package mavericksoft.clinicmate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * This class tests the major functionalities of the HealthProfessional class.
 * It tests most of the major methods of the class at some point.
 *
 * @author Grant Marshall
 */
public class HealthProfessionalTest {
    private Patient pat1;
    private Patient pat2;
    private HealthProfessional pro1;
    private HealthProfessional pro2;
    private HealthRecord rec1;
    private HealthRecord rec2;

    /**
     * Sets up the tests by creating a few sample objects and connects the
     * Driver for H2.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Load the H2 SQL driver
        Class.forName("org.h2.Driver");

        Person.checkTables();

        this.pat1 = new Patient("test1", "test" , "1", "password", "male",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());

        this.pat2 = new Patient("test2", "test" , "2", "password2", "female",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());

        // Add 2 records to test1
        this.rec1 = new HealthRecord(pat1.getPatientID(), new Date(), 0, 0, 0, 0);
        this.rec2 = new HealthRecord(pat1.getPatientID(), new Date(), 1, 1, 1, 1);

        this.pro1 = new HealthProfessional("pro1", "Pro", "1", "pass1", false, true, false);
        this.pro2 = new HealthProfessional("pro2", "Pro", "2", "pass2", false, false, true);
    }

    @Test
    public void testResetPassword() {
        assert(!pro1.exists());
        pro1.save();
        assert(pro1.exists());
        assert(pro1.checkPassword("pass1"));
        pro1.resetPassword("pass1", "pass3");
        pro1.save();
        assert(pro1.checkPassword("pass3"));
    }

    @After
    public void tearDown() throws Exception {
        File dbFile = new File("./data/clinicmate.h2.db");
        dbFile.delete();
        dbFile = new File("./data/clinicmate.trace.db");
        dbFile.delete();
    }
}
