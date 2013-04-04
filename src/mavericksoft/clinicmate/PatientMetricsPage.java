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
import javafx.scene.control.ToggleButton;

/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class PatientMetricsPage implements Initializable
{
    @FXML
    private ToggleButton bloodLinearRegToggle;
    @FXML
    private ToggleButton weightLinearRegToggle;
    @FXML
    private ToggleButton glucoseLinearRegToggle;
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
        new ClinicMatePage("patientOptions.fxml",event,"Welcome");
    }
}