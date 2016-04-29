package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class FileSourceMultiUdpPaneController implements Initializable {
	@FXML protected Label port1,port2,path1,path2;
	@FXML protected TextField port1TextField,port2TextField,path1TextField,path2TextField;
	@FXML protected Button startButton, stopButton, search1, search2;
	@FXML protected AnchorPane multiUdpAnchor;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }
}
