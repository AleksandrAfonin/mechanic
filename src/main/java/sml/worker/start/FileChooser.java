package sml.worker.start;

import javafx.application.Application;
import javafx.stage.Stage;
import sml.worker.control.WindowsManager;

public class FileChooser extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //WindowsManager.getAddTypeElementWindow();
        WindowsManager.getDocumentWindow();
//        WindowsManager.getImageWindow();
        //WindowsManager.getAutomatsWindow();
        //WindowsManager.getAddAutomatWindow();
        //WindowsManager.getEmployeesWindow();
        //DBData.createTableStaff();
        //WindowsManager.getAddEmployeesWindow();


    }
}
