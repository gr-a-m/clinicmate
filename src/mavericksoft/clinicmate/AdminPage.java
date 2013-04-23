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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
    
    public HealthProfessional[] nurses;
    public HealthProfessional[] doctors;
    
    @FXML
    private Color x2;
    @FXML
    private Label notDeletedLabel;
    @FXML
    private Label deletedLabel;
    
    private Patient[] patients;
    
    private HealthProfessional currentNurse;
    private HealthProfessional currentDoctor;
    @FXML
    private ListView<?> doctorPatientList;
    @FXML
    private ListView<?> doctorList;
    @FXML
    private Label doctorLabel;
    @FXML
    private ListView<?> nursePatientList;
    @FXML
    private ListView<?> nurseList;
    @FXML
    private Label nurseLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        currentDoctor=null;
        currentNurse=null;
        updateNurseList();
        updateDoctorList();
    }
    
    @FXML
    public void addDoctor(javafx.event.ActionEvent event) throws IOException
    {
        resetLabels();
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    doctorUsernameField.getText(),
                    doctorFirstNameField.getText(),
                    doctorLastNameField.getText(),
                    doctorPasswordField.getText(),
                    false,false,true))
            {
                System.out.println("doctor created!");
                successLabel.setVisible(true);
                doctorUsernameField.setText("");
                doctorFirstNameField.setText("");
                doctorLastNameField.setText("");
                doctorPasswordField.setText("");
                updateDoctorList();
            }
        }
        catch(Exception e)
        {
            System.out.println("doctor not created");
            errorLabel.setVisible(true);
        }
    }
    
    @FXML
    public void addNurse(javafx.event.ActionEvent event) throws IOException
    {
        resetLabels();
        try{
            if(PersonController.getInstance().createNurseOrDoctor(
                    nurseUsernameField.getText(),
                    nurseFirstNameField.getText(),
                    nurseLastNameField.getText(),
                    nursePasswordField.getText(),
                    false,true,false))
            {
                System.out.println("nurse created!");
                successLabel.setVisible(true);
                nurseUsernameField.setText("");
                nurseFirstNameField.setText("");
                nurseLastNameField.setText("");
                nursePasswordField.setText("");
                updateNurseList();
            }
        }
        catch(Exception e)
        {
            System.out.println("nurse not created");
            errorLabel.setVisible(true);
        }
    }
    
    @FXML
    public void deleteDoctor(javafx.event.ActionEvent event) throws IOException
    {
        resetLabels();
        if(currentDoctor!=null)
        {
            if(currentDoctor.delete())
            {
                System.out.println("Deleted.");
                currentDoctor=null;
                deletedLabel.setVisible(true);
                doctorPatientList.setItems(null);
                updateDoctorList();
            }
            else
            {
                notDeletedLabel.setVisible(true);
            }
        }
    }
    
    @FXML
    public void deleteNurse(javafx.event.ActionEvent event) throws IOException
    {
        resetLabels();
        if(currentNurse!=null)
        {
            if(currentNurse.delete())
            {
                System.out.println("Deleted.");
                deletedLabel.setVisible(true);
                currentNurse=null;
                nursePatientList.setItems(null);
                updateNurseList();
            }
            else
            {
                notDeletedLabel.setVisible(true);
            }
        }
    }
    
    @FXML
    public void logOut(javafx.event.ActionEvent event) throws IOException
    {
        PermissionsController.getInstance().logout();
        new ClinicMatePage("loginPage.fxml",event,"Login");
    }
    
    public void resetLabels()
    {
        deletedLabel.setVisible(false);
        notDeletedLabel.setVisible(false);
        successLabel.setVisible(false);
        errorLabel.setVisible(false);
        //System.out.println("tab changed");
    }
    
        public void tabChanged(javafx.event.ActionEvent event) throws IOException
    {
        deletedLabel.setVisible(false);
        notDeletedLabel.setVisible(false);
        successLabel.setVisible(false);
        errorLabel.setVisible(false);
        System.out.println("tab changed");
    }
    
    @FXML
    public void updateDoctorPatientList(MouseEvent event) throws ArrayIndexOutOfBoundsException
    {
        if(doctorList.getSelectionModel().getSelectedItem()!=null)
            updateDoctorPatientList();
    }
    public void updateDoctorPatientList() throws NullPointerException
    {
        int index=doctorList.getSelectionModel().getSelectedIndex();
        currentDoctor=doctors[index];
        
        patients= currentDoctor.getPatients();
        ArrayList<String> patientList=new ArrayList<String>();
            
        for(Patient patient: patients)
        {
            patientList.add(patient.getLastName() + ", " + patient.getFirstName());
        }
        
        ObservableList items=FXCollections.observableArrayList(patientList);
        
        try{
            doctorPatientList.setItems(items);
        }catch(Exception ex){System.out.println("list is empty");}
    }
    public void updateDoctorList()
    {
        ObservableList items;
            try{
            doctors= PersonController.getInstance().getAllDoctors();
            }catch(Exception ex){}
            if(doctors!=null)
            {
                ArrayList<String> docs=new ArrayList<String>();
                for(HealthProfessional pro: doctors)
                {
                    docs.add(pro.getName());
                }
                items=FXCollections.observableArrayList(docs);
                doctorList.setItems(items);
            }
    }
    
    @FXML
    public void updateNursePatientList(MouseEvent event) throws ArrayIndexOutOfBoundsException
    {
        if(nurseList.getSelectionModel().getSelectedItem()!=null)
            updateNursePatientList();
    }
    public void updateNursePatientList() throws NullPointerException
    {
        int index=nurseList.getSelectionModel().getSelectedIndex();
        currentNurse=nurses[index];
        
        patients= currentNurse.getPatients();
        ArrayList<String> patientList=new ArrayList<String>();
            
        for(Patient patient: patients)
        {
            patientList.add(patient.getLastName() + ", " + patient.getFirstName());
        }
        
        ObservableList items=FXCollections.observableArrayList(patientList);
        
        try{
            nursePatientList.setItems(items);
        }catch(Exception ex){System.out.println("list is empty");}
    }
    public void updateNurseList()
    {
        ObservableList items;
            try{
            nurses= PersonController.getInstance().getAllNurses();
            }catch(Exception ex){}
            if(nurses!=null)
            {
                ArrayList<String> nurs=new ArrayList<String>();
                for(HealthProfessional pro: nurses)
                {
                    nurs.add(pro.getName());
                }
                items=FXCollections.observableArrayList(nurs);
                nurseList.setItems(items);
            }
    }
    /*
    public void updateLists()
    {
        //add nurses and doctors to the lists
        //try
        //{
            ObservableList items=null,items2=null;
            try{
            doctors= PersonController.getInstance().getAllDoctors();
            nurses= PersonController.getInstance().getAllNurses();
            }catch(Exception ex){}
            
            if(doctors!=null)
            {
                ArrayList<String> docs=new ArrayList<String>();
                for(HealthProfessional pro: doctors)
                {
                    docs.add(pro.getName());
                }
                items=FXCollections.observableArrayList(docs);
                doctorList.setItems(items);
            }
            if(nurses!=null)
            {
                ArrayList<String> nurs=new ArrayList<String>();
                for(HealthProfessional pro: nurses)
                {
                    nurs.add(pro.getName());
                }
                items2=FXCollections.observableArrayList(nurs);
                nurseList.setItems(items2);
            }            
        //}catch(Exception ex){ex.printStackTrace();}
    }*/
}
