package sml.worker.employees;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sml.worker.control.ControllerWindow;
import sml.worker.control.WindowsManager;
import sml.worker.data.DBData;
import sml.worker.data.Data;

import java.time.LocalDate;

public class ControllerAddEmployeeWindow implements ControllerWindow {
    public TextField fieldDocument;
    public TextField fieldSurname;
    public TextField fieldName;
    public TextField fieldPatronymic;
    public DatePicker dateDate;
    public TextArea areaDescription;

    @Override
    public void update() {
        fieldDocument.setText(Data.getDocumentName());
    }

    public void btnSetDocument(MouseEvent mouseEvent) {
        WindowsManager.getDocumentWindow();
    }

    public void btnClearDocument(MouseEvent mouseEvent) {
        fieldDocument.clear();
    }

    public void btnAdd(MouseEvent mouseEvent) {
        String sureName = fieldSurname.getText();
        if (sureName == null || sureName.isBlank()) return;
        String name = fieldName.getText();
        if (name == null || name.isBlank()) return;
        String patronymic = fieldPatronymic.getText();
        if (patronymic == null || patronymic.isBlank()) return;
        LocalDate dt = dateDate.getValue();
        if (dt == null) return;
        String date = dt.getDayOfMonth() + "." + dt.getMonth().getValue() + "." + dt.getYear();
        String description = areaDescription.getText();
        String document = fieldDocument.getText();
        if (DBData.insertEmployeeIntoStaff(sureName, name, patronymic, date, description, document)){
            System.out.println("Employee is added");
            Data.isAdd = true;
        }else{
            System.out.println("Employee is not added");
        }



        //Date date = Date.from(dateDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //System.out.println("day: " + dt.getDayOfMonth() + "  month: " + dt.getMonth().getValue() + "  year: " + dt.getYear());
    }
}
