package mkwuntr.c195;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreenController {

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button signOutButton;

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleAppointmentsClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentScreen.fxml"));
            Parent appointmentsRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage appointmentsStage = new Stage();
            appointmentsStage.setTitle("Appointments");
            appointmentsStage.setScene(new Scene(appointmentsRoot));
            appointmentsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCustomersClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customerScreen.fxml"));
            Parent customersRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage customersStage = new Stage();
            customersStage.setTitle("Customers");
            customersStage.setScene(new Scene(customersRoot));
            customersStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReportsClick() {
        try {
            // Load the FXML file for the Appointments page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reports.fxml"));
            Parent reportsRoot = loader.load();

            // Create a new stage for the Appointments page
            Stage reportsStage = new Stage();
            reportsStage.setTitle("Reports");
            reportsStage.setScene(new Scene(reportsRoot));
            reportsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignOutClick() {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }
}
