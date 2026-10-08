package sml.worker.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sml.worker.data.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindowsManager {

    private static final List<Stage> stageList = new ArrayList<>();
    private static final List<ControllerWindow> controllerList = new ArrayList<>();

    public static Stage getLastStage(){
        return stageList.getLast();
    }

    public static void getAddDocumentWindow(){
        showWindow("/documents/addDocumentWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Add a document",
                600, 400, true);
    }

    public static void getAddTypeDocumentWindow(){
        showWindow("/documents/addTypeDocumentWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Add a new type of document",
                600, 150, true);
    }

    public static void getDocumentWindow(){
        showWindow("/documents/documentWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Working with PDF documents",
                950, 500, true);

    }

    public static void getPDFWindow(String fileName){
        if (fileName == null || fileName.isBlank()) return;
        Data.setDocumentName(fileName);
        showWindow("/documents/pdfWindow.fxml",
                "/documents/TypeOfDocument.png",
                fileName,
                850, 600, true);
    }

    public static void getAddTypeElementWindow(){
        showWindow("/elements/addTypeElementWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Add a new type of element",
                600, 150, true);
    }

    public static void getAutomatsWindow(){
        showWindow("/elements/automatsWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Automats",
                650, 400, true);
    }

    public static void getAddAutomatWindow(){
        showWindow("/elements/addAutomatWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Add automat",
                600, 300, true);
    }

    public static void getEmployeesWindow(){
        showWindow("/employees/employeesWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Employees",
                720, 420, true);
    }

    public static void getAddEmployeesWindow(){
        showWindow("/employees/addEmployeeWindow.fxml",
                "/documents/TypeOfDocument.png",
                "Add Employee",
                600, 300, true);
    }

    public static void getImageWindow(){
        showWindow("/documents/image.fxml",
                "/documents/TypeOfDocument.png",
                "titi",
                500, 400, true);
    }

    private static void showWindow(String resourceWindow, String resourceIcon, String title, double width, double height, boolean resizable){
        FXMLLoader loader;
        Parent root;
        Image image;
        try {
            loader = new FXMLLoader(WindowsManager.class.getResource(resourceWindow));
            root = loader.load();
            image = new Image(WindowsManager.class.getResourceAsStream(resourceIcon));
            controllerList.add(loader.getController());
        } catch (IOException e) {
            //TODO
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.getIcons().add(image);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setOnCloseRequest(e -> {
            closeCurrentWindow();
        });
        if (!stageList.isEmpty()){
            stageList.getLast().hide();
        }
        stageList.add(stage);
        stage.show();
    }

    public static void closeCurrentWindow(){
        stageList.getLast().close();
        stageList.removeLast();
        controllerList.removeLast();
        if (!stageList.isEmpty()){
            controllerList.getLast().update();
            stageList.getLast().show();
        }
    }
}
