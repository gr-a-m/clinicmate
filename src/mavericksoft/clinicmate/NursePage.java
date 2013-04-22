/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

    
/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class NursePage implements Initializable {
    @FXML
    private Label patientsLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Label glucoseLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label observationsLabel;
    @FXML
    private Button addPatientButton;
    @FXML
    private TextField dateField;
    @FXML
    private TextField glucoseField;
    @FXML
    private TextField weightField;
    @FXML
    private TextArea observationArea;
    @FXML
    private Button logOutButton;
    @FXML
    private ListView<?> patientList;
    
    private Patient[] patients;
    @FXML
    private TextField systolicField;
    @FXML
    private Label systolicLabel;
    @FXML
    private Label diastolicLabel;
    @FXML
    private TextField diastolicField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        patients= ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getPatients();
        ArrayList<String> patientArray=new ArrayList<String>();
        //System.out.println("numOfPatients:"+patients.length);
        
        for(int i=0;i<patients.length;i++)
        {
            patientArray.add(patients[i].getLastName() + ", " + patients[i].getFirstName());
            //System.out.println(i+":"+patients[i].getLastName() + ", " + patients[i].getFirstName());
        }
        
        ObservableList items=FXCollections.observableArrayList(patientArray);
        patientList.setItems(items);
    }
    
    @FXML
    public void save(javafx.event.ActionEvent event) throws IOException
    {
        //String date=dateField.getText();
        Date date=new Date();
        int diastolic=Integer.parseInt(diastolicField.getText());
        int systolic=Integer.parseInt(systolicField.getText());
        int glucose=Integer.parseInt(glucoseField.getText());
        int weight=Integer.parseInt(weightField.getText());
        String observations=observationArea.getText();
        
        int index=patientList.getSelectionModel().getSelectedIndex();
        Patient selectedPatient=patients[index];
        UUID id=selectedPatient.getPatientID();
        //System.out.println("selected:"+selectedPatient.getFirstName());
        
        HealthRecordController.getInstance().addRecord(id,date,diastolic,systolic,glucose,weight);
        try{
            HealthRecord[] records=HealthRecordController.getInstance().getRecordsForPatient(id);
            UUID recordID = records[records.length-1].getRecordID();
            HealthRecordController.getInstance().addComment(recordID,observations);
        }catch(Exception ex){System.out.println("NonExistantRecordException");}
        
        System.out.println("nurse saved patient info");
    }
    
    @FXML
    public void addPatient(javafx.event.ActionEvent event) throws IOException
    {
        System.out.println("nurse to add patient");
        new ClinicMatePage("newPatient.fxml",event,"Add New Patient");
    }
    
    @FXML
    public void logOut(javafx.event.ActionEvent event) throws IOException
    {
        PermissionsController.getInstance().logout();
        new ClinicMatePage("loginPage.fxml",event,"Login");
    }
}
