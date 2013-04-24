/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.math3.stat.regression.SimpleRegression;

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
    private LineChart<String, Integer> bloodChart;
    @FXML
    private LineChart<String, Integer> weightChart;
    @FXML
    private LineChart<String, Integer> glucoseChart;
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
    
    private Patient currentPatient;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.setEditable(false);
        
        dateColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("date"));
        systolicColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("systolic"));
        diastolicColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("diastolic"));
        glucoseColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("glucose"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<Row,String>("weight"));
        
        ObservableList<Row> data = FXCollections.observableArrayList();
        currentPatient = (Patient)PermissionsController.getInstance().getCurrentUser();
        
        try{
        HealthRecord[] records = currentPatient.getHealthRecords();

        if(records!=null)
        {
            for(HealthRecord record : records)
            {
                data.add(new Row(record.getDate(),
                        record.getDiaBloodPressure(),
                        record.getSysBloodPressure(),
                        record.getGlucose(),
                        record.getWeight()));
            }
            
            // Create lines for the charts
                    ObservableList<XYChart.Data<String, Integer>> sysData = FXCollections.observableArrayList();
                    ObservableList<XYChart.Data<String, Integer>> diaData = FXCollections.observableArrayList();
                    ObservableList<XYChart.Data<String, Integer>> glucData = FXCollections.observableArrayList();
                    ObservableList<XYChart.Data<String, Integer>> weightData = FXCollections.observableArrayList();

                    Date beginDate = null;
                    Date endDate = null;

                    for (HealthRecord record : records) {
                        if (beginDate == null) {
                            beginDate = record.getDate();
                            endDate = record.getDate();
                        } else {
                            if (record.getDate().getTime() < beginDate.getTime()) {
                                beginDate = record.getDate();
                            } else if (record.getDate().getTime() > endDate.getTime()) {
                                endDate = record.getDate();
                            }
                        }
                    }

                    for (HealthRecord record : records) {
                        if (record.getDate().getTime() <= endDate.getTime() &&
                                record.getDate().getTime() >= beginDate.getTime()) {
                            int dayOffset = (int) ((record.getDate().getTime() - beginDate.getTime()) / (1000*60*60*24));
                            sysData.add(new XYChart.Data<String, Integer>(record.getDate().toString(), record.getSysBloodPressure()));
                            diaData.add(new XYChart.Data<String, Integer>(record.getDate().toString(), record.getDiaBloodPressure()));
                            glucData.add(new XYChart.Data<String, Integer>(record.getDate().toString(), record.getGlucose()));
                            weightData.add(new XYChart.Data<String, Integer>(record.getDate().toString(), record.getWeight()));
                            System.out.println("sys: " + dayOffset + " " + record.getSysBloodPressure());
                            System.out.println("dia: " + dayOffset + " " + record.getDiaBloodPressure());
                            System.out.println("gluc: " + dayOffset + " " + record.getGlucose());
                            System.out.println("weight: " + dayOffset + " " + record.getWeight());
                        }
                    }

                    //System.out.println("1");

                    XYChart.Series<String, Integer> diaSeries = new XYChart.Series<String, Integer>(diaData);
                    XYChart.Series<String, Integer> sysSeries = new XYChart.Series<String, Integer>(sysData);
                    XYChart.Series<String, Integer> weightSeries = new XYChart.Series(weightData);
                    XYChart.Series<String, Integer> glucSeries = new XYChart.Series(glucData);

                    //System.out.println("2");

                    bloodChart.setData(FXCollections.observableArrayList(diaSeries, sysSeries));
                    weightChart.setData(FXCollections.observableArrayList(weightSeries));
                    glucoseChart.setData(FXCollections.observableArrayList(glucSeries));
                }

                if(data.isEmpty()){System.out.println("empty data");}
                table.setItems(data);
            }catch(Exception ex){ex.printStackTrace();}
        //}

        //ArrayList<String> getComments();
        //table.setItems(data);
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
    public void done(javafx.event.ActionEvent event) throws IOException
    {
        new ClinicMatePage("patientOptions.fxml",event,"Welcome");
    }
    
    @FXML
    public void genRegBlood() {
            HealthRecord[] records = currentPatient.getHealthRecords();

            if (bloodChart.getData().size() == 4) {
                bloodChart.getData().remove(2, 4);
                return;
            }

            Date beginDate = null;
            Date endDate = null;

            for (HealthRecord record : records) {
                if (beginDate == null) {
                    beginDate = record.getDate();
                    endDate = record.getDate();
                }

                if (record.getDate().getTime() < beginDate.getTime()) {
                    beginDate = record.getDate();
                } else if (record.getDate().getTime() > endDate.getTime()) {
                    endDate = record.getDate();
                }
            }

            try {
                SimpleRegression reg = HealthRecordController.getInstance().linearRegression(currentPatient.getPatientID(), beginDate, endDate, RegressionTypes.DIA);
                ObservableList<XYChart.Data<String, Integer>> diaData = FXCollections.observableArrayList();
                int dayOffset = (int) ((beginDate.getTime() - endDate.getTime()) / (1000*60*60*24));
                diaData.add(new XYChart.Data<String, Integer>(beginDate.toString(), (((int) reg.getIntercept()))));
                diaData.add(new XYChart.Data<String, Integer>(endDate.toString(), (((int) reg.getIntercept()) + ((int) (reg.getSlope() * dayOffset)))));
                XYChart.Series<String, Integer> diaSeries = new XYChart.Series<String, Integer>(diaData);
                bloodChart.getData().add(diaSeries);

                reg = HealthRecordController.getInstance().linearRegression(currentPatient.getPatientID(), beginDate, endDate, RegressionTypes.SYS);
                ObservableList<XYChart.Data<String, Integer>> sysData = FXCollections.observableArrayList();
                sysData.add(new XYChart.Data<String, Integer>(beginDate.toString(), (((int) reg.getIntercept()))));
                sysData.add(new XYChart.Data<String, Integer>(endDate.toString(), (((int) reg.getIntercept()) + ((int) (reg.getSlope() * dayOffset)))));
                XYChart.Series<String, Integer> sysSeries = new XYChart.Series<String, Integer>(sysData);
                bloodChart.getData().add(sysSeries);
            } catch (NonexistentRecordException nre) {
                nre.printStackTrace();
            }
        }

    @FXML
    public void genRegWeight() {
            HealthRecord[] records = currentPatient.getHealthRecords();

            if (weightChart.getData().size() == 2) {
                weightChart.getData().remove(1);
                return;
            }

            Date beginDate = null;
            Date endDate = null;

            for (HealthRecord record : records) {
                if (beginDate == null) {
                    beginDate = record.getDate();
                    endDate = record.getDate();
                }

                if (record.getDate().getTime() < beginDate.getTime()) {
                    beginDate = record.getDate();
                } else if (record.getDate().getTime() > endDate.getTime()) {
                    endDate = record.getDate();
                }
            }

            try {
                SimpleRegression reg = HealthRecordController.getInstance().linearRegression(currentPatient.getPatientID(), beginDate, endDate, RegressionTypes.WEIGHT);
                ObservableList<XYChart.Data<String, Integer>> weightData = FXCollections.observableArrayList();
                int dayOffset = (int) ((beginDate.getTime() - endDate.getTime()) / (1000*60*60*24));
                weightData.add(new XYChart.Data<String, Integer>(beginDate.toString(), (((int) reg.getIntercept()))));
                weightData.add(new XYChart.Data<String, Integer>(endDate.toString(), (((int) reg.getIntercept()) + ((int) (reg.getSlope() * dayOffset)))));
                XYChart.Series<String, Integer> weightSeries = new XYChart.Series<String, Integer>(weightData);
                weightChart.getData().add(weightSeries);
            } catch (NonexistentRecordException nre) {
                nre.printStackTrace();
            }
        }
    

    @FXML
    public void genRegGluc() {
            HealthRecord[] records = currentPatient.getHealthRecords();

            if (glucoseChart.getData().size() == 2) {
                glucoseChart.getData().remove(1);
                return;
            }

            Date beginDate = null;
            Date endDate = null;

            for (HealthRecord record : records) {
                if (beginDate == null) {
                    beginDate = record.getDate();
                    endDate = record.getDate();
                }

                if (record.getDate().getTime() < beginDate.getTime()) {
                    beginDate = record.getDate();
                } else if (record.getDate().getTime() > endDate.getTime()) {
                    endDate = record.getDate();
                }
            }

            try {
                SimpleRegression reg = HealthRecordController.getInstance().linearRegression(currentPatient.getPatientID(), beginDate, endDate, RegressionTypes.GLUCOSE);
                ObservableList<XYChart.Data<String, Integer>> glucData = FXCollections.observableArrayList();
                int dayOffset = (int) ((beginDate.getTime() - endDate.getTime()) / (1000*60*60*24));
                glucData.add(new XYChart.Data<String, Integer>(beginDate.toString(), (((int) reg.getIntercept()))));
                glucData.add(new XYChart.Data<String, Integer>(endDate.toString(), (((int) reg.getIntercept()) + ((int) (reg.getSlope() * dayOffset)))));
                XYChart.Series<String, Integer> glucSeries = new XYChart.Series<String, Integer>(glucData);
                glucoseChart.getData().add(glucSeries);
            } catch (NonexistentRecordException nre) {
                nre.printStackTrace();
            }
        }
}