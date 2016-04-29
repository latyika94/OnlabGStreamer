package onlab;


import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WebcamPane extends AnchorPane {
		protected ChoiceBox<String> choiceBox;
		protected VBox root;
		protected Label label;
		protected WebcamPaneController webcamPaneController;
		protected TCPServer serverService;
		public WebcamPane(TCPServer serverService)
		{
			this.serverService=serverService;
			FXMLLoader loader=new FXMLLoader(getClass().getResource("WebcamPaneXML.fxml"));
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			webcamPaneController=loader.getController();
			root=webcamPaneController.root;

			//
			label=webcamPaneController.sinkTypeLabel;
			root.getChildren().add(new WebcamUdpPane(serverService));

			choiceBox=webcamPaneController.choiceBox;
			choiceBox.setItems((FXCollections.observableArrayList("UDP sink","Multi udp sink")));
			choiceBox.getSelectionModel().selectFirst();
			choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
						switch(newValue.intValue())
						{
						case 0:{
							WebcamUdpPane pane=new WebcamUdpPane(serverService);
							root.getChildren().set(0, pane);
						    VBox.setVgrow(pane, Priority.ALWAYS);}
						break;
						case 1:
							WebcamMultiUdpPane pane1=new WebcamMultiUdpPane(serverService);
							root.getChildren().set(0, pane1);
						    VBox.setVgrow(pane1, Priority.ALWAYS);
						break;
						}
					// TODO Auto-generated method stub

				}



			});
			//getChildren().addAll(label,choiceBox,root);
			getChildren().add(webcamPaneController.webcamAnchor);

		}
}
