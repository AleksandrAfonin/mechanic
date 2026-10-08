package sml.worker.data;


import java.util.ArrayList;
import java.util.List;

public class Data {
    private static String documentName = ""; // Если имя документа выбрано (установлено)
    public static int count; // счетчик
    public static List<Integer> id = new ArrayList<>(); // список id-шников элементов списка
    public static int thisElementId = -1; // id-шник конкретного элемента
    public static boolean isAdd = false; // если добавление произошло (индикатор)
    public static boolean isChanged = false; // если изменено (индикатор)

    public static void setDocumentName(String documentName){
        if (documentName == null || documentName.isBlank()){
            Data.documentName = "";
        }else{
            Data.documentName = documentName;
        }
    }

    public static String getDocumentName(){
        String str = Data.documentName;
        Data.documentName = "";
        return str;
    }
}
