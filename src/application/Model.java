package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
	private ArrayList<Category> categories = new ArrayList<Category>();
	private HashMap<String,ArrayList<Category>> subCategory = new HashMap<String,ArrayList<Category>>();

	private ArrayList<View> Observers = new ArrayList<View>();
	
	public ArrayList<Category> getCategories(){
		return this.categories;
	}
	
	public HashMap<String,ArrayList<Category>> getSubCategories(){
		return this.subCategory;
	}

	public boolean categoryExist(String c) {
		for(Category el: this.categories) {
			if(el.getName() == c) {
				return true;
			}
		}
		return false;
	}


	public Category getCategoryWithName(String c) {
		for(Category el: this.categories) {
			if(el.getName() == c) {
				return el;
			}
		}
		return null;
	}
	
	public boolean subCategoriesExist(String c, String sc) {
		ArrayList<Category> scList = this.subCategory.get(c);
		for(Category el: scList) {
			if(el.getName() == sc) {
				return true;
			}
		}
		return false;
	}
	
	public Category getSubCategoryWithName(String c, String sc) {// This is added 
		ArrayList<Category> scList = this.subCategory.get(c);
		for(Category el: scList) {
			if(el.getName() == sc) {
				return el;
			}
		}
		return null;
	}
	
	

	public void addCategory(Category category) {
		this.categories.add(category);
		this.notifyObservers();
	}
	
	public void addSubCategory(String c, Category subCategory) {
		this.subCategory.get(c).add(subCategory);
		this.notifyObservers();
	}

	public void addSubCategory(String catagory, ArrayList<Category> emptySubCategory, Category subCategory) {
		this.subCategory.put(catagory, emptySubCategory);
		this.subCategory.get(catagory).add(subCategory);
		this.notifyObservers();
	}
	
	public void addTransaction(String c, String sc, Transaction newTransaction) throws SQLException { // Add a transaction of an existing category to the Model.
		ArrayList<Category> scList = this.subCategory.get(c);
		for(Category el: scList) {
			if(el.getName() == sc) {
				el.addTransaction(newTransaction);
			}
		}
		// DBConn.AddTransToDB(newTransaction, c, sc); // Add the transaction to the SQL database for long-term storage.
		this.notifyObservers();
	}
	
	public void removeTransaction(String c, String sc, Transaction transaction) throws SQLException { // Remove an existing transaction from the Model & DB.
		ArrayList<Category> scList = this.subCategory.get(c);
		for(Category el: scList) {
			if(el.getName() == sc) {
				el.removeTransaction(transaction);
			}
		}
		DBConn.RemoveTransFromDB(transaction, c, sc); // Remove the transaction from the SQL database.
		this.notifyObservers();
	}

	public void attachObserver(View o) {
		Observers.add(o);
	}

	public void detachOberver(View o) {
		Observers.remove(o);
	}
	
	public void notifyObservers() {
		for (View Observer: Observers) {
			Observer.update();
		}
	}	
	
	
}
