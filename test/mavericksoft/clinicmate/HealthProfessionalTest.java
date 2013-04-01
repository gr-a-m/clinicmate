package mavericksoft.clinicmate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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

    /**
     * This method tests that the Permissions methods of Person and
     * HealthProfessional. It also serves as an example of how to interact with
     * the permissions of people.
     */
    @Test
    public void testPermissions() {
        pro1.save();
        pro2.save();
        assert(pro1.exists() && pro2.exists());
        assert(pro1.isNurse());
        assert(Person.getPermissions("pro1") == Permissions.NURSE);
        assert(!pro2.isAdmin() && !pro2.isNurse());
        assert(Person.getPermissions("pro2") == Permissions.DOCTOR);
        assert(Person.getPermissions("pro3") == null);
        pro1.delete();
        assert(Person.getPermissions("pro1") == null);
    }

    /**
     * This method tests the reset password utility of the HealthProfessional
     */
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

    /**
     * This method tests the addition of patients to a HealthProfessional
     */
    @Test
    public void testGetPatients() {
        assert(!pro2.exists());
        pro2.save();
        assert(pro2.exists());

        // Save the patients and add them to the professional
        pat1.save();
        pat2.save();
        assert(pat1.exists() && pat2.exists());
        pro2.addPatient(pat1.getPatientID());
        pro2.addPatient(pat2.getPatientID());
        pro2.save();

        assert(pro2.getPatients().length == 2);

        ArrayList<UUID> ids = pro2.getPatientIDs();

        // Check that the ids of the patients retrieved match
        for (UUID id: ids) {
            Patient[] pats = pro2.getPatients();
            assert(id.equals(pats[0].getPatientID()) || id.equals(pats[1].getPatientID()));
        }
    }

    /**
     * This method tests that getById() works
     */
    @Test
    public void testGetById() {
        pro1.save();
        try {
            assert(pro1.getUsername().equals(HealthProfessional.getById(pro1.getEmployeeID()).getUsername()));
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
        }
    }

    /**
     * This method tests that getByUsername() works correctly
     */
    @Test
    public void testGetByUsername() {
        pro2.save();
        try {
            assert(pro2.getEmployeeID().toString().equals(HealthProfessional.getByUsername(pro2.getUsername()).getEmployeeID().toString()));
        } catch (NonexistentRecordException nre) {
            nre.printStackTrace();
        }
    }

    /**
     * This method tests that save() and delete() works correctly
     */
    @Test
    public void testSaveAndDelete() {
        assert(!pro1.exists());
        pro1.save();
        assert(pro1.exists());
        pro1.delete();
        assert(!pro1.exists());
    }

    /**
     * This method deletes the database created during setUp.
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
