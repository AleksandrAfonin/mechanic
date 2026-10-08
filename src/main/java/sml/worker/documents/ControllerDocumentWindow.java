package sml.worker.documents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

public class ControllerDocumentWindow implements ControllerWindow {

    @FXML
    public Label labelCount;

    @FXML
    public ListView list;

    @FXML
    public Label label;

    @FXML
    public TextArea descriptionArea;

    @FXML
    public ChoiceBox choiceBox;

    @FXML
    public TextField filterByDescriptionField;

    @FXML
    public Button btnThisDocument;

    @FXML
    public void initialize() {
        if (DBData.createTableDocument()){
            System.out.println("The table 'Document' is OK");
        }else{
            System.out.println("The table 'Document' is not OK");
        }
        if (DBData.createTableTypeDocument()){
            System.out.println("The table 'TypeDocument' is OK");
        }else{
            System.out.println("The table 'TypeDocument' is not OK");
        }
        setListChooser();
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (choiceBox.getValue() == null) return;
            setListItems();
        });
        setListItems();
        MultipleSelectionModel<String> selectionModel = list.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                descriptionArea.setText(DBData.getDescriptionDocumentByName(t1));
                label.setText(t1);
            }
        });
    }

    @FXML
    public void addFile(MouseEvent me) {
        WindowsManager.getAddDocumentWindow();
    }

    @FXML
    public void delete(MouseEvent mouseEvent) {
        String documentName = label.getText();
        if (DBData.deleteDocumentFromDocumentTableByName(documentName)){
            System.out.println("Delete document is OK");
        }else{
            System.out.println("Delete document is not OK");
        }
        setListItems();
    }

    @FXML
    public void addTypeOfDocument(MouseEvent mouseEvent) {
        WindowsManager.getAddTypeDocumentWindow();
    }

    @FXML
    public void apply(MouseEvent mouseEvent) {
        setListItems();
    }

    @FXML
    public void saveDescription(MouseEvent mouseEvent) {
        if (DBData.updateDescriptionDocumentByName(label.getText(), descriptionArea.getText())){
            System.out.println("Description of document is update");
        }else{
            System.out.println("Description of document is not update");
        }
    }

    @FXML
    public void viewDocument(MouseEvent mouseEvent) {
        WindowsManager.getPDFWindow(label.getText());
    }

    @FXML
    public void saveDocumentAs(MouseEvent mouseEvent) {
        if (DBData.saveDocumentByNameAs(label.getText())){
            System.out.println("The file is saved");
        }else{
            System.out.println("The file is not saved");
        }
    }

    @Override
    public void update() {
        if (Data.isAdd){
            setListChooser();
            Data.isAdd = false;
        }
    }

    @FXML
    public void selectThis(MouseEvent mouseEvent) {
        String string = label.getText();
        Data.setDocumentName(string);
        btnThisDocument.setText("Selected");
        System.out.println(string);
        WindowsManager.closeCurrentWindow();
    }

    //=================================================================
    private void setListItems() {
        ObservableList<String> row = DBData.getListItemsDocument(String.valueOf(choiceBox.getValue()), filterByDescriptionField.getText());
        if (row != null){
            list.setItems(row);
            labelCount.setText(String.valueOf(Data.count));
        }
    }

    private void setListChooser() {
        ObservableList<String> row = DBData.getListChooserTypesOfDocuments(true);
        if (row == null) {
            return;
        }
        choiceBox.setItems(row);
        choiceBox.setValue(row.getFirst());
    }

}
