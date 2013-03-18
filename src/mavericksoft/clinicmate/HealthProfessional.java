package mavericksoft.clinicmate;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 */
class HealthProfessional extends Person {
    private UUID employeeID;
    private boolean admin;
    private boolean nurse;
    private boolean doctor;
    private ArrayList<UUID> patientIDs;

    public HealthProfessional(String firstName, String lastName,
                              String password, boolean admin, boolean nurse,
                              boolean doctor) {
        super(firstName, lastName, password);

        // Generate a UUID
        this.employeeID = UUID.randomUUID();

        // Fill in the rest of the fields
        this.admin = admin;
        this.nurse = nurse;
        this.doctor = doctor;

        // A new HealthProfessional has no Patients
        this.patientIDs = new ArrayList<UUID>();
    }

    // TODO
    boolean save() {
        return false;
    }

    // TODO
    boolean delete() {
        return false;
    }

    // TODO
    public Patient[] getPatients() {
        return new Patient[0];
    }
}
