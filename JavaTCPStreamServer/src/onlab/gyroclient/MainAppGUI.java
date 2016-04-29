package onlab.gyroclient;



import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainAppGUI extends Application {
	 protected Stage primaryStage;
	 protected Scene scene;
	 protected Parent parent;

	protected TextField serverIPTextField;
		protected Label serverIPlabel;
		protected Label coords;
	protected TextArea gotCoords;
	protected Button startButton;
	 protected AnchorPane root;
	 protected VBox vbox;



	@Override
	public void start(Stage primaryStage) {

		/*TCPServer serverService=new TCPServer("SERVER");
		serverService.start();*/

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Coordinates");

		vbox=new VBox(0);


		scene=new Scene(vbox,600,500);

		primaryStage.setScene(scene); // Az ablak mérete és
        primaryStage.centerOnScreen(); // Az ablak a képernyő közepén legyen


        startButton = new Button("Connect to server");
        startButton.setLayoutX(340);
        startButton.setLayoutY(13);
        startButton.setOnAction(event->StartClient());
        serverIPTextField=new TextField();
        serverIPTextField.setLayoutX(150);
        serverIPTextField.setLayoutY(13);

        serverIPlabel=new Label("Server IP address: ");
        serverIPlabel.setLayoutX(10);
        serverIPlabel.setLayoutY(18);

        coords=new Label("Messages: ");
        coords.setLayoutX(10);
        coords.setLayoutY(56);

        gotCoords=new TextArea();
        gotCoords.setLayoutX(150);
        gotCoords.setLayoutY(56);
        gotCoords.setPrefWidth(327);
        gotCoords.setPrefHeight(200);
        gotCoords.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                gotCoords.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });


        root=new AnchorPane();
        root.setLayoutX(0);
        root.setLayoutY(0);
        root.getChildren().addAll(startButton,serverIPlabel,serverIPTextField,coords,gotCoords);


       //vbox.getChildren().add(menuBar);
       //vbox.getChildren().add(new ChoosePane(scene));  // Placeholder panel*/

       /*primaryStage.setOnCloseRequest(event -> close());
       primaryStage.show();
       controller=loader.getController();
      // vbox.getChildren().add(main)

       controller.startButton.setOnAction(event->StartClient());

       vbox.getChildren().add(controller.root);*/
      // AnchorPane a=mainAppController.rootAnchor;
        vbox.getChildren().add(root);
        primaryStage.setOnCloseRequest(event -> close());
        primaryStage.show();


	}
	public void StartClient()
	{
		String ip=serverIPTextField.getText();
		UdpClient u=new UdpClient(ip,gotCoords);
		u.start();
		System.out.println("Lefutott");
	}
	public void close()
	{
		 System.exit(0);
	}
	public static void main(String[] args) {
		launch(args);
	}
}