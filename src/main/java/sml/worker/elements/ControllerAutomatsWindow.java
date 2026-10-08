package sml.worker.elements;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

public class ControllerAutomatsWindow implements ControllerWindow {
    private int index;

    public ListView listAutomatsArea;
    public TextField fieldFilterByDescription;
    public TextArea fieldDescriptionArea;
    public TextField fieldDocument;
    public Label labelCount;

    private Stage stage;


    @FXML
    public void initialize() {
        index = -1;
        if (DBData.createTableAutomats()) {
            System.out.println("The table 'automats' is OK");
        } else {
            System.out.println("The table 'automats' is not OK");
        }
        if (DBData.createTableTypeElement()) {
            System.out.println("The table 'type_element' is OK");
        } else {
            System.out.println("The table 'type_element' is not OK");
        }
        setListItems();
        MultipleSelectionModel<String> selectionModel = listAutomatsArea.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            index = newValue.intValue();
            if (index != -1){
                fieldDescriptionArea.setText(DBData.getDescriptionAutomatById(Data.id.get(index)));
                fieldDocument.setText(DBData.getNameDocumentOfAutomatById(Data.id.get(index)));
                //System.out.println("Индекс: " + index + "    id: " + Data.id.get(index));
            }else{
                fieldDescriptionArea.clear();
                fieldDocument.clear();
            }
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    @Override
    public void update() {
        fieldDocument.setText(Data.getDocumentName());
        if (Data.isAdd){
            setListItems();
            Data.isAdd = false;
        }
        // TODO
    }

    @FXML
    public void apply(MouseEvent mouseEvent) {
        setListItems();
    }

    @FXML
    public void saveDescription(MouseEvent mouseEvent) {
        if (index == -1) return;
        if (DBData.updateDescriptionAutomatById(Data.id.get(index), fieldDescriptionArea.getText())){
            System.out.println("Description of automat is update");
        }else{
            System.out.println("Description of automat is not update");
        }
    }

    @FXML
    public void addAutomat(MouseEvent mouseEvent) {
        WindowsManager.getAddAutomatWindow();
    }

    @FXML
    public void deleteAutomat(MouseEvent mouseEvent) {
        if (index == -1) return;
        if (DBData.deleteDocumentFromAutomatsTableById(Data.id.get(index))){
            System.out.println("Automat is deleted");
        }else{
            System.out.println("Automat is not deleted");
        }
        setListItems();
    }

    @FXML
    public void viewDocument(MouseEvent mouseEvent) {
        WindowsManager.getPDFWindow(fieldDocument.getText());
    }

    @FXML
    public void setDocument(MouseEvent mouseEvent) {
        WindowsManager.getDocumentWindow();
    }

    @FXML
    public void thisAutomat(MouseEvent mouseEvent) {
        if (index == -1) return;
        Data.thisElementId = Data.id.get(index);
    }

    @FXML
    public void btnUpdate(MouseEvent mouseEvent) {
        if (index == -1) return;
        if (DBData.updateDocumentForAutomatById(Data.id.get(index), fieldDocument.getText())){
            System.out.println("documentName is update");
        }else{
            System.out.println("documentName is not update");
        }
    }

    @FXML
    public void btnClearDocument(MouseEvent mouseEvent) {
        fieldDocument.clear();
    }

    //==========================================================================

    private void setListItems() {
        ObservableList<String> row = DBData.getListItemsAutomats(String.valueOf(fieldFilterByDescription.getText()));
        if (row != null) {
            listAutomatsArea.setItems(row);
            labelCount.setText(String.valueOf(Data.count));
        }
    }
}
