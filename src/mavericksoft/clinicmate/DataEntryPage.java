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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Dan
 */
public class DataEntryPage implements Initializable {
    @FXML
    private TextField dateField;
    @FXML
    private TextField bloodPressureField;
    @FXML
    private TextField glucoseField;
    @FXML
    private TextField weightField;
    @FXML
    private Label dateLabel;
    @FXML
    private Label bloodPressureLabel;
    @FXML
    private Label glucoseLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label title;
    @FXML
    private TextArea observationArea;
    @FXML
    private Label observationsLabel;
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
}
