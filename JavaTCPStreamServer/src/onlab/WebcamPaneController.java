package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class WebcamPaneController implements Initializable {
	@FXML protected ChoiceBox<String> choiceBox;
	@FXML protected VBox root;
	@FXML protected Label sinkTypeLabel;
	@FXML protected AnchorPane webcamAnchor;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        //assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'simple.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected
       // close.setOnAction(event->System.exit(0));
    }
}
