package mavericksoft.clinicmate;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * This class is a test class for HealthRecordController.java. Though it uses
 * JUnit, in many ways these tests act as integration tests because of the
 * close relations between the controller classes and database-interfacing
 * classes.
 *
 * @author Grant Marshall
 */
public class HealthRecordControllerTest {
    private Patient pat1;
    private Patient pat2;
    private HealthProfessional hp1;

    /**
     * This method is executed once before all of the tests for the class.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        Class.forName("org.h2.Driver");
    }

    /**
     * This method is executed before each test. It creates dummy data that is
     * recreated each time to avoid side-effects from previous tests.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Person.checkTables();
        this.pat1 = new Patient("test1", "test" , "1", "password", "male",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());
        this.pat2 = new Patient("test2", "test" , "2", "password2", "female",
                "111 E St", "insuroco", "5555555555", "1111111111", new Date());
        this.hp1 = new HealthProfessional("pro1", "Pro", "1", "pass1",
                false, true, false);
    }

    /**
     * This method is executed after each test. It deletes the data created
     * during the test in the database. This avoids any data from crossing
     * between the test cases.
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

    /**
     * This test simply makes sure that the instance given is not null and that
     * each invocation of getInstance() returns the same object.
     *
     * @throws Exception
     */
    @Test
    public void testGetInstance() throws Exception {
        HealthRecordController copy1 = HealthRecordController.getInstance();
        assert(HealthRecordController.getInstance() != null);
        assert(copy1 == HealthRecordController.getInstance());
    }

    /**
     * This method tests that the comments being added to the records work
     * correctly.
     *
     * @throws Exception
     */
    @Test
    public void testAddComment() throws Exception {
        // Save the nurse and login as that person
        hp1.save();
        assert (PermissionsController.getInstance().logon("pro1", "pass1"));
        assert (PermissionsController.getInstance().currentUserPermissions() ==
                Permissions.NURSE);

        // Save patient 1 and add a HealthRecord to them
        pat1.save();
        HealthRecord hr = HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(), 1, 1, 1, 1);

        HealthRecordController hrc = HealthRecordController.getInstance();

        // Add the patient to the nurse
        hp1.addPatient(pat1.getPatientID());
        hp1.save();

        // Add the comments to the created HealthRecord
        hrc.addComment(hr.getRecordID(), "Hi There!");
        hrc.addComment(hr.getRecordID(), "Second!");

        // The HealthRecord associated with pat1 should have 2 comments
        assert (((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getPatients()[0].getHealthRecords()[0].getComments().size() == 2);
    }

    /**
     * This method tests that records are added to the database successfully.
     * At the same time, this method tests getRecordsForPatient in validation.
     *
     * @throws Exception
     */
    @Test
    public void testAddRecord() throws Exception {
        // Save pat1
        pat1.save();

        // pat1 should start with no records
        assert (pat1.getHealthRecords().length == 0);

        // Add one health record
        HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(), 1, 1, 1, 1);
        assert (pat1.getHealthRecords().length == 1);

        // Add another health record
        HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(), 2, 2, 2, 2);
        assert (pat1.getHealthRecords().length == 2);

        // Delete them both
        HealthRecord[] hrs = HealthRecordController.getInstance().getRecordsForPatient(pat1.getPatientID());
        for (HealthRecord hr : hrs) {
            hr.delete();
        }

        // Now this patient should have no HealthRecords
        assert (pat1.getHealthRecords().length == 0);
    }

    /**
     * This method tests the validity of the linear regression models generated
     * by HealthRecordController.linearRegression().
     */
    @Test
    public void testLinearRegression() {
        pat1.save();

        // We need to create three HealthRecords for the patient with
        // different slopes and intercepts for each stat
        HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(2000, 1, 1), 50, 70, 100, 120);
        HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(2000, 1, 2), 60, 90, 130, 120);
        HealthRecordController.getInstance().addRecord(pat1.getPatientID(), new Date(2000, 1, 3), 70, 110, 160, 120);

        try {
            SimpleRegression reg = HealthRecordController.getInstance().linearRegression(pat1.getPatientID(),
                    new Date(2000, 1, 1), new Date(2000, 1, 3), RegressionTypes.DIA);
            assert (reg.getIntercept() == 50.0 && reg.getSlope() == 10.0);
            reg = HealthRecordController.getInstance().linearRegression(pat1.getPatientID(),
                    new Date(2000, 1, 1), new Date(2000, 1, 3), RegressionTypes.SYS);
            assert (reg.getIntercept() == 70.0 && reg.getSlope() == 20.0);
            reg = HealthRecordController.getInstance().linearRegression(pat1.getPatientID(),
                    new Date(2000, 1, 1), new Date(2000, 1, 3), RegressionTypes.GLUCOSE);
            assert (reg.getIntercept() == 100.0 && reg.getSlope() == 30.0);
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
        }
    }
}
