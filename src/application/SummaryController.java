package application;

import java.net.URL;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class SummaryController implements View, Initializable {
	
	@FXML
	private LineChart<String, Number> transactionLineChart;
	
	@FXML
	private BarChart<String, Number> categoryBarChart;
	
	@FXML
	private CategoryAxis monthName;
	
	@FXML
	private NumberAxis monthlyTotal;
	
	@FXML
	private CategoryAxis categoryName;
	
	@FXML
	private NumberAxis categoryTotal;
	
	private Model model;
	
	public void setModel(Model m) {
		this.model = m;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	// Populate the axis of the graph with data and create the LineChart object from them.
	private void populateLineChart() { // Going to change this to draw directly from Model or database later.
		
		XYChart.Series<String, Number> expenses = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> income = new XYChart.Series<String, Number>();
		
		expenses.getData().clear();
		income.getData().clear();
		transactionLineChart.getData().clear();
		
		for (int i = 1; i <= 12; i++) // For every month, add its String name and Double value to the expense/income series.
		{
			expenses.getData().addAll(new Data<String, Number>(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), model.getTotalForMonth(i, true)));
			income.getData().addAll(new Data<String, Number>(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), model.getTotalForMonth(i, false)));
		}
		
		expenses.setName("Expenses");
		income.setName("Income");
		
		// Add the data series to the line chart to create a line for both Expenses and Income.
		transactionLineChart.getData().addAll(expenses, income);
		transactionLineChart.layout();
		
	}
	// Bar chart of expenditure (exclusively displays expenditure, not income) in each category throughout the year.
	private void populateBarChart() {
		
		categoryTotal = new NumberAxis(0, 10000, 200);
		categoryName = new CategoryAxis();
		
		XYChart.Series<String, Number> expenditure = new XYChart.Series<String, Number>();
		
		String[] expenseCategoryList = {"Children","Debt","Education","Entertainment","Everyday","Gifts","Health","Home","Insurance","Pets","Technology","Transportation","Travel","Utilities","Invests&eTransfer"};
		
		for (Category c : model.getCategories())
		{
			for (String s : expenseCategoryList) { // Only allow expense categories to populate the bar chart.
				if (c.getName().equals(s));
					expenditure.getData().add(new XYChart.Data<String, Number>(c.getName(), model.getTotalForCategory(c)));
			}
		}
		
		expenditure.setName("Annual Categorical Expenditure");
		
		// Add the expenditure series to the bar chart after clearing its data.
		categoryBarChart.getData().clear();
		categoryBarChart.getData().addAll(expenditure);	
		categoryBarChart.layout();
	}
	
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		populateLineChart();
		populateBarChart();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// Initialize the LineChart of Monthly expenses/income.
		XYChart.Series<String, Number> expenses = new XYChart.Series<String, Number>(); 
		XYChart.Series<String, Number> income = new XYChart.Series<String, Number>();
		transactionLineChart.getData().addAll(expenses, income);
		categoryBarChart.layout();
		
		// Initialize the BarChart of categorical expenditure.
		XYChart.Series<String, Number> expenditure = new XYChart.Series<String, Number>();
		categoryBarChart.getData().addAll(expenditure);
		categoryBarChart.layout();
		categoryBarChart.getXAxis().setAnimated(false);
		categoryBarChart.setCategoryGap(20);
	}
}
