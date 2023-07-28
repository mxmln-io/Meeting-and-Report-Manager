package mkwuntr.c195;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentScreenController {

    @FXML
    private Button addButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;


    @FXML
    private void handleAddClick(){
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addAppointment.fxml"));
            Parent addAppointmentRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage addAppointmentStage = new Stage();
            addAppointmentStage.setTitle("Appointments");
            addAppointmentStage.setScene(new Scene(addAppointmentRoot));
            addAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifyClick(){
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyAppointment.fxml"));
            Parent modifyAppointmentRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage modifyAppointmentStage = new Stage();
            modifyAppointmentStage.setTitle("Appointments");
            modifyAppointmentStage.setScene(new Scene(modifyAppointmentRoot));
            modifyAppointmentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteClick(){

    }

    @FXML
    private void handleExitClick(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
