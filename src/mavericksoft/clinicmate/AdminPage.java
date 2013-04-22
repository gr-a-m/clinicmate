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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Dan
 */
public class AdminPage implements Initializable {
    @FXML
    private Tab editDoctorsTab;
    @FXML
    private Button deleteDoctorButton;
    @FXML
    private Label doctorPatientsLabel;
    @FXML
    private Font x1;
    @FXML
    private TextArea doctorPatientsArea;
    @FXML
    private Tab addDoctorTab;
    @FXML
    private Button doctorCreateButton;
    @FXML
    private Label doctorPasswordLabel;
    @FXML
    private TextField doctorPasswordField;
    @FXML
    private Tab editNursesTasb;
    @FXML
    private Button deleteNurseMenu;
    @FXML
    private Label nursePatientsLabel;
    @FXML
    private TextArea nursePatientsArea;
    @FXML
    private Tab addNurseTab;
    @FXML
    private Button createNurseButton;
    @FXML
    private Label nursePasswordLabel;
    @FXML
    private TextField nursePasswordField;
    @FXML
    private Button logOutButton;
    @FXML
    private TextField doctorUsernameField;
    @FXML
    private Label docUsernameLabel;
    @FXML
    private Label nurseUsernameLabel;
    @FXML
    private TextField nurseUsernameField;
    @FXML
    private ComboBox<?> doctorComboBox;
    @FXML
    private ComboBox<?> nurseComboBox;
    @FXML
    private Label doctorFirstNameLabel;
    @FXML
    private TextField doctorFirstNameField;
    @FXML
    private Label doctorLastNameLabel;
    @FXML
    private TextField doctorLastNameField;
    @FXML
    private Label nurseFirstNameLabel;
    @FXML
    private TextField nurseFirstNameField;
    @FXML
    private Label nurseLastNameLabel;
    @FXML
    private TextField nurseLastNameField;
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
        //add nurses and doctors to ComboBoxes
        HealthProfessional[] nurses;
        HealthProfessional[] doctors;
        try
        {
            doctors= ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getAllDoctors();
            for(int i=0;i<doctors.length;i++)
            {
                HealthProfessional doc=doctors[i];
                doctorComboBox.getItems().addAll(doc.getName());
            }
        }
        catch(Exception ex){System.out.println("No doctors found.");}
    }
    
    @FXML
    public void addDoctor(javafx.event.ActionEvent event) throws IOException
    {
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    doctorUsernameField.getText(),
                    doctorFirstNameField.getText(),
                    doctorLastNameField.getText(),
                    doctorPasswordField.getText(),
                    false,false,true))
            {
                System.out.println("doctor created!");
                errorLabel.setVisible(false);
                successLabel.setVisible(true);
            }
        }
        catch(Exception e)
        {
            System.out.println("doctor not created");
            errorLabel.setVisible(true);
            successLabel.setVisible(false);
        }
    }
    
    @FXML
    public void addNurse(javafx.event.ActionEvent event) throws IOException
    {
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    nurseUsernameField.getText(),
                    nurseFirstNameField.getText(),
                    nurseLastNameField.getText(),
                    nursePasswordField.getText(),
                    false,true,false))
            {
                System.out.println("nurse created!");
                errorLabel.setVisible(false);
                successLabel.setVisible(true);
            }
        }
        catch(Exception e)
        {
            System.out.println("nurse not created");
            errorLabel.setVisible(true);
            successLabel.setVisible(false);
        }
    }
    
    @FXML
    public void deleteDoctor(javafx.event.ActionEvent event) throws IOException
    {
        System.out.println("to be deleted");
    }
    
    @FXML
    public void deleteNurse(javafx.event.ActionEvent event) throws IOException
    {
        System.out.println("to be deleted");
    }
    
    @FXML
    public void logOut(javafx.event.ActionEvent event) throws IOException
    {
        PermissionsController.getInstance().logout();
        new ClinicMatePage("loginPage.fxml",event,"Login");
    }
}
