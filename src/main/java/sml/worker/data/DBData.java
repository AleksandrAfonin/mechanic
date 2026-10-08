package sml.worker.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.sql.*;

public class DBData {
    // подключение к базе данных
    public static String url = "jdbc:sqlite:databases/database.sdb";

//    public static String url = "jdbc:postgresql://localhost:5432/postgres";
//    public static String user = "postgres";
//    public static String password = "2854";

    // Таблицы
    private static final String TableDocument = "documents";
    private static final String TableTypeDocument = "type_document";
    private static final String TableTypeElement = "type_element";
    private static final String TableAutomats = "automats";
    private static final String TableStaff = "staff";

    public static String CreateTableTypeElement = "CREATE TABLE IF NOT EXISTS " + TableTypeElement + " (" +
            " id INTEGER NOT NULL," +
            " type TEXT NOT NULL UNIQUE," +
            " PRIMARY KEY (id));";
    public static String CreateTableAutomats = "CREATE TABLE IF NOT EXISTS " + TableAutomats + " (" +
            " id INTEGER PRIMARY KEY NOT NULL," +
            " manufacturer TEXT NOT NULL," +
            " designation TEXT NOT NULL," +
            " characteristic TEXT NOT NULL," +
            " rated_current INTEGER NOT NULL," +
            " rated_voltage INTEGER NOT NULL," +
            " number_of_poles INTEGER NOT NULL," +
            " description TEXT," +
            " document TEXT," +
            " UNIQUE (manufacturer, designation, characteristic, rated_current));";
    public static String CreateTableDocument = "CREATE TABLE IF NOT EXISTS " + TableDocument + " (" +
            " id INTEGER NOT NULL," +
            " name TEXT NOT NULL UNIQUE," +
            " type TEXT NOT NULL," +
            " description TEXT NOT NULL," +
            " data BLOB NOT NULL," +
            " bookmark TEXT NOT NULL DEFAULT '1'," +
            " PRIMARY KEY (id));";
    public static String CreateTableTypeDocument = "CREATE TABLE IF NOT EXISTS " + TableTypeDocument + " (" +
            " id INTEGER NOT NULL," +
            " type TEXT NOT NULL UNIQUE," +
            " PRIMARY KEY (id));";
    public static String CreateTableStaff = "CREATE TABLE IF NOT EXISTS " + TableStaff + " (" +
            " id INTEGER PRIMARY KEY NOT NULL," +
            " surname TEXT NOT NULL," +//
            " name TEXT NOT NULL," +//
            " patronymic TEXT NOT NULL," +//
            " date_of_birth TEXT NOT NULL," +//
            " description TEXT," +//
            " document TEXT," +//
            " UNIQUE (surname, name, patronymic, date_of_birth));";

    public static String StSelectAllFromDocumentByName = "SELECT * FROM " + TableDocument + " WHERE name = ?;";
    public static String StSelectAllFromTypeDocument = "SELECT * FROM " + TableTypeDocument + ";";
    public static String StSelectNameFromDocument = "SELECT name FROM " + TableDocument;
    public static String StSelectFromStaff = "SELECT id, surname, name, patronymic, date_of_birth FROM " + TableStaff;
    public static String StGetFileFromDocumentByName = "SELECT * FROM " + TableDocument + " WHERE name = ?;";

    // Данные по окну addTypeDocumentWindow
    public static String StInsertIntoTypeDocument = "INSERT INTO " + TableTypeDocument + " (type) VALUES (?);";

    // Данные по окну addDocumentWindow
    public static String StInsertIntoDocument = "INSERT INTO " + TableDocument + " (name, type, description, data) VALUES (?, ?, ?, ?);";

    public static String StInsertIntoTypeElement = "INSERT INTO " + TableTypeElement + " (type) VALUES (?);";

    public static String StInsertIntoStaff = "INSERT INTO " + TableStaff + " (surname, name, patronymic, date_of_birth, description, document) VALUES (?, ?, ?, ?, ?, ?);";

    public static String StSelectFromAutomats = "SELECT id, manufacturer, designation, characteristic, rated_current, rated_voltage, number_of_poles FROM " + TableAutomats;

    // --- documents ---
    // ControllerDocumentWindow
    public static ObservableList<String> getListChooserTypesOfDocuments(boolean isAll) {
        ResultSet result = null;
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            String request = StSelectAllFromTypeDocument;
            result = statement.executeQuery(request);
            ObservableList<String> row = FXCollections.observableArrayList();
            if (isAll) row.add("All");
            while (result.next()) {
                row.add(result.getString("type"));
            }
            return row;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException ignored) {
            }
        }
    }

    //*
    public static boolean deleteDocumentFromDocumentTableByName(String documentName) {
        if (documentName == null || documentName.isBlank()) return false;
        return deleteByName(documentName, TableDocument, "name");
    }

    //*
    public static boolean updateDescriptionDocumentByName(String documentName, String description) {
        if (documentName == null || documentName.isBlank()) return false;
        return updateByName(documentName, TableDocument, "name", "description", description);
    }

    public static boolean saveDocumentByNameAs(String documentName) {
        if (documentName == null || documentName.isBlank()) return false;
        File file;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");
        file = directoryChooser.showDialog(new Stage());
        if (file == null) return false;
        System.out.println("save as " + file.getName());
        file = new File(file, documentName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StGetFileFromDocumentByName)) {
            statement.setString(1, documentName);
            ResultSet result = statement.executeQuery();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(result.getBytes("data"));
            } catch (NullPointerException e) {
                System.out.println("The record by name = " + documentName + " is not exists");
                return false;
            } catch (IOException e) {
                //TODO
                throw new RuntimeException(e);
            }
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> getListItemsDocument(String choiceTypeOfDocument, String filterDescription) {
        ResultSet result = null;
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            result = statement.executeQuery(requestDocument(choiceTypeOfDocument, filterDescription));
            ObservableList<String> row = FXCollections.observableArrayList();
            int count = 0;
            while (result.next()) {
                row.add(result.getString("name"));
                count++;
            }
            Data.count = count;
            return row;
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException ignored) {
            }
        }
    }

    //*
    public static boolean createTableDocument() {
        return createTable(CreateTableDocument);
    }

    //*
    public static boolean createTableTypeDocument() {
        return createTable(CreateTableTypeDocument);
    }

    //*
    public static String getDescriptionDocumentByName(String documentName) {
        return getStringByName(TableDocument, documentName, "name", "description");
    }

    // ControllerAddDocumentWindow
    public static boolean insertFileIntoDocument(String documentName, String typeDocument, String descriptionDocument, File file) {
        if (file == null || documentName.isBlank() || typeDocument.isBlank()) return false;
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return false;
            //TODO
        }
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StInsertIntoDocument)
        ) {
            statement.setString(1, documentName);
            statement.setString(2, typeDocument);
            statement.setString(3, descriptionDocument);
            statement.setBytes(4, fis.readAllBytes());
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            return false;
            //TODO
        } finally {
            try {
                fis.close();
            } catch (IOException ignored) {
            }
        }
    }

    // ControllerAddTypeDocument
    public static boolean insertTypeIntoTypeDocument(String type) {
        if (type == null || type.isBlank()) return false;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StInsertIntoTypeDocument)
        ) {
            statement.setString(1, type);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean insertEmployeeIntoStaff(String sureName, String name, String patronymic, String dateOfBirth, String description, String document) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StInsertIntoStaff)
        ) {
            statement.setString(1, sureName);
            statement.setString(2, name);
            statement.setString(3, patronymic);
            statement.setString(4, dateWithZero(dateOfBirth));
            statement.setString(5, description);
            statement.setString(6, document);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //* ControllerPDFWindow
    public static PDDocument getDocumentByName(String documentName) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StSelectAllFromDocumentByName)) {
            statement.setString(1, documentName);
            ResultSet resultSet = statement.executeQuery();
            return Loader.loadPDF(resultSet.getBytes("data"));
        } catch (SQLException | IOException | NullPointerException e) {
            return null;
            //TODO
        }
    }

    public static String getBookmarkDocument(String documentName){
        String request = "SELECT bookmark FROM " + TableDocument + " WHERE name = ?;";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request);) {
            statement.setString(1, documentName);
            ResultSet result = statement.executeQuery();
            return result.getString("bookmark");
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateBookmarkDocument(String documentName, String bookmark){
        if (documentName == null || documentName.isBlank()) return false;
        return updateByName(documentName, TableDocument, "name", "bookmark", bookmark);
    }

    //*
    public static boolean createTableAutomats() {
        return createTable(CreateTableAutomats);
    }

    //*
    public static boolean createTableTypeElement() {
        return createTable(CreateTableTypeElement);
    }

    public static boolean insertTypeIntoTypeElement(String type) {
        if (type == null || type.isBlank()) return false;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(StInsertIntoTypeElement)
        ) {
            statement.setString(1, type);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //*
    public static ObservableList<String> getListItemsAutomats(String filterByDescription) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(requestAutomats(filterByDescription))) {
            ObservableList<String> row = FXCollections.observableArrayList();
            Data.count = 0;
            Data.id.clear();
            while (result.next()) {
                String string = result.getString("manufacturer") + "  ";
                string += result.getString("designation") + "  ";
                string += result.getString("characteristic");
                string += result.getInt("rated_current") + "  ";
                string += result.getInt("rated_voltage") + "volt  ";
                string += result.getInt("number_of_poles") + "poles";
                row.add(string);
                Data.count++;
                Data.id.add(result.getInt("id"));
            }
            return row;
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
            return null;
        }
    }

    //*
    public static String getDescriptionAutomatById(int id){
        return getStringById(TableAutomats, id, "id", "description");
    }

    //*
    public static String getNameDocumentOfAutomatById(int id){
        return getStringById(TableAutomats, id, "id", "document");
    }

    //*
    public static boolean updateDescriptionAutomatById(int id, String description){
        return updateById(id, TableAutomats, "id", "description", description);
    }

    public static boolean updateDescriptionStaffById(int id, String description){
        return updateById(id, TableStaff, "id", "description", description);
    }

    public static boolean updateDocumentForAutomatById(int id, String documentName){
        return updateById(id, TableAutomats, "id", "document", documentName);
    }

    public static boolean updateDocumentForStaffById(int id, String documentName){
        return updateById(id, TableStaff, "id", "document", documentName);
    }

    public static boolean deleteDocumentFromAutomatsTableById(int id) {
        return deleteById(id, TableAutomats, "id");
    }

    public static boolean deleteDocumentFromStaffTableById(int id) {
        return deleteById(id, TableStaff, "id");
    }

    public static boolean addAutomat(String manufacturer, String designation, String characteristic, int ratedCurrent, int ratedVoltage, int numberOfPoles, String description, String document) {
        String request = "INSERT INTO " + TableAutomats + " (manufacturer, designation, characteristic, rated_current, rated_voltage, number_of_poles, description, document) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)
        ) {
            statement.setString(1, manufacturer);
            statement.setString(2, designation);
            statement.setString(3, characteristic);
            statement.setInt(4, ratedCurrent);
            statement.setInt(5, ratedVoltage);
            statement.setInt(6, numberOfPoles);
            statement.setString(7, description);
            statement.setString(8, document);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean createTableStaff(){
        return createTable(CreateTableStaff);
    }

    public static ObservableList<String> getListItemsEmployees(String filterByDescription) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(requestEmployees(filterByDescription))) {
            ObservableList<String> row = FXCollections.observableArrayList();
            Data.count = 0;
            Data.id.clear();
            while (result.next()) {
                String string = result.getString("surname") + " ";
                string += result.getString("name") + " ";
                string += result.getString("patronymic") + "  ";
                string += result.getString("date_of_birth");
                row.add(string);
                Data.count++;
                Data.id.add(result.getInt("id"));
            }
            return row;
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
            return null;
        }
    }

    public static String getDescriptionEmployeeById(int id) {
        return getStringById(TableStaff, id, "id", "description");
    }

    public static String getNameDocumentOfEmployeeById(int id) {
        return getStringById(TableStaff, id, "id", "document");
    }

    //= Служебные =====================================================================
    // Получение значений Integer, String
    // String table - имя таблицы
    // String columnId - имя столбца идентификатора
    // .. valueId - значение идентификатора
    // String columnValue - имя столбца со значением
    private static Integer getInteger(String table, String columnId, String valueId, String columnValue){
        String request = "SELECT " + columnValue + " FROM " + table + " WHERE " + columnId + " = '" + valueId + "';";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request);
             ResultSet result = statement.executeQuery()) {
            return result.getInt(columnValue);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }
    private static Integer getInteger(String table, String columnId, int valueId, String columnValue){
        String request = "SELECT " + columnValue + " FROM " + table + " WHERE " + columnId + " = " + valueId + ";";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request);
             ResultSet result = statement.executeQuery()) {
            return result.getInt(columnValue);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }
    private static String getString(String table, String columnId, String valueId, String columnValue){
        String request = "SELECT " + columnValue + " FROM " + table + " WHERE " + columnId + " = '" + valueId + "';";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request);
             ResultSet result = statement.executeQuery()) {
            return result.getString(columnValue);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }
    private static String getString(String table, String columnId, int valueId, String columnValue){
        String request = "SELECT " + columnValue + " FROM " + table + " WHERE " + columnId + " = " + valueId + ";";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request);
             ResultSet result = statement.executeQuery()) {
            return result.getString(columnValue);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }
//----------------

    private static String dateWithZero(String dateNow){
        String[] values = dateNow.split("\\.");
        String str = "";
        if (values[0].length() < 2){
            str += "0" + values[0];
        }else str += values[0];
        str += ".";
        if (values[1].length() < 2){
            str += "0" + values[1];
        }else str += values[1];
        str += "." + values[2];
        return str;
    }

    private static String requestDocument(String choiceTypeOfDocument, String filterDescription) {
        String request = StSelectNameFromDocument;
        if (!choiceTypeOfDocument.equals("All")) {
            request += " WHERE type = '" + choiceTypeOfDocument + "'";
            if (filterDescription != null && !filterDescription.isBlank()) {
                request += " AND description LIKE '%" + filterDescription + "%'";
            }
        } else {
            if (filterDescription != null && !filterDescription.isBlank()) {
                request += " WHERE description LIKE '%" + filterDescription + "%'";
            }
        }
        request += ";";
        System.out.println(request);
        return request;
    }

    private static String requestAutomats(String filterDescription) {
        String request = StSelectFromAutomats;
        if (filterDescription != null && !filterDescription.isBlank()) {
            request += " WHERE description LIKE '%" + filterDescription + "%'";
        }
        request += ";";
        System.out.println(request);
        return request;
    }

    private static String requestEmployees(String filterDescription) {
        String request = StSelectFromStaff;
        if (filterDescription != null && !filterDescription.isBlank()) {
            request += " WHERE description LIKE '%" + filterDescription + "%'";
        }
        request += ";";
        System.out.println(request);
        return request;
    }

    private static boolean updateById(int id, String table, String columnId, String columnContent, String content){
        String request = "UPDATE " + table + " SET " + columnContent + " = ? WHERE " + columnId + " = ?;";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, content);
            statement.setInt(2, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    private static boolean updateByName(String name, String table, String columnName, String columnContent, String content){
        String request = "UPDATE " + table + " SET " + columnContent + " = ? WHERE " + columnName + " = ?;";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, content);
            statement.setString(2, name);
            statement.execute();
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    private static boolean deleteById(int id, String table, String columnId){
        String request = "DELETE FROM " + table + " WHERE " + columnId + " = ?;";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setInt(1, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    private static boolean deleteByName(String name, String table, String columnName){
        String request = "DELETE FROM " + table + " WHERE " + columnName + " = ?;";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, name);
            statement.execute();
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createTable(String request) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(request);
            return true;
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    private static String getStringById(String table, int id, String columnId, String columnString){
        if (id < 0) return "";
        String request = "SELECT " + columnString + " FROM " + table + " WHERE " + columnId + " = ?;";
        ResultSet result = null;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setInt(1, id);
            result = statement.executeQuery();
            return result.getString(columnString);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private static String getStringByName(String table, String name, String columnName, String columnString){
        String request = "SELECT " + columnString + " FROM " + table + " WHERE " + columnName + " = ?;";
        ResultSet result = null;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(request)) {
            statement.setString(1, name);
            result = statement.executeQuery();
            return result.getString(columnString);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException ignored) {
            }
        }
    }



}
