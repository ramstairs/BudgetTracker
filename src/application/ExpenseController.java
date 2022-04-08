package application;


import java.util.ArrayList;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

public class ExpenseController implements View, Initializable{

	@FXML
	private TreeTableView<Category> expenseTreeTableView;
	@FXML 
	private TreeTableColumn<Category, String> colCategory;
	@FXML
	private TreeTableColumn<Category, Number> colJan, colFeb, colMar, colApr, colMay, colJun,colJul, colAug, colSep, colOct, colNov, colDec, colTotal, colAvg;
	
	
	private Category rootCategory = new Category("Expenses",true, null);
	//The root node for all the categories
	private TreeItem<Category> root = new TreeItem<Category>(rootCategory );
	
	private Model model;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		root.setExpanded(true);
		
		root.expandedProperty().addListener(observable -> {
            if (!root.isExpanded()) {
            	root.setExpanded(true);
            }
        });
		
		// Defines how to fill data for each cell.
		// Get value from property of Category.
		colCategory.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Category, String> arg0) {
				return new SimpleStringProperty(arg0.getValue().getValue().getName());
			}
		});
		
//		colJan.setCellValueFactory( param -> new SimpleDoubleProperty( param.getValue().getValue().getJan() ) );
		colJan.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getJan());
			}
		});
		colFeb.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getFeb()) ;
			}
		});
		colMar.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getMar());
			}
		});
		colApr.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getApr());
			}
		});
		colMay.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getMay());
			}
		});
		colJun.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getJun());
			}
		});
		colJul.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getJul());
			}
		});
		colAug.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getAug());
			}
		});
		colSep.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getSep());
			}
		});
		colOct.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getOct());
			}
		});
		colNov.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getNov());
			}
		});
		colDec.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getDec());
			}
		});
		colTotal.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getTotal());
			}
		});
		colAvg.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Category, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<Category, Number> arg0) {
				return new SimpleDoubleProperty(arg0.getValue().getValue().getAvg());
			}
		});
		
		expenseTreeTableView.setRoot(root);
		setUpTable();
	}

	
	public void addTransaction(String cat, String subcat, Transaction newTransaction) throws SQLException {

		String category = cat;
		String subCategory = subcat;
		
		//If the category already exists
		if(this.model.categoryExist(category)) {
			
			// check if the subCategory exists:
			if(this.model.subCategoriesExist(category, subCategory)) {
				// Add the transaction to the subCategory
				this.model.addTransaction(cat, subcat, newTransaction);
			}
			//If the option doesnt exist
			else {
				//Get the Category
				Category c = this.model.getCategoryWithName(category);
				//Create new option
				Category newSubCategory = new Category(subCategory,false, c);
				// Add the Child(newSubCategory) to the Parent(newCategory)
				c.addToChildren(newSubCategory);
				// Add the transaction to the option
				newSubCategory.addTransaction(newTransaction);
				// Add the subCategory to the list of subCategories of that category	
				this.model.addSubCategory(category, newSubCategory);
				
				//Tree view
				//Get the Category
				TreeItem<Category> categoryTreeItem = this.getCategoryFromTree(c);
				this.addSubCategoryToTree(categoryTreeItem, newSubCategory);
			}
		}
		//If the category does not exist
		else {
			//Create a new Category
			Category newCategory = new Category(category,true, rootCategory);
			// Add the Child(newCategory) to the Parent(rootCategory) - this is for the GUI TreeView
			rootCategory.addToChildren(newCategory);
			//Create a new Option
			Category newSubCategory = new Category(subCategory,false, newCategory);
			// Add the Child(newOption) to the Parent(newCategory)
			newCategory.addToChildren(newSubCategory);
			// Add the transaction to the subCategory
			newSubCategory.addTransaction(newTransaction);
			// Add the category to the list of categories
			this.model.addCategory(newCategory);
			// Add a an empty array list of newSubCategory to the newCategory
			this.model.addSubCategory(category, new ArrayList<Category>(), newSubCategory);
			
			//Tree View
			TreeItem<Category> categoryTreeItem = this.addCategoryToTree(newCategory);
			this.addSubCategoryToTree(categoryTreeItem, newSubCategory);
		}
	}
	
	public Category getRootCategory() {
		return this.rootCategory;
	}
	
	public void setModel(Model m) {
		this.model = m;
		setUpTable();
	}
	
	public TreeItem<Category> addCategoryToTree(Category c) {
		//Get the children of the root
		ObservableList<TreeItem<Category>> rootChildren = root.getChildren();
		TreeItem<Category> categoryTreeItem = new TreeItem<Category>(c);
		categoryTreeItem.setExpanded(true);
		categoryTreeItem.expandedProperty().addListener(observable -> {
            if (!categoryTreeItem.isExpanded()) {
            	categoryTreeItem.setExpanded(true);
            }
        });
		//add the category to the table
		rootChildren.add(categoryTreeItem);
		return categoryTreeItem;
	}

	private TreeItem<Category> getCategoryFromTree(Category c) {//find the category
		ObservableList<TreeItem<Category>> rootChildren = root.getChildren();
		for (TreeItem<Category> categoryTreeItem: rootChildren) {
			if(c == categoryTreeItem.getValue()) {
				return categoryTreeItem;
			}
		}
		return null;
	}
	
	public void addSubCategoryToTree(TreeItem<Category> c, Category p) {
		//Get the children of the root
		ObservableList<TreeItem<Category>> rootChildren = root.getChildren();
		//create an subcategory node 
		TreeItem<Category> subCategory = new TreeItem<Category>(p);
		//find the category 
		int categoryTreeItemINDEX = rootChildren.indexOf(c);
		TreeItem<Category> categoryTreeItem = rootChildren.get(categoryTreeItemINDEX);
		// add the subcategory under the category
		categoryTreeItem.getChildren().add(subCategory);
	}
	
	// Returns the total value of a month's expenses.
	public Double monthlyExpenses(int m) { 
		Month currMonth = LocalDate.now().getMonth();
		Double value = 0.0;
		
		if(Month.of(m)==Month.JANUARY) {
			value = this.getRootCategory().getJan();
		}else if(currMonth==Month.FEBRUARY) {
			value = this.getRootCategory().getFeb();
		}else if(currMonth==Month.MARCH) {
			value = this.getRootCategory().getMar();
		}else if(currMonth==Month.APRIL) {
			value = this.getRootCategory().getApr();
		}else if(currMonth==Month.MAY) {
			value = this.getRootCategory().getMay();
		}else if(currMonth==Month.JUNE) {
			value = this.getRootCategory().getJun();
		}else if(currMonth==Month.JULY) {
			value = this.getRootCategory().getJul();
		}else if(currMonth==Month.AUGUST) {
			value = this.getRootCategory().getAug();
		}else if(currMonth==Month.SEPTEMBER) {
			value = this.getRootCategory().getSep();
		}else if(currMonth==Month.OCTOBER) {
			value = this.getRootCategory().getOct();
		}else if(currMonth==Month.NOVEMBER) {
			value = this.getRootCategory().getNov();
		}else if(currMonth==Month.DECEMBER) {
			value = this.getRootCategory().getDec();
		}
		return value;
	}
	
	// Returns the total value of a month's expenses.
	public Double expensesOfMonth(int m) { 
		Double value = 0.0;
		
		if(Month.of(m)==Month.JANUARY) {
			value = this.getRootCategory().getJan();
		}else if(Month.of(m)==Month.FEBRUARY) {
			value = this.getRootCategory().getFeb();
		}else if(Month.of(m)==Month.MARCH) {
			value = this.getRootCategory().getMar();
		}else if(Month.of(m)==Month.APRIL) {
			value = this.getRootCategory().getApr();
		}else if(Month.of(m)==Month.MAY) {
			value = this.getRootCategory().getMay();
		}else if(Month.of(m)==Month.JUNE) {
			value = this.getRootCategory().getJun();
		}else if(Month.of(m)==Month.JULY) {
			value = this.getRootCategory().getJul();
		}else if(Month.of(m)==Month.AUGUST) {
			value = this.getRootCategory().getAug();
		}else if(Month.of(m)==Month.SEPTEMBER) {
			value = this.getRootCategory().getSep();
		}else if(Month.of(m)==Month.OCTOBER) {
			value = this.getRootCategory().getOct();
		}else if(Month.of(m)==Month.NOVEMBER) {
			value = this.getRootCategory().getNov();
		}else if(Month.of(m)==Month.DECEMBER) {
			value = this.getRootCategory().getDec();
		}
		return value;
	}
	
	// this function is responsible for updating all items inside table when newly instance of the app is made 
	public void setUpTable() {
		if(this.model != null) {
			//Get the children of the root
			ObservableList<TreeItem<Category>> rootChildren = root.getChildren();
			for( Category c: this.model.getCategories()) {
				//create a category item and make it "always" expanded
				TreeItem<Category> category = new TreeItem<Category>(c);
				category.setExpanded(true);
				category.expandedProperty().addListener(observable -> {
		            if (!category.isExpanded()) {
		            	category.setExpanded(true);
		            }
		        });
				
				//add the category to the table
				rootChildren.add(category);
				
				//get the options of that category 
				ArrayList<Category> cList = this.model.getSubCategories().get(c.getName());
				// check if the options exist
				if (cList != null) {
					//Loop through the options if not empty
					if(!cList.isEmpty()) {
						for(Category p: cList) {
							//create an option node and add it under its category
							TreeItem<Category> option = new TreeItem<Category>(p);
							category.getChildren().add(option);
						}
					}
				}
			}
		}
	}


	@Override
	public void update() {
		expenseTreeTableView.refresh();
	}
}
