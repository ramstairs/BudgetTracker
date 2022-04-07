package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class SummaryController implements View, Initializable {

	@FXML
	private NumberAxis monthlyTotal;
	
	@FXML
	private CategoryAxis monthName;
	
	private TransactionsController transCont;
	
	private Model model;
	
	public void setModel(Model m) {
		this.model = m;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}