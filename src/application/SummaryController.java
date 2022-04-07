package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class SummaryController implements View, Initializable {

	@FXML
	private NumberAxis monthlyTotal; // Line Chart Y-Axis 
	
	@FXML
	private CategoryAxis monthName; // Line Chart X-Axis
	
	@FXML
	private LineChart<String, Number> transactionLineChart;
	
	@FXML
	private BarChart<String, Number> categoryBarChart;
	
	private TransactionsController transCont;
	
	private Model model;
	
	public void setModel(Model m) {
		this.model = m;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void populateLineChart() {
		
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
