package sml.worker.employees;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

public class ControllerEmployeesWindow implements ControllerWindow {
    private int index;

    public ListView listEmployees;
    public TextArea areaDescription;
    public TextField fieldDocument;
    public TextField fieldFilterByDescription;
    public Label labelCount;

    public void initialize(){
        index = -1;
        DBData.createTableStaff();
        setListItems();
        MultipleSelectionModel<String> selectionModel = listEmployees.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            index = newValue.intValue();
            if (index != -1){
                areaDescription.setText(DBData.getDescriptionEmployeeById(Data.id.get(index)));
                fieldDocument.setText(DBData.getNameDocumentOfEmployeeById(Data.id.get(index)));
            }else{
                areaDescription.clear();
                fieldDocument.clear();
            }
        });
    }

    @Override
    public void update() {
        fieldDocument.setText(Data.getDocumentName());
        if (Data.isAdd){
            setListItems();
            Data.isAdd = false;
        }
    }

    public void setDocument(MouseEvent mouseEvent) {
        WindowsManager.getDocumentWindow();
    }

    public void clearDocument(MouseEvent mouseEvent) {
        fieldDocument.clear();
    }

    public void btnUpdate(MouseEvent mouseEvent) {
        if (index == -1) return;
        if (DBData.updateDocumentForStaffById(Data.id.get(index), fieldDocument.getText())){
            System.out.println("documentName is update");
        }else{
            System.out.println("documentName is not update");
        }
        if (DBData.updateDescriptionStaffById(Data.id.get(index), areaDescription.getText())){
            System.out.println("description is update");
        }else{
            System.out.println("description is not update");
        }
    }

    public void addEmployee(MouseEvent mouseEvent) {
        WindowsManager.getAddEmployeesWindow();
    }

    public void deleteEmployee(MouseEvent mouseEvent) {
        if (index == -1) return;
        if (DBData.deleteDocumentFromStaffTableById(Data.id.get(index))){
            System.out.println("Employee is deleted");
        }else{
            System.out.println("Employee is not deleted");
        }
        setListItems();
    }

    public void thisEmployee(MouseEvent mouseEvent) {
        if (index == -1) return;
        Data.thisElementId = Data.id.get(index);
    }

    public void apply(MouseEvent mouseEvent) {
        setListItems();
    }

    public void view(MouseEvent mouseEvent) {
        String documentName = fieldDocument.getText();
        if (documentName == null || documentName.isBlank()) return;
        WindowsManager.getPDFWindow(documentName);
    }

    //===================================================================

    private void setListItems() {
        ObservableList<String> row = DBData.getListItemsEmployees(String.valueOf(fieldFilterByDescription.getText()));
        if (row != null) {
            listEmployees.setItems(row);
            labelCount.setText(String.valueOf(Data.count));
        }
    }


    public void btnData(MouseEvent mouseEvent) {

    }

    public void btnObuchenie(MouseEvent mouseEvent) {

    }

    public void btnMedKomissii(MouseEvent mouseEvent) {

    }
}
