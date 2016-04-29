package onlab;


import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WebcamUdpPane extends AnchorPane {
	protected Label port1;
	protected TextField port1TextField,sourceTextField;;
	protected Button startButton, stopButton,sourceFE;
	protected FileChooser fileChooser;
	protected WebcamMultiUdpPaneController webcamUdpController;
	protected TCPServer serverService;
	protected CheckBox flipBox;
	public WebcamUdpPane(TCPServer serverService)
	{
		this.serverService=serverService;
		FXMLLoader loader=new FXMLLoader(getClass().getResource("WebcamMultiUdpPaneXML.fxml"));
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webcamUdpController=loader.getController();

		webcamUdpController.port2TextField.setDisable(true);
		webcamUdpController.port2.setDisable(true);
		sourceTextField=webcamUdpController.sourceTextField;
		sourceFE=webcamUdpController.sourceFE;
		flipBox=webcamUdpController.flipBox;
		port1=webcamUdpController.port1;
		port1TextField=webcamUdpController.port1TextField;
		startButton=webcamUdpController.startButton;
		stopButton=webcamUdpController.stopButton;
		startButton.setOnAction(event->StartServer());
		stopButton.setOnAction(event->StopServer());
		sourceFE.setOnAction(event->SearchFile(sourceFE));

		getChildren().addAll(webcamUdpController.multiUdpAnchor);
	}

	public void SearchFile(Button button)
	{
		if(button==sourceFE)
		{
			Stage stage=(Stage)button.getScene().getWindow();
			fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile= fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
			   sourceTextField.setText(selectedFile.getAbsolutePath());
			 }
		}
	}

	public void StartServer()
	{
		Integer port1=null;
		port1=Integer.parseInt(this.port1TextField.getText());
		serverService.fileServerTCP.StreamType="SINGLEWEBCAM";
		serverService.fileServerTCP.port1=port1;
		serverService.fileServerTCP.webcamSource=sourceTextField.getText();
		serverService.fileServerTCP.flip=flipBox.isSelected();
		port1TextField.setDisable(true);
		startButton.setDisable(true);
		System.out.println(	serverService.fileServerTCP.StreamType);
		serverService.fileServerTCP.runOnUI(serverService.fileServerTCP.StreamType);

	}
	public void StopServer()
	{
		if(serverService!=null)
		{
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
		}
	}
}
