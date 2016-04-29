package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class WebcamMultiUdpPaneController implements Initializable {
	@FXML protected Label port1,port2;
	@FXML protected TextField port1TextField,port2TextField, sourceTextField;
	@FXML protected Button startButton, stopButton, sourceFE;
	@FXML protected AnchorPane multiUdpAnchor;
	@FXML protected CheckBox flipBox;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }
}
