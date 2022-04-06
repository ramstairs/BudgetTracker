package application;

import java.math.BigDecimal;
//import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;



public class HomeController implements View, Initializable{
	
	@FXML
	private TextField titleField, numField, leftToSpendField, monthlyBudgetField;
	@FXML
	private DatePicker dateField;
	@FXML
	private ChoiceBox<String> categoryField, subCategoryField;
	@FXML
	private RadioButton typeIncome, typeExpense; 
	@FXML
	private ProgressBar budgetMeter;
	@FXML
	private VBox recentTransactionsList;
	@FXML
	private PieChart categoryPie;
	
	private ExpenseController expenseController;
	private IncomeController incomeController;
	private TransactionsController transactionsController;
	
	private HashMap<String, String[]> options = new HashMap<String, String[]>();
	
	//Expenses Options:
	private String[] expenseCategoryList = {"Children","Debt","Education","Entertainment","Everyday","Gifts","Health","Home","Insurance","Pets","Technology","Transportation","Travel","Utilities","Invests&eTransfer"};

	private String[] Children = {"Activities","Allowance","Medical","Childcare","Clothing","School","Toys","Other"};
	private String[] Debt = {"Credit cards","Student loans","Other loans","Taxes (federal)","Taxes (state)","Other"};
	private String[] Education = {"Tuition","Books","Lessons","Other"};
	private String[] Entertainment = {"Books","Concerts/shows","Games","Hobbies","Movies","Music","Outdoor activities","Photography","Sports","Theater/plays","TV","Other"};
	private String[] Everyday = {"Groceries","Restaurants","Personal supplies","Clothes","Laundry/dry cleaning","Hair/beauty","Subscriptions","Other"};
	private String[] Gifts = {"Gifts","Donations (charity)","Other"};
	private String[] Health= {"Doctors/dental/vision","Specialty care","Pharmacy","Emergency","Other"};
	private String[] Home = {"Rent/mortgage","Property taxes","Furnishings","Lawn/garden","Supplies","Maintenance","Improvements","Moving","Other"};
	private String[] Insurance = {"Car","Health","Home","Life","Other"};
	private String[] Pets = {"Food","Vet/medical","Toys","Supplies","Other"};
	private String[] Technology = {"Domains & hosting","Online services","Hardware","Software","Other"};
	private String[] Transportation = {"Fuel","Car payments","Repairs","Registration/license","Supplies","Public transit","Other"};
	private String[] Travel = {"Airfare","Hotels","Food","Transportation","Entertainment","Other"};
	private String[] Utilities = {"Phone","TV","Internet","Electricity","Heat/gas","Water","Trash","Other"};
	private String[] Invests = {"Investments","eTransfer"};

	//Income Options:
	private String[] incomeCategoryList = {"Wages","Other Income"};
	
	private String[] Wages = {"Paycheck","Bonus", "Others"};
	private String[] incomeOther = {"Transfer from savings","Interest Income", "Dividends", "Gifts", "Refunds", "Investments"};
	
	//The list set for the recent transactions
	private ArrayList<RecentTransaction> recentList = new ArrayList<RecentTransaction>();
	private ArrayList<Category> recentCategories = new ArrayList<Category>();

	//Model object
	private Model model;
	//The monthly budget value set by the user
	private Double monthlyBudget = 0.0;
	//The result of the left to spend
	private Double leftToSpend;
	
	BigDecimal progressValue = new BigDecimal(String.format("%.2f", 0.0));
	
	// Sets the list of recent transactions and the related visual elements to the 10 most recent fetched from the DB.
	// Called in AddTransaction() after the transaction is added to the DB.
	public void updateRecentTrans() throws SQLException {
		
		recentList = DBConn.FetchRecents(); // Grabs 10 most recent transactions from Transactions table.
		recentTransactionsList.getChildren().clear(); // Clear previous list of most recent transactions.
		
		for (RecentTransaction r : recentList) { // Set the children of the recentTransactionsList to the new list.
			recentTransactionsList.getChildren().add(r);
		}
	}
	
	public void loadData() {
		
		// Sets the private field recentCategories, the list of the most recent categories spent in.
		for (int i = 0; i < recentList.size(); i++) // For all recent transactions...
		{
			Category newCat = model.getCategoryWithName(recentList.get(i).getCategoryName()); // Get category of RecentTransaction...
			
			if (recentCategories.size() == 0) // If recent categories list is empty, add it.
				recentCategories.add(newCat);
			
			Boolean addToRecentCats = true;
			
			for (int j = 0; j < recentCategories.size(); j++) { // If the category of RecentTransaction at recentList.get(i) is nowhere in recentCategories, add it.
				if (recentCategories.get(j).getName().equals(recentList.get(i).getCategoryName()))
				{
					addToRecentCats = false;
				}
			}
				
			if (addToRecentCats) {
				recentCategories.add(newCat);
			}	
		}
		
		// Now gather the data to put in the PieChart.
		ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
		
		for (Category c : recentCategories) {
			list.add(new PieChart.Data(c.getName(), c.getCurrMonth()));
		}
		
		String thisMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		
		categoryPie.setData(list);
		categoryPie.setTitle(thisMonth + " Categorical Spending");
		categoryPie.setClockwise(true);
		categoryPie.setLabelLineLength(10);
		categoryPie.setLabelsVisible(true);
		categoryPie.setLegendVisible(true);
		categoryPie.setLegendSide(Side.LEFT);
	}
		
	@FXML
	private void addTransaction() throws SQLException {
		//Get the data
		String title = titleField.getText();
		String price = numField.getText();
		LocalDate date = dateField.getValue();
		String category = categoryField.getValue();
		String subCategory = subCategoryField.getValue();
		TransactionType typeTransaction = getTransactionType();

		//Add the transaction if the data valid - Not Done
		if(fieldsValid()) {
			
			// Add the new valid Transaction to the Transactions table of the database.
			Transaction trans = new Transaction(title, (int) Math.round(Double.valueOf(price)), date, typeTransaction);
			try {
				DBConn.AddTransToDB(trans, category, subCategory);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			//Adding it to the list of Transactions
			this.transactionsController.addTransaction(category, subCategory, trans);
			
			updateRecentTrans(); // Set the recent transactions to the updated 10 newest.
			//loadData();
			
			//Adding the Transaction to Income Sheet:
			if(typeTransaction == TransactionType.INCOME) {
				incomeController.addTransaction(category, subCategory, trans);
				//recentTransactionsList.getChildren().add( new RecentTransaction(title, category, TransactionType.INCOME, price, date));//Adding to the recent transactions menu
			}//Adding the Transaction to Expense Sheet:
			else if (typeTransaction == TransactionType.EXPENSE) {
				expenseController.addTransaction(category, subCategory, trans);
				//recentTransactionsList.getChildren().add( new RecentTransaction(title, category, TransactionType.EXPENSE , price, date));//Adding to the recent transactions menu
			}

			//Clear the fields while keeping the date and the category
			titleField.clear();
			numField.clear();
			
			//Update the left to spend field 
			setLeftToSpendField();
			model.notifyObservers();
			
			loadData();
		}

	}

	@FXML
	private boolean fieldsValid() {
		//Only works on current Year
		int currentYr = LocalDate.now().getYear();
		
		String title = titleField.getText();
		String price = numField.getText();
		LocalDate date = dateField.getValue();
		String category = categoryField.getValue();
		String type = subCategoryField.getValue();
		
		//Check The Category Field
		if (category == null) {
			categoryField.setStyle("-fx-mark-color: red; -fx-border-width: 2px;");
			new animatefx.animation.Shake(categoryField).play();
			return false;
		}else {
			categoryField.setStyle(null);
		}
		//Check The Option Field
		if (type == null) {
			subCategoryField.setStyle("-fx-mark-color: red; -fx-border-width: 2px;");
			new animatefx.animation.Shake(subCategoryField).play();
			return false;
		}else {
			subCategoryField.setStyle(null);
		}
		//Check The Price Field
		if (price.length() == 0) {
			numField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			new animatefx.animation.Shake(numField).play();
			return false;
		}else {
			numField.setStyle(null);
		}
		//Check The Date Field
		if (date == null || date.getYear() != currentYr) {
			dateField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			new animatefx.animation.Shake(dateField).play();
			return false;
		}else {
			dateField.setStyle(null);
		}
		//Check The Title Field
		if (title.length() == 0) {
			titleField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			new animatefx.animation.Shake(titleField).play();
			return false;
		}else {
			titleField.setStyle(null);
		}
		
		return true;
	}
	
	private TransactionType getTransactionType() {
		if(typeIncome.isSelected()) {
			return TransactionType.INCOME;
		}else {
			return TransactionType.EXPENSE;
		}
	}
	
	public void setExpenseController(ExpenseController c) {
		this.expenseController = c;
	}
	
	public void setIncomeController(IncomeController c) {
		this.incomeController = c;
	}
	
	public void setTransactionController(TransactionsController c) {
		this.transactionsController = c;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
//		//Allowing only Numeric values in these Text Fields:
		numField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	numField.setText(newValue.replaceAll("[^\\d]", "") );	//Integers not doubles
		        }
		    }
		});
		//Initialize the left to spend Field
		monthlyBudgetField.setText(String.valueOf(DBConn.FetchBudget(LocalDate.now().getMonth(), this))); // Set this to the value in the the Budget table for the current month.
		monthlyBudgetField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	monthlyBudgetField.setText(newValue.replaceAll("[^\\d]", "") );	//Integers not doubles.

		        }else if(newValue.matches("\\d*")){
		        	try {
						setMonthlyBudget();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//		        	System.out.println("monthlyBudgetField Updating");
		        }
		    }
		});
		
		//Initializing categories with their corresponding options.
		//Expense Options
		options.put("Children", Children);
		options.put("Debt", Debt);
		options.put("Education", Education);
		options.put("Entertainment", Entertainment);
		options.put("Everyday", Everyday);
		options.put("Gifts", Gifts);
		options.put("Health", Health);
		options.put("Home", Home);
		options.put("Insurance", Insurance);
		options.put("Pets", Pets);
		options.put("Technology", Technology);
		options.put("Transportation", Transportation);
		options.put("Travel", Travel);
		options.put("Utilities", Utilities);
		options.put("Invests&eTransfer", Invests);
		//Income Options
		options.put("Wages", Wages);
		options.put("Other Income", incomeOther);
		
		//Initializing predetermined List of Categories to the drop-down menu.
		setCategoryDropDown();

		//Setting the date field to the current date of the machine
		dateField.setValue(LocalDate.now());
	}
	
	public void setModel(Model m) {
		this.model = m;
//		System.out.println("HomeController| Model has been set. 2");
	}
	
	public Model getModel() {
		return this.model;
	}
	
	@FXML
	private void setMonthlyBudget() throws InterruptedException {
		//This function responsible for setting the "Monthly Budget" field
		this.monthlyBudget = Double.valueOf(monthlyBudgetField.getText());
		
		DBConn.AddBudgetToDB(LocalDate.now().getMonth(), this.monthlyBudget); // Update the budget amount for the current month in the Budgets table.
		
		setLeftToSpendField(); // Update the LeftToSpend Field based on the budget value.
	}
	
	public Double getMonthlyBudget() {
		return this.monthlyBudget;
	}
	
	public void setMonthlyBudget(Double d) {
		this.monthlyBudget = d;
	}
	
	public void setLeftToSpend() {
		
		Month currMonth = LocalDate.now().getMonth();
		Double value = 0.0;
		
		if(currMonth==Month.JANUARY) {
			value = expenseController.getRootCategory().getJan();
		}else if(currMonth==Month.FEBRUARY) {
			value = expenseController.getRootCategory().getFeb();
		}else if(currMonth==Month.MARCH) {
			value = expenseController.getRootCategory().getMar();
		}else if(currMonth==Month.APRIL) {
			value = expenseController.getRootCategory().getApr();
		}else if(currMonth==Month.MAY) {
			value = expenseController.getRootCategory().getMay();
		}else if(currMonth==Month.JUNE) {
			value = expenseController.getRootCategory().getJun();
		}else if(currMonth==Month.JULY) {
			value = expenseController.getRootCategory().getJul();
		}else if(currMonth==Month.AUGUST) {
			value = expenseController.getRootCategory().getAug();
		}else if(currMonth==Month.SEPTEMBER) {
			value = expenseController.getRootCategory().getSep();
		}else if(currMonth==Month.OCTOBER) {
			value = expenseController.getRootCategory().getOct();
		}else if(currMonth==Month.NOVEMBER) {
			value = expenseController.getRootCategory().getNov();
		}else if(currMonth==Month.DECEMBER) {
			value = expenseController.getRootCategory().getDec();
		}
		
		this.leftToSpend = (monthlyBudget - value);
	}
	
	//This function responsible for setting the "Left To Spend" field
	public void setLeftToSpendField() {
		
		Month currMonth = LocalDate.now().getMonth();
		Double value = 0.0;
		
		if(currMonth==Month.JANUARY) {
			value = expenseController.getRootCategory().getJan();
		}else if(currMonth==Month.FEBRUARY) {
			value = expenseController.getRootCategory().getFeb();
		}else if(currMonth==Month.MARCH) {
			value = expenseController.getRootCategory().getMar();
		}else if(currMonth==Month.APRIL) {
			value = expenseController.getRootCategory().getApr();
		}else if(currMonth==Month.MAY) {
			value = expenseController.getRootCategory().getMay();
		}else if(currMonth==Month.JUNE) {
			value = expenseController.getRootCategory().getJun();
		}else if(currMonth==Month.JULY) {
			value = expenseController.getRootCategory().getJul();
		}else if(currMonth==Month.AUGUST) {
			value = expenseController.getRootCategory().getAug();
		}else if(currMonth==Month.SEPTEMBER) {
			value = expenseController.getRootCategory().getSep();
		}else if(currMonth==Month.OCTOBER) {
			value = expenseController.getRootCategory().getOct();
		}else if(currMonth==Month.NOVEMBER) {
			value = expenseController.getRootCategory().getNov();
		}else if(currMonth==Month.DECEMBER) {
			value = expenseController.getRootCategory().getDec();
		}
		
		// Set LeftToSpend to the current budget - current expenditure this month.
		this.leftToSpend = (monthlyBudget - value);
		
		leftToSpendField.setText( String.valueOf(this.leftToSpend));
		
		try {
			//Updating the Progress Bar (Budget Meter)
			if(0<= (int) Math.round(this.leftToSpend/monthlyBudget)) {
				progressValue = new BigDecimal(String.format("%.5f", (this.leftToSpend/monthlyBudget)*1.0 ));
				this.budgetMeter.setProgress(progressValue.doubleValue());
			}else {
//				System.out.println("ZERO!");
				progressValue = new BigDecimal(String.format("%.2f", 0.0));
				this.budgetMeter.setProgress(progressValue.doubleValue());
			}
		}catch(Exception e) {
			
		}
	}
	
	public Double getLeftToSpend() {
		return this.leftToSpend;
	}
	
	@FXML
	private void setCategoryDropDown(){
//		System.out.println("HomeController| setCategoryDropDown");
//		categoryField.setValue("");
		subCategoryField.getItems().clear();
		categoryField.getItems().clear();
		TransactionType typeTransaction = getTransactionType();
		if(typeTransaction == TransactionType.INCOME) {
			categoryField.getItems().addAll(incomeCategoryList);
		}//Adding the Transaction to Expense Sheet:
		else if (typeTransaction == TransactionType.EXPENSE) {
			categoryField.getItems().addAll(expenseCategoryList);
		}
	}
	
	@FXML
	private void updateOptions() {//This function gets called whenever the category changes to update the options
		try {
			String[] subCategory = options.get( categoryField.getValue() );
			subCategoryField.getItems().clear();
			subCategoryField.getItems().addAll(subCategory);
		}catch(Exception e) {
			System.out.println(e);
		}

	}

	@Override
	public void update() {
	}


}
