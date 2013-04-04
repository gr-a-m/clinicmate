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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import java.util.Date;

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
    private MenuButton monthMenu;
    @FXML
    private MenuButton dayMenu;
    @FXML
    private MenuButton yearMenu;
    @FXML
    private MenuButton doctorField;
    @FXML
    private Button doneButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }
    
    @FXML
    public void done(javafx.event.ActionEvent event) throws IOException
    {
        emialField.getText();
        emergencyContactField.getText();
        emergencyContactPrimaryPhoneField.getText();
        emergencyContactSecondaryPhoneField.getText();
        passwordField.getText();
        
        /*
        private MenuButton monthMenu;
        private MenuButton dayMenu;
        private MenuButton yearMenu;
        private MenuButton doctorField;
        */
        
        //Create new patient
        try{
        PersonController.getInstance().createPatient(
                "patientUsername",
                firstNameField.getText(),
                lastNameField.getText(),
                passwordField.getText(),
                genderField.getText(),
                addressField.getText(),
                insuranceField.getText(),
                primaryPhoneField.getText(),
                SecondaryPhoneField.getText(),
                new Date()); //date not in place yet
        System.out.println("success");
        }
        catch(Exception e){System.out.println("failed to create patient");}
        
        new ClinicMatePage("nursePage.fxml",event,"Nurse Accessibilities");
    }
}
