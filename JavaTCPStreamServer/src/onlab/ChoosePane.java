package onlab;


import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ChoosePane extends AnchorPane {


	protected RadioButton webcamSource,fileSource,goproSource;
	protected ToggleGroup sourceGroup;
	protected VBox root;
	protected ChoosePaneController choosePaneController;
	protected TCPServer serverService;
	protected TextArea serverText;
	protected Label tcpLabel;
	protected TextField tcpTextField;
	protected Button tcpButton;

	public ChoosePane(Scene app,TCPServer service){

		        FXMLLoader loader=new FXMLLoader(getClass().getResource("ChooseP.fxml"));
		        try {
					loader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        choosePaneController=loader.getController();
		        this.root=choosePaneController.root;
		        this.serverService=service;
				serverService.fileServerTCP.textArea=serverText;
				serverText=choosePaneController.serverTextArea;
				serverText.textProperty().addListener(new ChangeListener<Object>() {
		            @Override
		            public void changed(ObservableValue<?> observable, Object oldValue,
		                    Object newValue) {
		                serverText.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
		                //use Double.MIN_VALUE to scroll to the top
		            }
		        });
		        root.getChildren().add(new WebcamPane(serverService));

		        System.out.println(choosePaneController);
		        root.getChildren().add(serverText);
		        /*this.serverService=new TCPServer("SERVER",7777);
				this.serverService.start();*/

				this.sourceGroup=choosePaneController.sourceGroup;

				this.webcamSource=choosePaneController.webcamSource;
				webcamSource.setSelected(true);

				this.fileSource=choosePaneController.fileSource;

				this.goproSource=choosePaneController.goproSource;
				this.tcpButton=choosePaneController.tcpButton;
				this.tcpButton.setOnAction(event->StartTCPServer());
				this.tcpLabel=choosePaneController.tcpLabel;
				this.tcpTextField=choosePaneController.tcpTextField;
				//getChildren().clear();

				webcamSource.setOnAction(event->changePane(new WebcamPane(serverService)));
				goproSource.setOnAction(event->new AnchorPane());
				fileSource.setOnAction(event ->changePane(new FileSourcePane(serverService)));
				getChildren().addAll(choosePaneController.choosePaneRoot);
			}

	protected void changePane(Pane newPane) {
		root.getChildren().set(0, newPane);
	    VBox.setVgrow(newPane, Priority.ALWAYS);
	  }
	public void StartTCPServer()
	{
		serverService.setPORT_NUMBER(Integer.parseInt(tcpTextField.getText()));
		serverService.fileServerTCP.textArea=serverText;
		serverService.start();
	}
	}
