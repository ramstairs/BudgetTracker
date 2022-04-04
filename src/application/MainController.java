package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable{
	
	@FXML
	private StackPane contentArea;
	@FXML
	private Button btnHome, btnExpenses, btnIncome, btnTransactions, btnSummary;
	
	private Button currBtn = null;
	
	private String btnSet = "-fx-background-color: #C3E5ED; -fx-border-color:#1199B7; -fx-border-width: 0px 0px 0px 3px; -fx-text-fill: #1199B7; -fx-font-weight: bold;";
	
	private Page currPage;
	
	private Model model;
	
	private Parent homeRoot;
	private Parent incomeRoot;
	private Parent expenseRoot;
	private Parent transactionRoot;
//	private Parent summaryRoot;
	
	private FXMLLoader homeFXML;
	private FXMLLoader incomeFXML;
	private FXMLLoader expenseFXML;
	private FXMLLoader transactionFXML;
//	private FXMLLoader summaryFXML;
	
	private HomeController homeController;
	private ExpenseController expenseController;
	private IncomeController incomeController;
	private TransactionsController transactionController;
//	private SummaryController summaryController;
	
	public void btnHome(ActionEvent e) throws IOException{
		if ( currPage != Page.HOME) {
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(this.homeRoot);
			currPage = Page.HOME;
			setButton(btnHome);
		}
	}
	
	public void btnExpenses(ActionEvent e) throws IOException {
		if ( currPage != Page.EXPENSES) {
			
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(this.expenseRoot);
			currPage = Page.EXPENSES;
			setButton(btnExpenses);
		}
	}
	
	public void btnIncome(ActionEvent e) throws IOException{
		if ( currPage != Page.INCOME) {
			
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(this.incomeRoot);
			currPage = Page.INCOME;		
			setButton(btnIncome);
		}
	}
	
	public void btnTransaction(ActionEvent e) throws IOException{
		if ( currPage != Page.TRANSACTIONS) {	
			
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(this.transactionRoot);
			currPage = Page.TRANSACTIONS;
			setButton(btnTransactions);
		}
	}

	public void btnSummary(ActionEvent e) throws IOException{
		if ( currPage != Page.SUMMARY) {
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll();
			currPage = Page.SUMMARY;
			setButton(btnSummary);
		}
	}

	private void setButton(Button b) {
		if(currBtn != null) {
			currBtn.setStyle(null);
		}
		b.setStyle(btnSet);
		currBtn = b;
	}
	
	public void setModel(Model m) throws SQLException {
		this.model = m;
		if(this.homeController != null) {//Set The Home controller's Database model And set Observers
			this.homeController.setModel(m);
			this.model.attachOberver(homeController);
		}if(this.expenseController != null) {//Set The Expense controller's Database model
			this.expenseController.setModel(m);
			this.model.attachOberver(expenseController);
		}if(this.incomeController != null) {//Set The Income controller's Database model
			this.incomeController.setModel(m);
			this.model.attachOberver(incomeController);
		}if(this.transactionController != null) {//Set The Transactions controller's Database model
			this.transactionController.setModel(m);
			this.model.attachOberver(transactionController);
		}
		DBConn.FetchTransactions(expenseController, incomeController, transactionController);
		homeController.updateRecentTrans();
	}
	
	public Model getModel() {
		return this.model;
	}
	
	private void loadFiles() {
	//loading all the other files
		try {
			//Loading Home Page
			this.homeFXML = new FXMLLoader(getClass().getResource("/homePage.fxml"));//Getting the FXML file
			this.homeRoot = this.homeFXML.load();//loading the FXML
			this.homeController = this.homeFXML.getController();//Getting the controller class
		} catch(Exception e) {System.out.println(e);}

		try {
			//Loading Expense Page
			this.expenseFXML = new FXMLLoader(getClass().getResource("/expensePage.fxml"));//Getting the FXML file
			this.expenseRoot = expenseFXML.load();
			this.expenseController = this.expenseFXML.getController();
		} catch(Exception e) {System.out.println(e);}
		
		try {
			//Loading Income Page
			this.incomeFXML = new FXMLLoader(getClass().getResource("/incomePage.fxml"));
			this.incomeRoot = this.incomeFXML.load();
			this.incomeController = this.incomeFXML.getController();
		} catch(Exception e) {System.out.println(e);}
		try {
			//Loading Transactions Page
			this.transactionFXML = new FXMLLoader(getClass().getResource("/transactionsPage.fxml"));//Getting the FXML file
			this.transactionRoot = this.transactionFXML.load();
			this.transactionController = this.transactionFXML.getController();
		} catch(Exception e) {System.out.println(e);}
	
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.loadFiles();// MUST LOAD FILES FIRST
			this.homeController.setExpenseController(expenseController);
			this.homeController.setIncomeController(incomeController);
			this.homeController.setTransactionController(transactionController);
			//setting the home page as the first page
			contentArea.getChildren().removeAll();
			contentArea.getChildren().setAll(this.homeRoot);
			currPage = Page.HOME;
			setButton(btnHome);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
}