package mkwuntr.c195;

import dataaccessobjects.AppointmentDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> idColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactIdColumn;

    @FXML
    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ObservableList<Appointment> appointmentList = appointmentDAO.getAllAppointmentsObservable();
        appointmentTable.setItems(appointmentList);
    }

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
