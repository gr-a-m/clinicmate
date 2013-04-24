/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * FXML Controller class
 *
 * @author Mark Karlsrud
 */
public class DoctorPage implements Initializable {
    @FXML
    private TabPane dataMetricsTabPane;
    @FXML
    private Tab dataTab;
    @FXML
    private AnchorPane dataAnchorPane;
    @FXML
    private TextArea recordComments;
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
    private Label patientsLabel;
    @FXML
    private Button logOutButton;
    @FXML
    private ListView<?> patientList;
    @FXML
    private TableView<Row> table;
    @FXML
    private ToggleButton bloodNonLinToggle;
    @FXML
    private ToggleButton weightNonLinToggle;
    @FXML
    private ToggleButton glucoseNonLinToggle;
    @FXML
    private TableColumn<Row,String> dateColumn;
    @FXML
    private TableColumn<Row,String> systolicColumn;
    @FXML
    private TableColumn<Row,String> diastolicColumn;
    @FXML
    private TableColumn<Row,String> glucoseColumn;
    @FXML
    private TableColumn<Row,String> weightColumn;
    
    private Patient[] patients;
    private HealthRecord currentRecord;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        patients= ((HealthProfessional)PermissionsController.getInstance().getCurrentUser()).getPatients();
        ArrayList<String> patientArray=new ArrayList<String>();
        //System.out.println("numOfPatients:"+patients.length);
        
        for(int i=0;i<patients.length;i++)
        {
            patientArray.add(patients[i].getLastName() + ", " + patients[i].getFirstName());
        }
        
        ObservableList items=FXCollections.observableArrayList(patientArray);
        patientList.setItems(items);
        
        table.setEditable(false);
        
        dateColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("date"));
        systolicColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("systolic"));
        diastolicColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("diastolic"));
        glucoseColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("glucose"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("weight"));
        //table.getColumns().setAll(dateColumn,systolicColumn,diastolicColumn,glucoseColumn,weightColumn);
        table.getSelectionModel().getSelectedIndices().addListener(new HealthRecordRowListener());
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
            System.out.println(date.toString() + systolic + diastolic + glucose + weight);
        }
        public String getDate()
        {
            return date.get();
        }
        public String getSystolic()
        {
            return systolic.get();
        }
        public String getDiastolic()
        {
            return diastolic.get();
        }
        public String getGlucose()
        {
            return glucose.get();
        }
        public String getWeight()
        {
            return weight.get();
        }
    }
    
    @FXML
    private void save(javafx.event.ActionEvent event) throws IOException
    {
        Date date=null;
        UUID id=null;

        
        if(patientList.getSelectionModel().getSelectedItem()==null)
        {
            System.out.println("patient not selected");
        }
        else
        {
            int index=patientList.getSelectionModel().getSelectedIndex();
            Patient selectedPatient=patients[index];
            id=selectedPatient.getPatientID();
            String observations=addCommentsArea.getText();
            int hrIndex = table.getSelectionModel().getSelectedIndex();

            if (hrIndex != -1) {
                HealthRecordController.getInstance().addComment(selectedPatient.getHealthRecords()[hrIndex].getRecordID(), observations);
                table.getSelectionModel().select(hrIndex);
            }

            System.out.println("doctor saved patient info");
        }
    }
    
    @FXML
    public void logOut(javafx.event.ActionEvent event) throws IOException
    {
        PermissionsController.getInstance().logout();
        new ClinicMatePage("loginPage.fxml",event,"Login");
    }
    
    @FXML
    public void updateTable(MouseEvent event)
    {
        int index=patientList.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            Patient currentPatient=patients[index];
        
            ObservableList<DoctorPage.Row> data=FXCollections.observableArrayList();

            try {
                HealthRecord[] records=currentPatient.getHealthRecords();

                if(records!=null)
                {
                    for(HealthRecord record : records)
                    {
                        currentRecord=record;
                        data.add(new Row(record.getDate(),record.getDiaBloodPressure(),record.getSysBloodPressure(),record.getGlucose(),record.getWeight()));
                    }
                }

                if(data.isEmpty()){System.out.println("empty data");}
                table.setItems(data);
                //ArrayList<String> getComments();
            }catch(Exception ex){System.out.println("exception");}
        }
    }

    private class HealthRecordRowListener implements ListChangeListener<Integer> {
        public void onChanged(Change<? extends Integer> change) {
            int index=patientList.getSelectionModel().getSelectedIndex();
            Patient currentPatient = patients[index];

            int hrIndex = table.getSelectionModel().getSelectedIndex();
            if (hrIndex != -1) {
                HealthRecord hr = currentPatient.getHealthRecords()[hrIndex];
                String comments = "";

                // I put this hashmap hack in there because some comments are
                // stored twice -- we should fix that. TODO
                HashMap<String, Boolean> commentMap = new HashMap<String, Boolean>();

                if (hr.getComments().size() > 0) {
                    for (String comment : hr.getComments()) {
                        commentMap.put(comment, true);
                    }
                }

                for (String comment : commentMap.keySet()) {
                    comments += (comment + "\n");
                }

                recordComments.setText(comments);
            }
        }
    }
}
