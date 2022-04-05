package application;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class TransactionItem extends AnchorPane{
	@FXML
	CheckBox transactionCheck;
	@FXML
	ImageView deleteBtn;
	@FXML
	Label transactionTitle, transactionType, transactionCategory, transactionSubcategory, transactionDate, transactionAmmount;
	
	Transaction transaction;
	
	String title, category, subCategory, amm, tType;
	TransactionType ty;
	LocalDate d;
	
	TransactionsController transCntrl;
	
	public TransactionItem(String category, String subcategory, Transaction newTransaction, TransactionsController transCntrl) {
		this.transaction = newTransaction;
		this.title = newTransaction.getName();
		this.ty = newTransaction.getType();
		this.category = category;
		this.subCategory = subcategory;
		this.d = newTransaction.getDate();
		this.amm = Integer.toString(((int) Math.round(newTransaction.getValue()))); 
		this.transCntrl = transCntrl;
		
		//Making the Transaction Item as a component
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/transactionItem.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	
            loader.load();
        } catch (IOException exception) {
        	throw new RuntimeException(exception);
        }

        
        
		String tType ="";
		if(ty == TransactionType.EXPENSE) {
			this.tType = "Expense";
		}else {
			this.tType = "Income";
		}
        //Setting the components features:
        this.transactionTitle.setText(this.title);
        this.transactionType.setText(this.tType);
        this.transactionCategory.setText(this.category);
        this.transactionSubcategory.setText(this.subCategory);
        this.transactionDate.setText(this.d.toString());
        this.transactionAmmount.setText("$"+adjustAmmount(this.amm));
        
	}

	public Transaction getTransaction() {
		return this.transaction;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getSubcategory() {
		return this.subCategory;
	}
	
	@FXML
	private void deleteTransaction() throws SQLException {
		System.out.println("Deleting: "+ this.title+ ", under:" + this.subCategory+ "...");
		//String t, TransactionType ty, String c, String sc, LocalDate d, String amm
		this.transCntrl.deleteTransaction(this);
	}
	private String adjustAmmount(String amm) {
		int ammLen = amm.length();
		
		if(ammLen ==4) {
			return amm.substring(0, 1)+"K";
		}else if(ammLen ==5) {
			return amm.substring(0, 2)+"K";
		}else if(ammLen ==6) {
			return amm.substring(0, 3)+"K";
		}else if(ammLen ==7) {
			return amm.substring(0, 1)+"M";
		}else if(ammLen ==8) {
			return amm.substring(0, 2)+"M";
		}else if(ammLen ==9) {
			return amm.substring(0, 3)+"M";
		}else if(ammLen ==10) {
			return amm.substring(0, 1)+"B";
		}else if(ammLen ==11) {
			return amm.substring(0, 2)+"B";
		}else if(ammLen ==12) {
			return amm.substring(0, 3)+"B";
		}
		return amm;
	}
}
