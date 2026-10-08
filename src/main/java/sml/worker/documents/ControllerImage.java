package sml.worker.documents;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import sml.worker.control.ControllerWindow;
import sml.worker.data.DBData;
import sml.worker.data.Data;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ControllerImage implements ControllerWindow {
    public Button btn;
    public TextField txt;
    public AnchorPane pane;
    public ImageView img;
    public ScrollPane scrollPane;
    //public AnchorPane anchorPane;

    private PDDocument document;
    private BufferedImage image;
    private PDFRenderer renderer;

    public void click(MouseEvent mouseEvent) {
        double width = pane.getWidth();
        double height = pane.getHeight();
        txt.setText(width + " x " + height);
    }

    @FXML
    public void initialize() throws IOException {
        System.out.println("wi = " + pane.getWidth() + "   height = " + pane.getHeight());
        //System.out.println("scrollH = " + scrollPane.getWidth() + "   scrollV = " + scrollPane.getHeight());
        //System.out.println("ancH = " + anchorPane.getWidth() + "    ancV = " + anchorPane.getHeight());
        document = DBData.getDocumentByName("wer.pdf");
        renderer = new PDFRenderer(document);
        image = renderer.renderImageWithDPI(1, 200);
        img.setImage(SwingFXUtils.toFXImage(image, null));
        double w = scrollPane.getWidth();
        System.out.println("w = " + w);
        img.setFitWidth(w);
//        img.setFitHeight(pane.getHeight());
//        img.fitHeightProperty().bind(pane.heightProperty());
//        imageView.setFitWidth(stageWidth);
//        imageView.setFitHeight(stageWidth * imageXY);
        img.setPreserveRatio(true);
        img.setSmooth(true);
    }

    @Override
    public void update(){

    }
}
