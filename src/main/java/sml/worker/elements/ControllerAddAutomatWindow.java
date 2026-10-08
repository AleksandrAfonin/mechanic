package sml.worker.elements;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

public class ControllerAddAutomatWindow implements ControllerWindow {


    public TextField manufacturerField;
    public TextField designationField;
    public TextField characteristicField;
    public TextField ratedCurrentField;
    public TextField ratedVoltageField;
    public TextField numberOfPolesField;
    public TextField documentField;
    public TextArea descriptionArea;
    public Button btnSetDocument;
    public Button btnAdd;

    @Override
    public void update() {
        documentField.setText(Data.getDocumentName());
    }

    public void setDocument(MouseEvent mouseEvent) {
        WindowsManager.getDocumentWindow();
    }

    public void add(MouseEvent mouseEvent) {
        String manufacturer = manufacturerField.getText();
        if (manufacturer == null || manufacturer.isBlank()) return;
        String designation = designationField.getText();
        if (designation == null || designation.isBlank()) return;
        String characteristic = characteristicField.getText();
        if (characteristic == null || characteristic.isBlank()) return;
        int ratedCurrent = number(ratedCurrentField.getText());
        if (ratedCurrent == -1) return;
        int ratedVoltage = number(ratedVoltageField.getText());
        if (ratedVoltage == -1) return;
        int numberOfPoles = number(numberOfPolesField.getText());
        if (numberOfPoles == -1) return;
        String document = documentField.getText();
        String description = descriptionArea.getText();

        if (DBData.addAutomat(manufacturer, designation, characteristic, ratedCurrent, ratedVoltage, numberOfPoles, description, document)){
            Data.isAdd = true;
            System.out.println("Automat is added");
        }else{
            System.out.println("Automat is not added");
        }
    }

    public void clearDocument(MouseEvent mouseEvent) {
        documentField.clear();
    }

    public int number(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
