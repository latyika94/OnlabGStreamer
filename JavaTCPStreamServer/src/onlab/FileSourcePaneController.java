package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FileSourcePaneController implements Initializable {
	@FXML protected ChoiceBox<String> choiceBox;
	@FXML protected VBox root;
	@FXML protected Label label;
	@FXML protected AnchorPane FileSourceAnchor;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
