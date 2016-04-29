package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ChoosePaneController implements Initializable {
	@FXML protected RadioButton webcamSource,fileSource,goproSource;
	@FXML protected ToggleGroup sourceGroup;
	@FXML protected VBox root;
	@FXML protected AnchorPane choosePaneRoot;
	@FXML protected TextArea serverTextArea;
	@FXML protected Label tcpLabel;
	@FXML protected TextField tcpTextField;
	@FXML protected Button tcpButton;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }
}
