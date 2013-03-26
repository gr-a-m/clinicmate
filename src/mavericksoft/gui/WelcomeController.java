/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.gui;

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
public class WelcomeController implements Initializable{
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
        System.out.println("1");
    }
    
    @FXML
    private void viewMetrics(javafx.event.ActionEvent event) throws IOException
    {
        changeScene("doctorPage.fxml",event);
    }

    @FXML
    private void updateContactInfo(javafx.event.ActionEvent event) throws IOException
    {
        changeScene("patientInfo.fxml",event);
    }
    
    public void changeScene(String fxml, javafx.event.ActionEvent event) throws IOException
    {
        Node node=(Node) event.getSource();
        Stage s=(Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.show();
    }
}
