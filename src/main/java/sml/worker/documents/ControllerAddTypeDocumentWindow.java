package sml.worker.documents;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.data.DBData;
import sml.worker.data.Data;

public class ControllerAddTypeDocumentWindow implements ControllerWindow {

    @FXML
    public TextField textField;

    @FXML
    public Label label;

    @FXML
    public void initialize(){

    }

    public void addTypeDocument(MouseEvent mouseEvent) {
        String newType = textField.getText();
        if (DBData.insertTypeIntoTypeDocument(newType)){
            label.setText("The type '" + newType + "' added successful");
            textField.setText("");
            Data.isAdd = true;
        }else{
            label.setText("The new type is not added");
        }
    }

    @Override
    public void update() {
        initialize();
    }
}
