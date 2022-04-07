package application;

//import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
	
public class DBConn {
		
		private static Connection Conn;
		
		// For use by other classes to access the database connection Conn.
		public static Connection getConn() {return Conn;}

		public static void ConnectToDB() {
			try {
				Class.forName("org.h2.Driver"); // Set the driver to be used (standard h2 driver).
				Conn = DriverManager.getConnection("jdbc:h2:./database/btBeta", "rams", "rstm"); // Connects to database file btBeta in folder ./database/, located in eclipse project folder. 
																								 // If file does not exist, creates a new one.
				System.out.println("Connected to database.");
			
			}	catch (Exception ex) {
				System.out.println("Database connection exception occured.");
			}
		}
		
		// Fetches all transactions from the btBeta.mv.db database file's "Transactions" table, adds them to the current application instance's Income, Expense, & Transactions pages.
		public static void FetchTransactions(ExpenseController expCon, IncomeController incCon, TransactionsController transCon) throws SQLException {
			System.out.println("Fetching Transactions from database...");
			
			Statement st = DBConn.getConn().createStatement(); // Allows us to specify a command to the database in a string with SQL language, then execute it.
			
			// Select the Transactions table in its entirety and put the results in ResultSet res.
			// Using the .executeQuery("SQL commands...") on our Statement st, we send an SQL command or "query" to the database.
			ResultSet res = st.executeQuery("SELECT * FROM TRANSACTIONS");
			
			while (res.next()) { // Loop through all of the rows in the ResultSet taken from the table... (1 row = 1 transaction)
				// res is equivalent to a row of the table, res.getDATA("Example") gives us the data stored in column "Example", of data type DATA, at the current row res.
				
				String title = res.getString("Name"); 
				int price = (int) Math.round(res.getDouble("Transval"));
				Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
				LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
				String category = res.getString("Category");
				String subcategory = res.getString("Subcategory");
				String transType = res.getString("TransactionType");
				
				if (transType.equals("EXPENSE")) {
					Transaction newTrans = new Transaction(title, price, date, TransactionType.EXPENSE);
					expCon.addTransaction(category, subcategory, newTrans);
					transCon.addToTransactionList(category, subcategory, newTrans);
				}
				
				else if (transType.equals("INCOME")) {
					Transaction newTrans = new Transaction(title, price, date, TransactionType.INCOME);
					incCon.addTransaction(category, subcategory, newTrans);
					transCon.addToTransactionList(category, subcategory, newTrans);
				}
			}
		}
		
//		// Fetches all transactions from the btBeta.mv.db database file's "Transactions" table, adds them to the current application instance's Income, Expense, & Transactions pages.
//		public static void PopulateModel(Model m) throws SQLException {
//			System.out.println("Fetching Transactions from database...");
//			
//			Statement st = DBConn.getConn().createStatement(); // Allows us to specify a command to the database in a string with SQL language, then execute it.
//			
//			// Select the Transactions table in its entirety and put the results in ResultSet res.
//			// Using the .executeQuery("SQL commands...") on our Statement st, we send an SQL command or "query" to the database.
//			ResultSet res = st.executeQuery("SELECT * FROM TRANSACTIONS");
//			
//			while (res.next()) { // Loop through all of the rows in the ResultSet taken from the table... (1 row = 1 transaction)
//				// res is equivalent to a row of the table, res.getDATA("Example") gives us the data stored in column "Example", of data type DATA, at the current row res.
//				
//				String title = res.getString("Name"); 
//				int price = (int) Math.round(res.getDouble("Transval"));
//				Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
//				LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
//				String category = res.getString("Category");
//				String subcategory = res.getString("Subcategory");
//				String transType = res.getString("TransactionType");
//				
//				// Add the transaction to the model.
//				if (transType.equals("EXPENSE")) {
//					Transaction newTrans = new Transaction(title, price, date, TransactionType.EXPENSE); 
//					m.addTransaction(category, subcategory, newTrans);
//				}
//				else if (transType.equals("INCOME")) {
//					Transaction newTrans = new Transaction(title, price, date, TransactionType.INCOME);
//					m.addTransaction(category, subcategory, newTrans);
//				}		
//			}
//		}
		
		// Fetches all transactions from the btBeta.mv.db database file's "Transactions" table, adds them to the current application instance's Income, Expense, & Transactions pages.
		public static ArrayList<RecentTransaction> FetchRecents(Boolean expensesOnly) throws SQLException {
			ArrayList<RecentTransaction> recents = new ArrayList<RecentTransaction>() ;
			Statement st = DBConn.getConn().createStatement(); // Allows us to specify a command to the database in a string with SQL language, then execute it.
			// Select the Transactions table in its entirety and put the results in ResultSet res.
			// Using the .executeQuery("SQL commands...") on our Statement st, we send an SQL command or "query" to the database.
			ResultSet res = st.executeQuery("SELECT * FROM TRANSACTIONS ORDER BY TransID DESC");
			
			int count = 1;
				
			
			while (res.next() && count <= 10) { // Loop through all of the rows in the ResultSet taken from the table... (1 row = 1 transaction)
				// res is equivalent to a row of the table, res.getDATA("Example") gives us the data stored in column "Example", of data type DATA, at the current row res.
				
				String title = res.getString("Name"); 
				int price = (int) Math.round(res.getDouble("Transval"));
				String strPrice = String.valueOf(price); 
				Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
				LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
				String category = res.getString("Category");
				String transType = res.getString("TransactionType");
				
				if (transType.equals("EXPENSE")) {
					recents.add(new RecentTransaction(title, category, TransactionType.EXPENSE, strPrice, date));
					
					if (expensesOnly)
						count += 1;
				}
				
				else if (transType.equals("INCOME") && (!expensesOnly)) {
					recents.add(new RecentTransaction(title, category, TransactionType.INCOME, strPrice, date));
					count += 1;
				}		
			}
			
			return recents;
		}
		
		
		
		// Fetches the Budget that was set for the month passed in.
		// Used in setting the Budget text field in initialization of the Home Page in the HomeController.
		public static Double FetchBudget(Month month, HomeController hc) {
			
			Double budget = 0.0;
			Boolean budgetFound = false;
			try {
				Statement st = DBConn.getConn().createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM BUDGETS"); // Create a ResultSet object holding all rows.
				
				while (rs.next()) {
					if (rs.getInt("MonthNum") == month.getValue())
					{
						budget = rs.getDouble("Budget");
						budgetFound = true;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (budgetFound = true) {
				hc.setMonthlyBudget(budget);
				//hc.setLeftToSpend();
				return budget;
			}
			else { 
				hc.setMonthlyBudget(0.0);
				//hc.setLeftToSpend();
				return 0.0;
			}
		}
		
		// Adds a new transaction to the MySQL Database in the table of transactions.
		public static void AddTransToDB(Transaction trans, String category, String subcategory) throws SQLException {
			try {
				String query = "insert into transactions (Name, Transval, Date, Category, Subcategory, TransactionType)" + " values (?, ?, ?, ?, ?, ?)"; // Formatted query, each ? represents a column's value for a single row.
				PreparedStatement st = DBConn.getConn().prepareStatement(query);
				st.setString(1, trans.getName()); // Insert trans.getName() (transaction title) to the place of the first ? above, the Name column.
				st.setDouble(2, trans.getValue()); // Insert trans.getValue() to the second ? above, the Transval column.
				st.setDate(3, trans.getSQLDate());
				st.setString(4, category);
				st.setString(5, subcategory);
				st.setString(6, trans.getTransType());
				
				st.execute(); // Execute the preparedStatement (send the data to the newest row of the Transactions table).
				
			} catch (SQLException e) { 
			
				System.out.println("An error occured while trying to add the transaction to the Database.");
			} finally {
				System.out.println("Transaction: " + trans.getName() + " has been added to the Database.");
			}	
		}
		
		public static void RemoveTransFromDB(Transaction trans, String category, String subcategory) throws SQLException {
			try {
				Statement st = DBConn.getConn().createStatement(); // Allows us to specify a command to the database in a string with SQL language, then execute it.
				
				// Select the Transactions table in its entirety and put the results in ResultSet res.
				// Using the .executeQuery("SQL commands...") on our Statement st, we send an SQL command or "query" to the database.
				ResultSet res = st.executeQuery("SELECT * FROM TRANSACTIONS");
				
				while (res.next()) { // Loop through all of the rows in the ResultSet taken from the table... (1 row = 1 transaction)
					// res is equivalent to a row of the table, res.getDATA("Example") gives us the data stored in column "Example", of data type DATA, at the current row res.
					String title = res.getString("Name"); 
					Double price = res.getDouble("Transval");
					Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
					LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
					String cat = res.getString("Category");
					String subcat = res.getString("Subcategory");
					String transType = res.getString("TransactionType");
					
					// If the current transaction matches all attributes of the transaction we're deleting...
					if (title.equals(trans.getName()) && price.equals(trans.getValue()) && date.equals(trans.getDate()) && category.equals(cat) && subcategory.equals(subcat) && transType.equals(trans.getTransType())) {
						String query = "DELETE FROM Transactions WHERE TRANSID =" + res.getInt("TransID");
						PreparedStatement st2 = DBConn.getConn().prepareStatement(query);
						st2.execute();
						System.out.println("Transaction: " + trans.getName() + ", with ID: " + res.getInt("TransID") + " has been deleted from the Database.");
					}
				}
			} catch (SQLException e) { 
				System.out.println("An error occured while trying to delete the transaction from the Database.");
			}
		}
		
		public static void AddBudgetToDB(Month month, Double budget) {
			try {
				String query = "UPDATE Budgets set Budget = ? where MonthNum = ?";
				
				PreparedStatement st = getConn().prepareStatement(query);
				
				st.setDouble(1, budget);
				
				st.setInt(2, month.getValue());
				
				st.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Searches the local database file for a specific table. Returns false if not found.
		public static Boolean checkForTable(String name) throws SQLException {
			
			DatabaseMetaData meta = getConn().getMetaData(); // Metadata provides information about the database itself, i.e. calling meta.getTables() gives us a list of all tables
															 // that satisfy the passed in parameters.
			String[] types = {"TABLE"};
			
			ResultSet resultset = meta.getTables(null,  null, name, null); // We only want to search for a table by the passed in name arument, all other arguments can be null.
			
			return resultset.next(); // If the first // 0th entry of the ResultSet containing the tables with our argument isnt null, returns true. If there is no table in the set, returns false.
		}
		
		// Creates a Transaction table in the local H2 database.
		public static void createTransTable() throws SQLException {
			
			Statement st = getConn().createStatement();
			
			// SQL statement to create a Transactions table to hold user data.
			String sql = "CREATE TABLE Transactions " +
					"( TransID INTEGER NOT NULL AUTO_INCREMENT, " +
					" Name VARCHAR(255) NOT NULL, " +
					" Transval DOUBLE NOT NULL, " +
					" Date DATE NOT NULL, " +
					" Category VARCHAR(255) NOT NULL, " +
					" Subcategory VARCHAR(255) NOT NULL, " +
					" TransactionType VARCHAR(255) NOT NULL, " +
					" PRIMARY KEY ( TransID ))";
			
			st.executeUpdate(sql); // Execute the SQL statement in our database.
		}
		
		// Creates a Budget table in the local H2 database.
		public static void createBudgetTable() throws SQLException {
			Statement st = getConn().createStatement();
			// SQL statement to create a Budget table to hold user data.
			String sql = "CREATE TABLE Budgets " +
					"( MonthNum INTEGER NOT NULL, " +
					" MonthName VARCHAR(255) NOT NULL, " +
					" Budget DOUBLE NOT NULL, " +
					" PRIMARY KEY ( MonthNum ))";
			st.executeUpdate(sql); // Execute the SQL statement in our database.
			
			for (int i = 1; i <= 12; i++)
			{
				String query = "INSERT INTO Budgets (MonthNum, MonthName, Budget)" + " values (?, ?, ?)";
				PreparedStatement st2 = getConn().prepareStatement(query);
				st2.setInt(1, i);
				st2.setString(2, Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
				st2.setDouble(3, 0.0);
				st2.execute(); // Execute the SQL statement in our database.
			}
		}
		
		// Parent function to check for all necessary tables (Transactions & Budget), and creates them if they don't exist.
		public static void tableSetUp() throws SQLException {
			
			if (DBConn.checkForTable("TRANSACTIONS") == true) { // Transactions table already in database.
				System.out.println("Previous Transaction record found in database...");	
			}
			else { // No Transactions table in database, create one using createTransTable().
				DBConn.createTransTable();
				System.out.println("Transactions record created in database.");
			}
			if (DBConn.checkForTable("BUDGETS") == true) { // Transactions table already in database.
				System.out.println("Previous Budget record found in database...");
			}
			else { // No Transactions table in database, create one using createTransTable().
				DBConn.createBudgetTable();
				System.out.println("Budget record created in database.");
			}
		}
}

