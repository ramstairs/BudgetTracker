package application;
	
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
//import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	public Model model = new Model();

	
	@Override
	public void start(Stage stage) {
		try {

			//Getting the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
			//loading the FXML
			Parent root = loader.load();
			//Getting the controller class
			MainController controller = loader.getController();
			//passing the model to it
			controller.setModel(this.model);
			
			//I can iniate the Model here and pass it by calling the loader's controller...
			Scene scene = new Scene(root,750,500,Color.web("EAE9F0"));
			scene.getStylesheets().add("/application.css");

			//making an icon and setting it
			Image icon = new Image("TB.png");
			stage.getIcons().add(icon);
			
			//setting App Title
			stage.setTitle("Budget Tracker");

			//setting scene to the created scene
			stage.setScene(scene);
			stage.show();	//Displaying the scene
			
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) throws SQLException {
		
		DBConn.ConnectToDB(); // Establish a connection to the database upon startup.
		DBConn.tableSetUp();
		launch(args);
	}
}
