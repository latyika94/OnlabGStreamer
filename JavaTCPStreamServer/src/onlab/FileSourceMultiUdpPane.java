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

public class FileSourceMultiUdpPane extends AnchorPane {

	protected Label port1,port2,path1,path2;
	protected TextField port1TextField,port2TextField,path1TextField,path2TextField;
	protected Button startButton, stopButton, search1,search2;
	protected Label outputMessage;
	protected TCPServer serverService;
	protected FileSourceMultiUdpPaneController controller;
	protected FileChooser fileChooser;
	public FileSourceMultiUdpPane(TCPServer serverService)
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
		port2=controller.port2;
		port1TextField=controller.port1TextField;
		port2TextField=controller.port2TextField;

		path1TextField=controller.path1TextField;
		path2TextField=controller.path2TextField;

		startButton=controller.startButton;
		stopButton=controller.stopButton;
		search1=controller.search1;
		search2=controller.search2;

		startButton.setOnAction(event->StartServer());
		stopButton.setOnAction(event->StopServer());
		search1.setOnAction(event->SearchFile(search1));
		search2.setOnAction(event->SearchFile(search2));
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
		Integer port1,port2=null;
		port1=Integer.parseInt(this.port1TextField.getText());
		port2=Integer.parseInt(this.port2TextField.getText());
			/*fileServer = new FileServer(7777, ip1, port1,port2);
			RunFileSourceStream r=new RunFileSourceStream(path1TextField.getText(),path2TextField.getText(),port1,port2,ip1,ip2,controller,fileServer);
			Thread t=new Thread(r);
			t.start();*/
			//server=new TCPServer("SERVER");
			/*r=new RunFileSourceStream2(path1TextField.getText(),path2TextField.getText(),port1,port2,controller);
			Thread t=new Thread(r);
			t.start();*/
		serverService.fileServerTCP.StreamType="MULTIFILE";
		serverService.fileServerTCP.filePath1=path1TextField.getText();
		serverService.fileServerTCP.filePath2=path2TextField.getText();
		serverService.fileServerTCP.port1=port1;
		serverService.fileServerTCP.port2=port2;
		port1TextField.setDisable(true);
		port2TextField.setDisable(true);
		startButton.setDisable(true);
		path1TextField.setDisable(true);
		path2TextField.setDisable(true);
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
					port2TextField.setDisable(false);
					startButton.setDisable(false);
				}

 		};
 			Platform.runLater(runOnUI);

		}
	}
}
