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

public class WebcamMultiUdpPane extends AnchorPane {

	protected Label port1,port2;
	protected TextField port1TextField,port2TextField, sourceTextField;
	protected Button startButton, stopButton, sourceFE;
	protected Label outputMessage;
	protected WebcamMultiUdpPaneController webcamMultiUdpController;
	protected TCPServer serverService;
	protected FileChooser fileChooser;
	public WebcamMultiUdpPane(TCPServer serverService)
	{
		this.serverService=serverService;
		FXMLLoader loader=new FXMLLoader(getClass().getResource("WebcamMultiUdpPaneXML.fxml"));
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webcamMultiUdpController=loader.getController();

		port1=webcamMultiUdpController.port1;
		port2=webcamMultiUdpController.port2;
		port1TextField=webcamMultiUdpController.port1TextField;
		port2TextField=webcamMultiUdpController.port2TextField;
		sourceTextField=webcamMultiUdpController.sourceTextField;
		sourceFE=webcamMultiUdpController.sourceFE;
		startButton=webcamMultiUdpController.startButton;

		stopButton=webcamMultiUdpController.stopButton;

		startButton.setOnAction(event->StartServer());
		stopButton.setOnAction(event->StopServer());
		sourceFE.setOnAction(event->SearchFile(sourceFE));
		getChildren().addAll(webcamMultiUdpController.multiUdpAnchor);
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
		Integer port1,port2=null;
		port1=Integer.parseInt(this.port1TextField.getText());
		port2=Integer.parseInt(this.port2TextField.getText());

		serverService.fileServerTCP.StreamType="MULTIWEBCAM";
		serverService.fileServerTCP.port1=port1;
		serverService.fileServerTCP.port2=port2;
		serverService.fileServerTCP.webcamSource=sourceTextField.getText();
		port1TextField.setDisable(true);
		port2TextField.setDisable(true);
		startButton.setDisable(true);
		System.out.println(	serverService.fileServerTCP.StreamType);
		serverService.fileServerTCP.runOnUI(serverService.fileServerTCP.StreamType);



}public void StopServer()
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
				port2TextField.setDisable(false);
				startButton.setDisable(false);
			}
		};
			Platform.runLater(runOnUI);
	}
}
}