package sml.worker;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MainController {

    public TextField txtField;
    public Button btnOk;
    public TextArea txtArea;

    public void btnOk(MouseEvent mouseEvent) {
        String str = txtArea.getText();
        str = str + txtField.getText() + "\n";
        txtArea.clear();
        txtArea.setText(str);
        txtField.clear();
    }
}
