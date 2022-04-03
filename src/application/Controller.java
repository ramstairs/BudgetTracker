package application;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class Controller {
	
	@FXML
//	AnchorPane expenseAnchorPane = new AnchorPane();
	private StackPane contentArea;
	private TextField numField;
	
	//Constructor to set few constraints and invariants
	public void initialize() {
		
		//Allowing only Numeric values in numField
		numField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	numField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
	}
	
	public void btnHome(ActionEvent e) throws IOException{
		Parent fxml = FXMLLoader.load(getClass().getResource("/homePage.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
		System.out.println("Home");
	}
	
	public void btnExpenses(ActionEvent e) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/expensePage.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
		System.out.println("Expenses");
	}
	
	public void btnIncome(ActionEvent e) throws IOException{
		Parent fxml = FXMLLoader.load(getClass().getResource("/incomePage.fxml"));
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll(fxml);
		System.out.println("Income");
	}
	
	public void btnHistory(ActionEvent e) throws IOException{
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll();
		System.out.println("History");
	}

	public void btnGoals(ActionEvent e) throws IOException{
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll();
		System.out.println("Goals");
	}
	
	public void btnSummary(ActionEvent e) throws IOException{
		contentArea.getChildren().removeAll();
		contentArea.getChildren().setAll();
		System.out.println("Summary");
	}
}