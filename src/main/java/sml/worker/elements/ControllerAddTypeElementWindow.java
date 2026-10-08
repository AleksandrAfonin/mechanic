package sml.worker.elements;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.data.DBData;

public class ControllerAddTypeElementWindow implements ControllerWindow {

    public Label label;

    public TextField textField;

    public void initialize(){

    }

    public void addTypeElement(MouseEvent mouseEvent) {
        String newType = textField.getText();
        if (DBData.insertTypeIntoTypeElement(newType)){
            label.setText("The type '" + newType + "' added successful");
            textField.setText("");
        }else{
            label.setText("The new type is not added");
        }
    }

    @Override
    public void update() {
        initialize();
    }
}
