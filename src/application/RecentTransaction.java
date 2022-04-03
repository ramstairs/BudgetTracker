package application;

import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class RecentTransaction extends AnchorPane {

	@FXML
	Label transactionTitle, transactionType, transactionAmmount, transactionDate;
	@FXML
	ImageView transactionIco;
	
	private String title, category, type, ammount;
	private TransactionType transType;
	private LocalDate date;
	
	public RecentTransaction(String t, String c,TransactionType ty, String amm, LocalDate d){
		this.title = t;
		this.category = c;
		this.transType = ty;
		this.date = d;
		this.ammount ="$"+adjustAmmount(amm);
		
		
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recentTransactionItem.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
        	throw new RuntimeException(exception);
        }
        
        setIcon();
        transactionTitle.setText(title);
        
        
		if(this.transType == TransactionType.EXPENSE) {
			type = "Expense";
			this.ammount = "- "+this.ammount;
			transactionAmmount.setStyle("-fx-text-fill: red;");
		}else {
			type = "Income";
			this.ammount = "+ "+this.ammount;
			transactionAmmount.setStyle("-fx-text-fill: green;");
		}
		transactionAmmount.setText(ammount);
		transactionType.setText(type);
        transactionDate.setText(date.toString());
	}
	
	private void setIcon() {
		String imgPath = "file:Images/"+this.category+".png";
		Image icon = new Image(imgPath);
		this.transactionIco.setImage(icon);
	}
	public LocalDate getDate() {
		return this.date;
	}
	
	public String getCategoryName() {
		return this.category;
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
