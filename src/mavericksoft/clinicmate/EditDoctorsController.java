/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Dan
 */
public class EditDoctorsController implements Initializable {
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
