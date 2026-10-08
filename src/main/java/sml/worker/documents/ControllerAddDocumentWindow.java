package sml.worker.documents;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

import java.io.File;

public class ControllerAddDocumentWindow implements ControllerWindow {

    @FXML
    public ChoiceBox choiceBox;

    @FXML
    public Label labelAddFileInformation;

    @FXML
    public Label labelChosenFile;

    @FXML
    public TextArea descriptionArea;

    private File file;

    @FXML
    public void initialize(){
        setListChooser();
    }

    public void addNewTypeOfDocument(MouseEvent mouseEvent) {
        WindowsManager.getAddTypeDocumentWindow();
    }

    public void choiceFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF documents", "*.pdf");
        fileChooser.getExtensionFilters().add(extensionFilter);
        file = fileChooser.showOpenDialog(new Stage());
        labelChosenFile.setText(file.getName());
    }

    public void addFile(MouseEvent mouseEvent) {
        String documentName = labelChosenFile.getText();
        if (DBData.insertFileIntoDocument(
                documentName,
                String.valueOf(choiceBox.getValue()),
                descriptionArea.getText(),
                file)){
            labelAddFileInformation.setText("The file '" + documentName + "' added successfully");
            Data.isAdd = true;
        }else{
            labelAddFileInformation.setText("The file '" + documentName + "' not added");
        }
    }

    @Override
    public void update() {
        if (Data.isAdd){
            setListChooser();
            Data.isAdd = false;
        }
    }

    //===============================================

    private void setListChooser() {
        ObservableList<String> row = DBData.getListChooserTypesOfDocuments(false);
        if (row == null) {
            return;
        }
        choiceBox.setItems(row);
    }
}
