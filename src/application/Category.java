package application;

import java.time.Month;
import java.util.ArrayList;
import java.time.LocalDate;

public class Category {
	private String name = "Null";
	private boolean isParent;
	private Category parent = null;
	private ArrayList<Category> children = new ArrayList<Category>();
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private double jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec,total,avg;

	public Category(String name, boolean parent, Category p) {
		jan=feb=mar=apr=may=jun=jul=aug=sep=oct=nov=dec=total=avg = 0.0;
		
		this.setName(name);
		this.isParent = parent;
		this.parent = p;
	} 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public ArrayList<Category> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Category> children) {
		this.children = children;
	}
	public void addToChildren(Category child) {
		this.children.add(child);
	}
	public void removeFromChildren(Category child) {
		this.children.remove(child);
	}
	//This function is used only if the category is a parent of options
	public void updateParentValues() {
		if(this.isParent) {
			jan=feb=mar=apr=may=jun=jul=aug=sep=oct=nov=dec=total=avg = 0.0;
			
			for(Category child: this.children) {
				jan += child.getJan();
				feb += child.getFeb();
				mar += child.getMar();
				apr += child.getApr();
				may += child.getMay();
				jun += child.getJun();
				jul += child.getJul();
				aug += child.getAug();
				sep += child.getSep();
				oct += child.getOct();
				nov += child.getNov();
				dec += child.getDec();
				total += child.getTotal();
				avg += child.getAvg();				
			}
			if (this.parent == null) {
//				System.out.println("Category| This is: "+this.name+ " 1st Child: "+this.children.get(0).getName() );
//				//Wage				//PayCheck
//				this.children.get(0).children.get(0).printTransactions();
				return;
			}
		}
		if(this.parent != null) {
			this.parent.updateParentValues();			
		}else if(this.isParent) {
			this.updateParentValues();
		}
	}
	
	
	public ArrayList<Transaction> getTransactions(){
		return transactions;
	}
	public void printTransactions(){
		System.out.println("Category| Printing Transactions...");
		if(transactions.isEmpty()) {
			System.out.println("Category| No transaction");
		}
		for(Transaction el: transactions) {
			System.out.print(el.getName() + "| ");
		}
		System.out.println("");
	}
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		updateValues(transaction, true);
		parent.updateParentValues();
	}
	
	public void removeTransaction(Transaction transaction) {//This is wrong because when a transacion is passed it adds doesnt subtract
		updateValues(transaction, false);	//This has to occur first.
		transactions.remove(transaction);
		parent.updateParentValues();
	}
	
	//This function responsible for Summing all months expenses including Total and average
	public void updateValues(Transaction transaction, boolean addition) {
		//Loops through all the transactions within this category and updates
		for(Transaction t : transactions) {
			if(t == transaction) {
				Month month = t.getMonth();
				if(addition) {
					addTransToValues(t, month);
				}else {
					subTransToValues(t, month);
				}
			}
		}
	}


	private void subTransToValues(Transaction t, Month month) {
		if(month==Month.JANUARY) {
			jan -= t.getValue();
		}else if(month==Month.FEBRUARY) {
			feb -= t.getValue();
		}else if(month==Month.MARCH) {
			mar -= t.getValue();
		}else if(month==Month.APRIL) {
			apr -= t.getValue();
		}else if(month==Month.MAY) {
			may -= t.getValue();
		}else if(month==Month.JUNE) {
			jun -= t.getValue();
		}else if(month==Month.JULY) {
			jul -= t.getValue();
		}else if(month==Month.AUGUST) {
			aug -= t.getValue();
		}else if(month==Month.SEPTEMBER) {
			sep -= t.getValue();
		}else if(month==Month.OCTOBER) {
			oct -= t.getValue();
		}else if(month==Month.NOVEMBER) {
			nov -= t.getValue();
		}else if(month==Month.DECEMBER) {
			dec -= t.getValue();
		}
		
		total = jan+feb+mar+apr+may+jun+jul+aug+sep+oct+nov+dec;
		avg = Math.round(total/12 * 100.0) / 100.0;	// 2 decimal places
	}
	private void addTransToValues(Transaction t, Month month) {
		if(month==Month.JANUARY) {
			jan += t.getValue();
		}else if(month==Month.FEBRUARY) {
			feb += t.getValue();
		}else if(month==Month.MARCH) {
			mar += t.getValue();
		}else if(month==Month.APRIL) {
			apr += t.getValue();
		}else if(month==Month.MAY) {
			may += t.getValue();
		}else if(month==Month.JUNE) {
			jun += t.getValue();
		}else if(month==Month.JULY) {
			jul += t.getValue();
		}else if(month==Month.AUGUST) {
			aug += t.getValue();
		}else if(month==Month.SEPTEMBER) {
			sep += t.getValue();
		}else if(month==Month.OCTOBER) {
			oct += t.getValue();
		}else if(month==Month.NOVEMBER) {
			nov += t.getValue();
		}else if(month==Month.DECEMBER) {
			dec += t.getValue();
		}
		
		total = jan+feb+mar+apr+may+jun+jul+aug+sep+oct+nov+dec;
		avg = Math.round(total/12 * 100.0) / 100.0;	// 2 decimal places
	}
	
	public double getJan() {
		return jan;
	}
	public void setJan(double jan) {
		this.jan = jan;
	}
	public double getFeb() {
		return feb;
	}
	public void setFeb(double feb) {
		this.feb = feb;
	}
	public double getMar() {
		return mar;
	}
	public void setMar(double mar) {
		this.mar = mar;
	}
	public double getApr() {
		return apr;
	}
	public void setApr(double apr) {
		this.apr = apr;
	}
	public double getMay() {
		return may;
	}
	public void setMay(double may) {
		this.may = may;
	}
	public double getJun() {
		return jun;
	}
	public void setJun(double jun) {
		this.jun = jun;
	}
	public double getJul() {
		return jul;
	}
	public void setJul(double jul) {
		this.jul = jul;
	}
	public double getAug() {
		return aug;
	}
	public void setAug(double aug) {
		this.aug = aug;
	}
	public double getSep() {
		return sep;
	}
	public void setSep(double sep) {
		this.sep = sep;
	}
	public double getOct() {
		return oct;
	}
	public void setOct(double oct) {
		this.oct = oct;
	}
	public double getNov() {
		return nov;
	}
	public void setNov(double nov) {
		this.nov = nov;
	}
	public double getDec() {
		return dec;
	}
	public void setDec(double dec) {
		this.dec = dec;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	
	public Double getCurrMonth() {
		Month month = LocalDate.now().getMonth();
		Double spent = 0.0;
		
		if(month==Month.JANUARY) {
			spent = this.getJan();
		}else if(month==Month.FEBRUARY) {
			spent = this.getFeb();
		}else if(month==Month.MARCH) {
			spent = this.getMar();
		}else if(month==Month.APRIL) {
			spent = this.getApr();
		}else if(month==Month.MAY) {
			spent = this.getMay();
		}else if(month==Month.JUNE) {
			spent = this.getJun();
		}else if(month==Month.JULY) {
			spent = this.getJul();
		}else if(month==Month.AUGUST) {
			spent = this.getAug();
		}else if(month==Month.SEPTEMBER) {
			spent = this.getSep();
		}else if(month==Month.OCTOBER) {
			spent = this.getOct();
		}else if(month==Month.NOVEMBER) {
			spent = this.getNov();
		}else if(month==Month.DECEMBER) {
			spent = this.getDec();
		}
		
		return spent;
	}
	
	public Double getMonth(int m) {
		Month month = Month.of(m);
		Double spent = 0.0;
		
		if(month==Month.JANUARY) {
			spent = this.getJan();
		}else if(month==Month.FEBRUARY) {
			spent = this.getFeb();
		}else if(month==Month.MARCH) {
			spent = this.getMar();
		}else if(month==Month.APRIL) {
			spent = this.getApr();
		}else if(month==Month.MAY) {
			spent = this.getMay();
		}else if(month==Month.JUNE) {
			spent = this.getJun();
		}else if(month==Month.JULY) {
			spent = this.getJul();
		}else if(month==Month.AUGUST) {
			spent = this.getAug();
		}else if(month==Month.SEPTEMBER) {
			spent = this.getSep();
		}else if(month==Month.OCTOBER) {
			spent = this.getOct();
		}else if(month==Month.NOVEMBER) {
			spent = this.getNov();
		}else if(month==Month.DECEMBER) {
			spent = this.getDec();
		}
		
		return spent;
	}

}
