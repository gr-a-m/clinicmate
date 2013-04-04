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

/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class PatientOptionsPage implements Initializable{
    @FXML
    private Button dataButton;
    @FXML
    private Button viewMetricsButton;
    @FXML
    private Button contactInfoButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    @FXML
    private void enterData(javafx.event.ActionEvent event) throws IOException
    {
        new ClinicMatePage("dataEntry.fxml",event,"Enter Data");
    }
    
    @FXML
    private void viewMetrics(javafx.event.ActionEvent event) throws IOException
    {
        //new ClinicMatePage("doctorPage.fxml",event,"Doctor Accessibilities");
    }

    @FXML
    private void updateContactInfo(javafx.event.ActionEvent event) throws IOException
    {
        new ClinicMatePage("patientInfo.fxml",event,"Contact Information");
    }
}
