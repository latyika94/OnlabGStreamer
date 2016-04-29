package onlab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainAppController implements Initializable {
	 @FXML protected VBox rootVBox;
	 @FXML protected MenuBar menuBar;
	 @FXML protected Menu mFile, mHelp;
	 @FXML protected MenuItem mExit, mAbout;
	 @FXML protected AnchorPane rootAnchor;



    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

    }
}
