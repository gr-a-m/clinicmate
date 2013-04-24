/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
    private TextField systolicField;
    @FXML
    private TextField glucoseField;
    @FXML
    private TextField weightField;
    @FXML
    private TextArea observationsArea;
    @FXML
    private Label dateLabel;
    @FXML
    private Label systolicLabel;
    @FXML
    private Label glucoseLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label observationsLabel;
    @FXML
    private Label diastolicLabel;
    @FXML
    private TextField diastolicField;
    @FXML
    private ComboBox<?> dayCombo;
    @FXML
    private ComboBox<?> yearCombo;
    @FXML
    private ComboBox<String> monthCombo;
    
    private Hashtable daysInMonth= new Hashtable();
    private ObservableList<String> months;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        months=FXCollections.observableArrayList(
                "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
        monthCombo.setItems(months);

        //add years to MenuButton
        //yearMenu.getItems().addAll(new MenuItem("1980"));
        for(int i=2013;i>=1900;i--)
        {
            yearCombo.getItems().addAll(i);
        }
        //add days to hashtable
        daysInMonth.put("Jan","31");
        daysInMonth.put("Feb","28"); //need to make this dependent on year
        daysInMonth.put("Mar","31");
        daysInMonth.put("Apr","30");
        daysInMonth.put("May","31");
        daysInMonth.put("Jun","30");
        daysInMonth.put("Jul","31");
        daysInMonth.put("Aug","31");
        daysInMonth.put("Sep","30");
        daysInMonth.put("Oct","31");
        daysInMonth.put("Nov","30");
        daysInMonth.put("Dec","31");
        
        //get days in month
        monthCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldString, String newString)
            {
                System.out.println("days:"+daysInMonth.get(newString));
                for(int i=1;i<=Integer.parseInt(daysInMonth.get(newString).toString());i++)
                {
                    dayCombo.getItems().addAll(i);
                }
          }
        });
    }
    
    @FXML
    public void save(javafx.event.ActionEvent event) throws IOException
    {
        boolean save=true;
        Date date=null;
        int diastolic=-1;
        int systolic=-1;
        int glucose=-1;
        int weight=-1;
        String observations="";
        UUID id=((Patient)PermissionsController.getInstance().getCurrentUser()).getPatientID();
        try
        {
            
            if(monthCombo.getSelectionModel().selectedItemProperty().getValue()==null ||
                    dayCombo.getSelectionModel().selectedItemProperty().getValue()==null ||
                    yearCombo.getSelectionModel().selectedItemProperty().getValue()==null)
            {
                System.out.println("NULL DATE");
                save=false;
            }
                int month=months.indexOf(monthCombo.getSelectionModel().selectedItemProperty().getValue())+1;
                int day=Integer.parseInt(dayCombo.getSelectionModel().selectedItemProperty().getValue().toString());
                int year=Integer.parseInt(yearCombo.getSelectionModel().selectedItemProperty().getValue().toString())-1900;
                date = new Date(year,month,day);
                System.out.println(month + "/" + day + "/" + year);
        }
        catch(Exception ex){}
        
        if(diastolicField.getText().isEmpty() || systolicField.getText().isEmpty() ||
                glucoseField.getText().isEmpty() || weightField.getText().isEmpty())
        {
            System.out.println("textfields are empty");
            save=false;
        }
        else
        {
            diastolic=Integer.parseInt(diastolicField.getText());
            systolic=Integer.parseInt(systolicField.getText());
            glucose=Integer.parseInt(glucoseField.getText());
            weight=Integer.parseInt(weightField.getText());
            observations=observationsArea.getText();
        }

        if(save)
        {
            try{
                HealthRecord record = HealthRecordController.getInstance().addRecord(id,date,diastolic,systolic,glucose,weight);
                HealthRecordController.getInstance().addComment(record.getRecordID(),observations);
                //HealthRecord[] records=HealthRecordController.getInstance().getRecordsForPatient(id);
                //UUID recordID = records[records.length-1].getRecordID();
                //HealthRecordController.getInstance().addComment(recordID,observations);
            }catch(Exception ex){System.out.println("NonExistantRecordException");}

            System.out.println("saved patient info");
            new ClinicMatePage("patientOptions.fxml",event,"Welcome");
        }
        else
        {
            errorLabel.setVisible(true);
        }
    }
}
