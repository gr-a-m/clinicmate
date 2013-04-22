/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * FXML Controller class
 *
 * @author Dan
 */
public class NewPatientPage implements Initializable {
    @FXML
    private Label firstNameLabel;
    @FXML
    private Font x1;
    @FXML
    private Label genderLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label secondaryPhoneNumberLabel;
    @FXML
    private Label primaryPhoneNumberLabel;
    @FXML
    private Label insuranceProviderLabel;
    @FXML
    private Label emergencyContactPrimaryPhoneLabel;
    @FXML
    private Label doctorLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label EmergencyContactSecondaryPhoneLabel;
    @FXML
    private Label emergencyContactLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField insuranceField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField primaryPhoneField;
    @FXML
    private TextField SecondaryPhoneField;
    @FXML
    private TextField emialField;
    @FXML
    private TextField emergencyContactField;
    @FXML
    private TextField emergencyContactPrimaryPhoneField;
    @FXML
    private TextField emergencyContactSecondaryPhoneField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button doneButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private ComboBox<String> monthComboBox;
    
    private Hashtable daysInMonth= new Hashtable();
    private ObservableList<String> months;
    @FXML
    private ComboBox<?> dayComboBox;
    @FXML
    private ComboBox<?> yearComboBox;
    @FXML
    private ComboBox<?> doctorComboBox;
    @FXML
    private Label errorLabel;
    
    private HealthProfessional[] doctors;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        errorLabel.setVisible(false);
        
        //add doctors to MenuButton
        try
        {
            doctors= ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getAllDoctors();
            for(int i=0;i<doctors.length;i++)
            {
                HealthProfessional doc=doctors[i];
                doctorComboBox.getItems().addAll(doc.getName());
            }
            //doctorComboBox.getItems().addAll(doctors.getName());
        }
        catch(Exception ex){System.out.println("No doctors found.");}
        //add months to combobox
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
    public void done(javafx.event.ActionEvent event) throws IOException
    {
        //get Date
        boolean dateError=false;
        Date date=null;
        try
        {
            int month=months.indexOf(monthComboBox.getSelectionModel().selectedItemProperty().getValue());
            int day=Integer.parseInt(dayComboBox.getSelectionModel().selectedItemProperty().getValue().toString());
            int year=Integer.parseInt(yearComboBox.getSelectionModel().selectedItemProperty().getValue().toString())-1900;
            date = new Date(year,month,day);
            //System.out.println(month + "/" + day + "/" + year);
        }
        catch(Exception ex)
        {
            dateError=true;
        }
        
        if(emialField.getText().isEmpty() ||
            emergencyContactField.getText().isEmpty() ||
            emergencyContactPrimaryPhoneField.getText().isEmpty() ||
            emergencyContactSecondaryPhoneField.getText().isEmpty() ||
            passwordField.getText().isEmpty() ||
            usernameField.getText().isEmpty() ||
            firstNameField.getText().isEmpty() ||
            lastNameField.getText().isEmpty() ||
            passwordField.getText().isEmpty() ||
            genderField.getText().isEmpty() ||
            addressField.getText().isEmpty() ||
            insuranceField.getText().isEmpty() ||
            primaryPhoneField.getText().isEmpty() ||
            SecondaryPhoneField.getText().isEmpty() ||
            dateError)
        {
            errorLabel.setVisible(true);
        }
        else
        {
            //Create new patient
            Patient patient= PersonController.getInstance().createPatient(
                    usernameField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    passwordField.getText(),
                    genderField.getText(),
                    addressField.getText(),
                    insuranceField.getText(),
                    primaryPhoneField.getText(),
                    SecondaryPhoneField.getText(),
                    date);
            UUID id=patient.getPatientID();
            String selected = doctorComboBox.getValue().toString();
            int i;
            for(i=0;i<doctors.length;i++)
            {
                if(doctors[i].getName().equals(selected))
                {
                    break;
                }
            }
            try{
            HealthProfessional doc=doctors[i];
            //add patient to selected doctor
            doc.addPatient(id);
            System.out.println("patient added to doctor "+doc.getName());
            }catch(Exception ex){System.out.println("doc not found");}
            
            try{
            if(patient!=null)
            {
                System.out.println("success");
                //add patient to nurse
                ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).addPatient(patient.getPatientID());
                new ClinicMatePage("nursePage.fxml",event,"Nurse Accessibilities");     
            }
            else
            {
                errorLabel.setVisible(true);
                System.out.println("failed");
            }
            }//end try
            catch(Exception e){System.out.println("failed to create patient");}
        }
    }
}
