package onlab;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
	 protected Stage primaryStage;
	 protected Scene scene;
	 protected VBox vbox;
	 protected MenuBar menuBar;
	 protected Menu mFile, mHelp;
	 protected MenuItem mExit, mAbout;
	 protected Parent parent;
	 protected MainAppController mainAppController;


	@Override
	public void start(Stage primaryStage) {

		TCPServer serverService=new TCPServer("SERVER");
		//serverService.start();

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("CameraServer");
		FXMLLoader loader=new FXMLLoader(getClass().getResource("MainAppXML.fxml"));
		//vbox=new VBox(0);
		try {
			parent=loader.load();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scene=new Scene(parent,710,600);

		primaryStage.setScene(scene); // Az ablak mérete és
        primaryStage.centerOnScreen(); // Az ablak a képernyő közepén legyen
        //vbox.getChildren().add(menuBar);
        //vbox.getChildren().add(new ChoosePane(scene));  // Placeholder panel*/

        primaryStage.setOnCloseRequest(event -> close());
        primaryStage.show();


        mainAppController=loader.getController();
        mainAppController.mExit.setOnAction(event -> close());
        vbox = mainAppController.rootVBox;
        vbox.getChildren().add(new ChoosePane(scene,serverService));
       // vbox.getChildren().add(new WebcamMultiUdpPane(new TCPServer("SERVER",30) ));
       // vbox.getChildren();
       // AnchorPane a=mainAppController.rootAnchor;

	}
	public void close()
	{
		String[] args2 = new String[] {"/bin/bash", "-c", "killall -9 gst-launch-1.0"};
		try {
			@SuppressWarnings("unused")
			Process proc2 = new ProcessBuilder(args2).start();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 System.exit(0);
	}
	public static void main(String[] args) {

		launch(args);


	}
}
