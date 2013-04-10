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
import javafx.scene.control.MenuItem;
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
    private MenuButton doctorMenu;
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
    private Label doctorNameLabel;
    @FXML
    private Label doctorPasswordLabel;
    @FXML
    private TextField doctorNameField;
    @FXML
    private TextField doctorPasswordField;
    @FXML
    private Tab editNursesTasb;
    @FXML
    private MenuButton nursesMenu;
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
    private Label nurseNameLabel;
    @FXML
    private Label nursePasswordLabel;
    @FXML
    private TextField nurseNameField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //add nurses and doctors to MenuButtons
        nursesMenu.getItems().addAll(new MenuItem("nurse1"), new MenuItem("nurse2"), new MenuItem("nurse3"));
        doctorMenu.getItems().addAll(new MenuItem("doc1"), new MenuItem("doc2"));
    }
    
    @FXML
    public void addDoctor(javafx.event.ActionEvent event) throws IOException
    {
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    doctorUsernameField.getText(),
                    doctorNameField.getText(), //need to make first name field
                    doctorNameField.getText(), //need to make last name field
                    doctorPasswordField.getText(),
                    false,false,true))
            {
                //doctorPatientsArea.getText();
                System.out.println("doctor created!");
            }
        }
        catch(Exception e){System.out.println("doctor not created");}
    }
    
    @FXML
    public void addNurse(javafx.event.ActionEvent event) throws IOException
    {
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    nurseUsernameField.getText(),
                    nurseNameField.getText(), //need to make first name field
                    nurseNameField.getText(), //need to make last name field
                    nursePasswordField.getText(),
                    false,true,false))
            {
                //doctorPatientsArea.getText();
                System.out.println("nurse created!");
            }
        }
        catch(Exception e){System.out.println("nurse not created");}
    }
    
    @FXML
    public void logOut(javafx.event.ActionEvent event) throws IOException
    {
        PermissionsController.getInstance().logout();
        new ClinicMatePage("loginPage.fxml",event,"Login");
    }
}