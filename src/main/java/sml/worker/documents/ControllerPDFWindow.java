package sml.worker.documents;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import sml.worker.control.ControllerWindow;
import sml.worker.data.DBData;
import sml.worker.data.Data;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ControllerPDFWindow implements ControllerWindow {
    public ProgressBar progressbar;
    public Label labelCurrentPage;
    public Label labelPages;
    public Button btnPreviewPage;
    public Button btnNextPage;
    public TextField textFieldPage;
    public ImageView imageView;
    public Button btnGoToPage;
    public GridPane gridPane;
    public ScrollPane scrollPane;
    public Label labelBookMark;
    public Button btnGoToBookmark;
    public Button btnSetBookmark;

    private String docName;
    private int pages;
    private int page;
    private PDFRenderer renderer;

    @FXML
    public void initialize(){
        docName = Data.getDocumentName();
        PDDocument document = DBData.getDocumentByName(docName);
        if (document == null) return;
        renderer = new PDFRenderer(document);
        pages = document.getNumberOfPages();
        labelPages.setText(String.valueOf(pages));
        labelBookMark.setText(DBData.getBookmarkDocument(docName));
        page = 0;
        labelCurrentPage.setText(String.valueOf(page));
        imageView.fitWidthProperty().bind(scrollPane.widthProperty());
        setProperty();
    }

    private void setProperty() {
        BufferedImage image;
        try {
            image = renderer.renderImageWithDPI(page, 200);
        } catch (IOException e) {
            //TODO
            throw new RuntimeException(e);
        }
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
        scrollPane.setVvalue(0.0);
        progressbar.setProgress((double) page / (pages - 1));
        labelCurrentPage.setText(String.valueOf(page + 1));
    }

    @Override
    public void update() {
        initialize();
    }

    public void previewPage(MouseEvent mouseEvent) {
        if (page == 0) {
            return;
        }
        previewPage();
    }

    private void previewPage(){
        --page;
        setProperty();
    }

    public void nextPage(MouseEvent mouseEvent) {
        if (page == (pages - 1)) {
            return;
        }
        nextPage();
    }

    private void nextPage(){
        ++page;
        setProperty();
    }

    public void goToPage(MouseEvent mouseEvent) {
        goToPag(textFieldPage.getText());
    }

    public void keyReleased(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if (keyCode == KeyCode.ENTER){
            goToPag(textFieldPage.getText());
            btnGoToPage.requestFocus();
        }
    }

    private void goToPag(String number){
        if (number.isBlank()){
            return;
        }
        int numberPage;
        try {
            numberPage = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return;
        }
        if (numberPage < 1 || numberPage > pages){
            return;
        }
        page = numberPage - 1;
        setProperty();
    }

    public void scroll(ScrollEvent scrollEvent) {
        if (scrollPane.getVvalue() == 1.0 && scrollEvent.getDeltaY() < 0 && page < pages - 1){
            nextPage();
        }
        if (scrollPane.getVvalue() == 0.0 && scrollEvent.getDeltaY() > 0 && page > 0){
            previewPage();
            scrollPane.setVvalue(1.0);
        }
    }

    public void goToBookmark(MouseEvent mouseEvent) {
        goToPag(labelBookMark.getText());
    }

    public void setBookmark(MouseEvent mouseEvent) {
        String bookmark = labelCurrentPage.getText();
        if (DBData.updateBookmarkDocument(docName, bookmark)){
            labelBookMark.setText(bookmark);
            System.out.println("Set bookmark is Ok");
        }else{
            System.out.println("Set bookmark false");
        }
    }
}
