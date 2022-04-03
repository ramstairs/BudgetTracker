package application;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

public class Transaction {
	      
    private String name;
    private double value;
    private LocalDate date;
    private TransactionType type;


    public Transaction(String name, double value, LocalDate date, TransactionType type) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Month getMonth() {
		// TODO Auto-generated method stub
		return date.getMonth();
	}
	
	// Returns the TransactionType as a string for insertion/retrieval to/from SQL tables.
	public String getTransType() {
		String type = null;
		if (this.getType() == TransactionType.INCOME)
			type = "INCOME";
		else if (this.getType() == TransactionType.EXPENSE)
			type = "EXPENSE";
		return type;
	}
	
	// Returns the SQL compatible java.sql.Date from the LocalDate of the Transaction.
	public Date getSQLDate() {
		return java.sql.Date.valueOf(this.getDate());
	}
	
}
