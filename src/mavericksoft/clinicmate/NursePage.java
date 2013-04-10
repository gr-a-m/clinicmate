/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
    private Label bloodLabel;
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
    private TextField bloodField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Patient[] patients= ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getPatients();
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
        String date=dateField.getText();
        String blood=bloodField.getText();
        String glucose=glucoseField.getText();
        String weight=weightField.getText();
        String observations=observationArea.getText();
        
        System.out.println("nurse saved patient info");
        //changeScene(".fxml",event);
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
