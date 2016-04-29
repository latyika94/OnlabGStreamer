package onlab;


import java.io.File;

import java.io.IOException;



import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FileSourceUdpPane extends AnchorPane {
	protected Label port1;
	protected TextField port1TextField,port2TextField,path1TextField,path2TextField;
	protected Button startButton, stopButton, search1,search2;
	protected FileServer fileServer;
	protected FileSourceMultiUdpPaneController controller;
	protected FileChooser fileChooser;
	protected TCPServer serverService;
	public FileSourceUdpPane(TCPServer serverService)
	{
		this.serverService=serverService;
		FXMLLoader loader=new FXMLLoader(getClass().getResource("FileSourceMultiUdpPane.fxml"));
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controller=loader.getController();

		port1=controller.port1;
		port1TextField=controller.port1TextField;
		port2TextField=controller.port2TextField;
		port2TextField.setDisable(false);
		path1TextField=controller.path1TextField;
		path2TextField=controller.path2TextField;
		path2TextField.setDisable(true);
		controller.port2.setDisable(true);
		controller.search2.setDisable(true);


		startButton=controller.startButton;
		stopButton=controller.stopButton;
		search1=controller.search1;
		search2=controller.search2;


		controller.port2TextField.setDisable(true);
		controller.port2.setDisable(true);

		path2TextField.setDisable(true);
		startButton=controller.startButton;

		stopButton=controller.stopButton;

		startButton.setOnAction(event->StartServer());
		stopButton.setOnAction(event->StopServer());
		search1.setOnAction(event->SearchFile(search1));
		getChildren().addAll(controller.multiUdpAnchor);
	}
	public void SearchFile(Button button)
	{
		if(button==search1)
		{
			Stage stage=(Stage)button.getScene().getWindow();
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile= fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
			   path1TextField.setText(selectedFile.getAbsolutePath());
			 }
		}
		else{
			Stage stage=(Stage)button.getScene().getWindow();
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile= fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
			   path2TextField.setText(selectedFile.getAbsolutePath());
			 }
		}
	}


	public void StartServer()
	{

	}
	public void StopServer()
	{
		if(fileServer!=null){
			fileServer.sendMessage("STOPSTREAMING");
		String[] args2 = new String[] {"/bin/bash", "-c", "killall -9 gst-launch-1.0"};
		try {
			@SuppressWarnings("unused")
			Process proc2 = new ProcessBuilder(args2).start();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 Runnable runOnUI = new Runnable()
 		{

				@Override
				public void run() {

					port1TextField.setDisable(false);
					startButton.setDisable(false);

				}

 		};
 Platform.runLater(runOnUI);
		fileServer.interrupt();
		fileServer=null;
		}
	}
}
