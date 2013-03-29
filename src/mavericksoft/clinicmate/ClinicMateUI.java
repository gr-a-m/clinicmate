/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mavericksoft.clinicmate;

/**
 *
 * @author Mark Karlsrud
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClinicMateUI extends Application
{  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Application.launch(ClinicMateUI.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            AnchorPane page = (AnchorPane) FXMLLoader.load(ClinicMateUI.class.getResource("loginPage.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("MaverickSoft's ClinicMate");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Logger.getLogger(ClinicMateUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}