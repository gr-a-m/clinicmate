/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    @FXML
    private TableView<Row> table;
    @FXML
    private LineChart<?, ?> bloodChart;
    @FXML
    private LineChart<?, ?> weightChart;
    @FXML
    private ScatterChart<?, ?> glucoseChart;
    @FXML
    private TableColumn<?, ?> dateColumn;
    @FXML
    private TableColumn<?, ?> systolicColumn;
    @FXML
    private TableColumn<?, ?> diastolicColumn;
    @FXML
    private TableColumn<?, ?> glucoseColumn;
    @FXML
    private TableColumn<?, ?> weightColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.setEditable(false);
        
        ObservableList<Row> data=null;
        
        
        try {
            HealthRecord[] records=(Patient.getById(((Patient)PermissionsController.getInstance().getCurrentUser()).getPatientID())).getHealthRecords();
            //HealthRecord[] records=null;
            if(records!=null)
            {
                for(HealthRecord record: records)
                {
                    //data.add(new Row(record.getDate(),record.getDiaBloodPressure(),record.getSysBloodPressure(),
                    //record.getGlucose(),record.getWeight()));
                }
            }
            
            //table.setItems(data);
            //ArrayList<String> getComments();
        }catch(NonexistentRecordException ex){}
        
        
    }
    
    public static class Row
    {
        private final SimpleStringProperty date,systolic,diastolic,glucose,weight;
        private Row(Date date,int systolic,int diastolic,int glucose,int weight)
        {
            this.date = new SimpleStringProperty(date.toString());
            this.systolic = new SimpleStringProperty(Integer.toString(systolic));
            this.diastolic = new SimpleStringProperty(Integer.toString(diastolic));
            this.glucose = new SimpleStringProperty(Integer.toString(glucose));
            this.weight = new SimpleStringProperty(Integer.toString(weight));
        }
    }
    
    @FXML
    public void done(javafx.event.ActionEvent event) throws IOException
    {
        new ClinicMatePage("patientOptions.fxml",event,"Welcome");
    }
}