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
		
		for (int i = 1; i <= 12; i++)
		{
			expenses.getData().addAll(new Data<String, Number>(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), model.getTotalForMonth(i, true)));
			income.getData().addAll(new Data<String, Number>(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), model.getTotalForMonth(i, false)));
		}
		
		expenses.setName("Expenses");
		income.setName("Income");
		
		// Add both line plots to the chart after clearing its data.
		transactionLineChart.getData().addAll(expenses, income);
		//transactionLineChart.setLegendVisible(true);
		
	}
	// Bar chart of expenditure (specifically, doesn't include income) in each category throughout the year.
	private void populateBarChart() {
		
		categoryTotal = new NumberAxis(0, 10000, 200);
		categoryName = new CategoryAxis();
		
		XYChart.Series<String, Number> expenditure = new XYChart.Series<String, Number>();
		
		String[] expenseCategoryList = {"Children","Debt","Education","Entertainment","Everyday","Gifts","Health","Home","Insurance","Pets","Technology","Transportation","Travel","Utilities","Invests&eTransfer"};
		
		for (Category c : model.getCategories())
		{
			for (String s : expenseCategoryList) { // Only allow expense categorys to populate the bar chart ( removes income categories with val = 0.0.
				if (c.getName().equals(s));
					expenditure.getData().add(new XYChart.Data<String, Number>(c.getName(), model.getTotalForCategory(c)));
			}
		}
		
		expenditure.setName("Annual Categorical Expenditure");
		
		// Add the expenditure series to the bar chart after clearing its data.
		categoryBarChart.getData().clear();
		categoryBarChart.getData().addAll(expenditure);	
	}
	
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
//		System.out.println("updating charts via notifyObservers");
		populateLineChart();
		populateBarChart();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
//		monthName = new CategoryAxis();
//		monthName.setLabel("Month");
//		monthlyTotal = new NumberAxis(0, 2000, 100); // y limits [$0 -> 150% x $(total spent annually)].
//		monthlyTotal.setLabel("$ Amount");
//		transactionLineChart = new LineChart<String, Number>(monthName, monthlyTotal); // Month as x-axis, month's total as y-axis.
		
		XYChart.Series<String, Number> expenses = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> income = new XYChart.Series<String, Number>();
		
		transactionLineChart.getData().addAll(expenses, income);
		
		
//		categoryTotal = new NumberAxis(0, 10000, 200);
//		categoryTotal.setLabel("$ Amount");
//		categoryName = new CategoryAxis();
//		categoryName.setLabel("Category");
		
		XYChart.Series<String, Number> expenditure = new XYChart.Series<String, Number>();
		categoryBarChart.getData().addAll(expenditure);
		categoryBarChart.setCategoryGap(20);
	}
}
