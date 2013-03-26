/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class DoctorPageController implements Initializable {
    @FXML
    private TabPane dataMetricsTabPane;
    @FXML
    private Tab dataTab;
    @FXML
    private AnchorPane dataAnchorPane;
    @FXML
    private ScrollPane addCommentsScrollPane;
    @FXML
    private TextArea addCommentsArea;
    @FXML
    private Label addCommentsLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Tab MetricsTab;
    @FXML
    private AnchorPane metricsAnchorPane;
    @FXML
    private TabPane measurementsTabPane;
    @FXML
    private Tab bloodPressureTab;
    @FXML
    private AnchorPane bloodAnchorPane;
    @FXML
    private LineChart<?, ?> bloodChart;
    @FXML
    private ToggleButton bloodLinearRegToggle;
    @FXML
    private Tab weightTab;
    @FXML
    private AnchorPane weightAnchorPane;
    @FXML
    private LineChart<?, ?> weightChart;
    @FXML
    private ToggleButton weightLinearRegToggle;
    @FXML
    private Tab glucoseLevelsTab;
    @FXML
    private AnchorPane glucoseAchorPane;
    @FXML
    private LineChart<?, ?> glucoseChart;
    @FXML
    private ToggleButton glucoseLinearRegToggle;
    @FXML
    private ScrollPane patientsScrollPane;
    @FXML
    private AnchorPane scrollAnchorPane;
    @FXML
    private Label patientsLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void save(javafx.event.ActionEvent event) throws IOException
    {
        System.out.println(addCommentsArea.getText());
        addCommentsArea.getText();
    }
}
