/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

    
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
    
    private Hashtable daysInMonth= new Hashtable();
    private ObservableList<String> months;
    @FXML
    private ComboBox<?> dayComboBox;
    @FXML
    private ComboBox<?> yearComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;

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
        
        months=FXCollections.observableArrayList(
                "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
        monthComboBox.setItems(months);

        //add years to MenuButton
        //yearMenu.getItems().addAll(new MenuItem("1980"));
        for(int i=2013;i>=1900;i--)
        {
            yearComboBox.getItems().addAll(i);
        }
        //add days to hashtable
        daysInMonth.put("Jan","31");
        daysInMonth.put("Feb","28"); //need to make this dependent on year
        daysInMonth.put("Mar","31");
        daysInMonth.put("Apr","30");
        daysInMonth.put("May","31");
        daysInMonth.put("Jun","30");
        daysInMonth.put("Jul","31");
        daysInMonth.put("Aug","31");
        daysInMonth.put("Sep","30");
        daysInMonth.put("Oct","31");
        daysInMonth.put("Nov","30");
        daysInMonth.put("Dec","31");
        
        //get days in month
        monthComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldString, String newString)
            {
                System.out.println("days:"+daysInMonth.get(newString));
                for(int i=1;i<=Integer.parseInt(daysInMonth.get(newString).toString());i++)
                {
                    dayComboBox.getItems().addAll(i);
                }
          }
        });
    }
    
    @FXML
    public void save(javafx.event.ActionEvent event) throws IOException
    {
        boolean save=true;
        Date date=null;
        int diastolic=-1;
        int systolic=-1;
        int glucose=-1;
        int weight=-1;
        String observations="";
        UUID id=null;
        try
        {
            
            if(monthComboBox.getSelectionModel().selectedItemProperty().getValue()==null ||
                    dayComboBox.getSelectionModel().selectedItemProperty().getValue()==null ||
                    yearComboBox.getSelectionModel().selectedItemProperty().getValue()==null)
            {
                System.out.println("NULL DATE");
                save=false;
            }
                int month=months.indexOf(monthComboBox.getSelectionModel().selectedItemProperty().getValue())+1;
                int day=Integer.parseInt(dayComboBox.getSelectionModel().selectedItemProperty().getValue().toString());
                int year=Integer.parseInt(yearComboBox.getSelectionModel().selectedItemProperty().getValue().toString())-1900;
                date = new Date(year,month,day);
                System.out.println(month + "/" + day + "/" + year);
        }
        catch(Exception ex){}
        
        if(diastolicField.getText().isEmpty() || systolicField.getText().isEmpty() ||
                glucoseField.getText().isEmpty() || weightField.getText().isEmpty())
        {
            System.out.println("textfields are empty");
            save=false;
        }
        else
        {
            diastolic=Integer.parseInt(diastolicField.getText());
            systolic=Integer.parseInt(systolicField.getText());
            glucose=Integer.parseInt(glucoseField.getText());
            weight=Integer.parseInt(weightField.getText());
            observations=observationArea.getText();
        }

        
        if(patientList.getSelectionModel().getSelectedItem()==null)
        {
            save=false;
            System.out.println("patient not selected");
        }
        else
        {
            int index=patientList.getSelectionModel().getSelectedIndex();
            Patient selectedPatient=patients[index];
            id=selectedPatient.getPatientID();
            //System.out.println("selected:"+selectedPatient.getFirstName());
        }

        if(save)
        {
            try{
                HealthRecordController.getInstance().addRecord(id,date,diastolic,systolic,glucose,weight);
                //HealthRecord[] records=HealthRecordController.getInstance().getRecordsForPatient(id);
                //UUID recordID = records[records.length-1].getRecordID();
                //HealthRecordController.getInstance().addComment(recordID,observations);
            }catch(Exception ex){System.out.println("NonExistantRecordException");}

            System.out.println("nurse saved patient info");
            successLabel.setVisible(true);
            errorLabel.setVisible(false);
        }
        else
        {
            successLabel.setVisible(false);
            errorLabel.setVisible(true);
        }
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
