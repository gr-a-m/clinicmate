/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

    
/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class NursePage implements Initializable {
    @FXML
    private ScrollPane patientScrollPane;
    @FXML
    private AnchorPane patientAnchorPane;
    @FXML
    private Label patientsLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Label bloodLabel;
    @FXML
    private Label glucoseLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label observationsLabel;
    @FXML
    private Button addPatientButton;
    @FXML
    private TextField dateField;
    @FXML
    private TextField bloodField;
    @FXML
    private TextField glucoseField;
    @FXML
    private TextField weightField;
    @FXML
    private TextArea observationArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    
    @FXML
    public void save(javafx.event.ActionEvent event) throws IOException
    {
        String date=dateField.getText();
        String blood=bloodField.getText();
        String glucose=glucoseField.getText();
        String weight=weightField.getText();
        String observations=observationArea.getText();
        
        System.out.println("nurse saved patient info");
        //changeScene(".fxml",event);
    }
    
    @FXML
    public void addPatient(javafx.event.ActionEvent event) throws IOException
    {
        System.out.println("nurse to add patient");
        //changeScene("addPatient.fxml",event);
    }
    
    public void changeScene(String fxml, javafx.event.ActionEvent event) throws IOException
    {
        Node node=(Node) event.getSource();
        Stage s=(Stage) node.getScene().getWindow();
        //s.setTitle("Add Patient");
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.show();
    }
}
