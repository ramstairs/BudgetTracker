package application;

//import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;




public class TransactionsController implements View, Initializable{
	
	@FXML
	TextField searchField;
	@FXML
	VBox transactionsList;
	
	private ArrayList<TransactionItem> TransactionItemList = new ArrayList<TransactionItem>();
	
	private Model model;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		EventHandler<ActionEvent> searchEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {	//Check if the field isn't empty
            	if ( searchField.getText() != "" && searchField.getText() != null) {
                	//create a temporary array for the matching transactions
            		ArrayList<TransactionItem> wantedTransactions = new ArrayList<TransactionItem>();
                	//Get the text of what we are looking for
            		String lookUp = searchField.getText();
            		for (TransactionItem tr : TransactionItemList) {
                		if(lookUp.equals(tr.getTransaction().getName()) || lookUp.equals(Double.toString(tr.getTransaction().getValue())) || lookUp.equals(tr.category) || lookUp.equals(tr.subCategory)) {
                			// if the name of thetransaction, category,subcategory, or ammount, or date is found then add it
                			wantedTransactions.add(tr);
                		}
                	}
                	//Display the found
                	transactionsList.getChildren().setAll(wantedTransactions);
            	}else {
            		//If the search is empty, then display all of the transactions
            		transactionsList.getChildren().setAll(TransactionItemList);
            	}
            }
        };
		searchField.setOnAction(searchEvent);
	}

	public void setModel(Model m) {
		this.model = m;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void addTransaction(String category, String subcategory, Transaction newTransaction) {
		TransactionItem newTransactionItem = new TransactionItem(category, subcategory, newTransaction, this);
		TransactionItemList.add(newTransactionItem);
	}

	public void deleteTransaction(TransactionItem transactionItem) {
		//Called only from transactionItem class (controller)
		TransactionItemList.remove(transactionItem);
		this.model.removeTransaction(transactionItem.category, transactionItem.subCategory, transactionItem.getTransaction());
	}
	
	// Fetches all transactions from the btBeta.mv.db database file's "Transactions" table, adds them to the current application instance's Income & Expense pages.
	public void PopulateTransactions() throws SQLException {
		transactionsList.getChildren().clear();
		System.out.println("Fetching Transactions from database...");
		Statement st = DBConn.getConn().createStatement(); // Allows us to specify a command to the database in a string with SQL language, then execute it.
		
		// Select the Transactions table in its entirety and put the results in ResultSet res.
		// Using the .executeQuery("SQL commands...") on our Statement st, we send an SQL command or "query" to the database.
		ResultSet res = st.executeQuery("SELECT * FROM TRANSACTIONS");
		while (res.next()) { // Loop through all of the rows in the ResultSet taken from the table... (1 row = 1 transaction)
			// res is equivalent to a row of the table, res.getDATA("Example") gives us the data stored in column "Example", of data type DATA, at the current row res.
			
			String title = res.getString("Name"); 
			Double price = res.getDouble("Transval");
			String strPrice = String.valueOf(price); 
			Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
			LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
			String category = res.getString("Category");
			String subcategory = res.getString("Subcategory");
			String transType = res.getString("TransactionType");
			
			TransactionType type = null;
			
			if (transType.equals("INCOME"))
				type = TransactionType.INCOME;
			else if (transType.equals("EXPENSE"))
				type = TransactionType.EXPENSE;
			
			Transaction newTrans = new Transaction(title, price, date, type);
			addTransaction(category, subcategory, newTrans);
		}
	}

	@Override
	public void update() {
		transactionsList.getChildren().setAll(TransactionItemList);
	}
	


}
