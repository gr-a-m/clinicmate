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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Mark Karlsrud
 */
public class LoginPage implements Initializable {
    
    ProgressBar bar;
    Stage stage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginTitle;
    @FXML
    private Label invalidLoginLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    //method run when login button is pressed
    //checks database to ensure that the user has an actual account
    @FXML
    private void login(javafx.event.ActionEvent event) throws IOException 
    {
        if(usernameField.getText().equals("1") && passwordField.getText().equals("2"))
        {
            // successfull login
            invalidLoginLabel.setVisible(false);
            changeScene("patientOptions.fxml",event,"Welcome");
        }
        else
        {
            //login failed
            invalidLoginLabel.setVisible(true);
            System.out.println("failed login");
        }
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
