package mkwuntr.c195;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModifyCustomerController {

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    @FXML
    private void handleSaveClick(){

    }

    @FXML
    private void handleCancelClick(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
