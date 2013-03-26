/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class PatientInfoController implements Initializable
{
    @FXML
    private Label contactInfoLabel;
    @FXML
    private Font x1;
    @FXML
    private Label emergencyLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label insuranceLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label primaryPhoneLabel;
    @FXML
    private Label secondaryPhoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField secondaryPhoneField;
    @FXML
    private TextField primaryPhoneField;
    @FXML
    private TextArea addressTextArea;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField insuranceField;
    @FXML
    private TextField emergencyNameField;
    @FXML
    private TextField emergencyPrimaryPhoneField;
    @FXML
    private TextField emergencySecondaryPhoneField;
    @FXML
    private Label emergencyNameLabel;
    @FXML
    private Label emergencyPrimaryPhoneLabel;
    @FXML
    private Label emergencySecondaryPhoneLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Label invalidLabel1;
    @FXML
    private Label invalidLabel2;
    @FXML
    private Label invalidLabel3;
    @FXML
    private Label invalidLabel5;
    @FXML
    private Label invalidLabel4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    
    @FXML
    private void saveContactInfo(javafx.event.ActionEvent event) throws IOException
    {
        boolean noErrors=true;
        
        String firstName=firstNameField.getText();
        String lastName=lastNameField.getText();
        String primaryPhone=primaryPhoneField.getText();
        String secondaryPhone=secondaryPhoneField.getText();
        String address=addressTextArea.getText();
        String email=emailField.getText();
        String insurance=insuranceField.getText();
        String password=passwordField.getText();
        String emergencyName=emergencyNameField.getText();
        String emergencyPrimary=emergencyPrimaryPhoneField.getText();
        String emergencySecondary=emergencySecondaryPhoneField.getText();
        
        //mavericksoft.clinicmate.Patient patient=new mavericksoft.clinicmate.Patient();
        
        if(firstName.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            //patient.setfirstName(firstName);
        }
        if(lastName.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            //patient.setLastName(lastName);
        }
        if(secondaryPhone.length()!=10 || !isNumber(secondaryPhone))
        {
            //invalid number input
            invalidLabel2.setVisible(true);
            noErrors=false;
        }
        else
        {
            //valid number input
            //patient.setSecondaryPhoneNumber(secondaryPhone);
            invalidLabel2.setVisible(false);
        }
        if(primaryPhone.length()!=10 || !isNumber(primaryPhone))
        {
            //invalid number input
            invalidLabel1.setVisible(true);
            noErrors=false;
        }
        else
        {
            //valid number input
            //patient.setPrimaryPhoneNumber(primaryPhone);
            invalidLabel1.setVisible(false);
        }
        if(address.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            //patient.setAddress(address);
        }
        if(!email.contains("@") || !email.contains("."))
        {
            //invalid email entry if there is no @ or .
            invalidLabel3.setVisible(true);
            noErrors=false;
        }
        else
        {
            //patient.setEmail(email);
            invalidLabel3.setVisible(false);
        }
        if(password.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            //patient.setPassword(password);
        }
        if(insurance.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            //patient.setInsuranceProvider(insurance);
        }
        if(emergencyName.isEmpty())
        {
            noErrors=false;
        }
        else
        {
            
        }
        if(emergencyPrimary.length()!=10 || !isNumber(emergencyPrimary))
        {
            invalidLabel4.setVisible(true);
            noErrors=false;
        }
        else
        {
            invalidLabel4.setVisible(false);
        }
        if(emergencySecondary.length()!=10 || !isNumber(emergencySecondary))
        {
            invalidLabel5.setVisible(true);
            noErrors=false;
        }
        else
        {
            invalidLabel5.setVisible(false);
        }
        
        //Only save data if there are no data entry errors
        if(noErrors)
        {
            System.out.println("saved");
            //store info...to be done
            backToWelcome(event);
        }
    }
    
    //this method determines if the input is purely a number (contains no other characters besides numbers)
    public boolean isNumber(String num)
    {
        try
        {
            Long.parseLong(num);
        }
        catch(NumberFormatException ex)
        {
            // is not a number
            return false;
        }
        //is a pure number if exception is not caught
        return true;
    }
    
    public void backToWelcome(javafx.event.ActionEvent event) throws IOException
    {
        Node node=(Node) event.getSource();
        Stage s=(Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.show();
    }
}
