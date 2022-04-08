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
        //Creating a search behavior for the search field
        //newValue is the what we are looking for
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {            
            if ( newValue != "" && newValue != null) {
                //create a temporary array for the matching transactions
                ArrayList<TransactionItem> wantedTransactions = new ArrayList<TransactionItem>();
                // loop through the transactions and see if they exist
                for (TransactionItem tr : TransactionItemList) {
                    if(newValue.equals(tr.getTransaction().getName()) || newValue.equals(Double.toString(tr.getTransaction().getValue())) || newValue.equals(tr.category) || newValue.equals(tr.subCategory)) {
                        // if the name of the transaction, category, subcategory, or amount, or date is found then add it
                        wantedTransactions.add(tr);
                    }
                }
                //Display the found transactions
                transactionsList.getChildren().setAll(wantedTransactions);
            }else {
                //If the search is empty, then display all of the transactions
                transactionsList.getChildren().setAll(TransactionItemList);
            }
        });        
    }

	public void setModel(Model m) {
		this.model = m;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void addToTransactionList(String category, String subcategory, Transaction newTransaction) {
		TransactionItem newTransactionItem = new TransactionItem(category, subcategory, newTransaction, this);
		TransactionItemList.add(newTransactionItem);
	}

	public void deleteTransaction(TransactionItem transactionItem) throws SQLException{
		//Called only from transactionItem class (controller)
		TransactionItemList.remove(transactionItem);
		this.model.removeTransaction(transactionItem.category, transactionItem.subCategory, transactionItem.getTransaction());
	}

	@Override
	public void update() {
		transactionsList.getChildren().setAll(TransactionItemList);
	}
}
