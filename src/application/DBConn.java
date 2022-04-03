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
import java.util.ArrayList;
	
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
				Double price = res.getDouble("Transval");
				String strPrice = String.valueOf(price); 
				Date sqldate = res.getDate("Date"); // sql.Date data type which is used by the database.
				LocalDate date = sqldate.toLocalDate(); // LocalDate which is used in our application.
				String category = res.getString("Category");
				String subcategory = res.getString("Subcategory");
				String transType = res.getString("TransactionType");
				
				if (transType.equals("EXPENSE")) {
					Transaction newTrans = new Transaction(title, price, date, TransactionType.EXPENSE);
					expCon.addTransaction(category, subcategory, newTrans);
					transCon.addTransaction(category, subcategory, newTrans);
				}
				
				else if (transType.equals("INCOME")) {
					Transaction newTrans = new Transaction(title, price, date, TransactionType.INCOME);
					incCon.addTransaction(category, subcategory, newTrans);
					transCon.addTransaction(category, subcategory, newTrans);
				}
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
}

