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

public class FileSourcePane extends AnchorPane {

	protected ChoiceBox<String> choiceBox;
	protected VBox root;
	protected Label label;
	protected FileSourcePaneController controller;
	protected TCPServer serverService;
	public FileSourcePane(TCPServer serverService)
	{
		this.serverService=serverService;
		FXMLLoader loader=new FXMLLoader(getClass().getResource("FileSourcePane.fxml"));
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controller=loader.getController();
		root=controller.root;
		root.getChildren().add(new FileSourceUdpPane(serverService));
		label=controller.label;

		choiceBox=controller.choiceBox;
		choiceBox.setItems((FXCollections.observableArrayList("UDP sink","Multi udp sink")));
		choiceBox.getSelectionModel().selectFirst();
		choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
					switch(newValue.intValue())
					{
					case 0:{
						FileSourceUdpPane pane=new FileSourceUdpPane(serverService);
						root.getChildren().set(0, pane);
					    VBox.setVgrow(pane, Priority.ALWAYS);}
					break;
					case 1:
						FileSourceMultiUdpPane pane1=new FileSourceMultiUdpPane(serverService);
						root.getChildren().set(0, pane1);
					    VBox.setVgrow(pane1, Priority.ALWAYS);
					break;
					}
				// TODO Auto-generated method stub

			}



		});
		//getChildren().addAll(label,choiceBox,root);
		getChildren().add(controller.FileSourceAnchor);

	}
}
