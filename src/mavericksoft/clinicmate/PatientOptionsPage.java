/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.awt.event.ActionEvent;
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
import javafx.stage.Stage;

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
        changeScene("nursePage.fxml",event,"Nurse Accessibilities");
    }
    
    @FXML
    private void viewMetrics(javafx.event.ActionEvent event) throws IOException
    {
        changeScene("doctorPage.fxml",event,"Doctor Accessibilities");
    }

    @FXML
    private void updateContactInfo(javafx.event.ActionEvent event) throws IOException
    {
        changeScene("patientInfo.fxml",event,"Contact Information");
    }
    
    public void changeScene(String fxml, javafx.event.ActionEvent event, String title) throws IOException
    {
        Node node=(Node) event.getSource();
        Stage s=(Stage) node.getScene().getWindow();
        s.setTitle(title);
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.show();
    }
}
