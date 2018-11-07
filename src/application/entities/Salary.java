package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Salary {
	private final IntegerProperty salaryID;
	private final IntegerProperty empID;
	private final StringProperty salaryStatus;
	private final IntegerProperty numAbsc;
	
	public Salary() {
		this(0,0,null,0);
	}
	
	public Salary(int salaryID,int empID,String salaryStatus,int numAbsc) {
		this.salaryID = new SimpleIntegerProperty(salaryID);
		this.empID = new SimpleIntegerProperty(empID);
		this.salaryStatus = new SimpleStringProperty(salaryStatus);
		this.numAbsc = new SimpleIntegerProperty(numAbsc);
	}
	
	public void setSalaryID(int salaryID) {
		this.salaryID.set(salaryID);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setSalaryStatus(String salaryStatus) {
		this.salaryStatus.set(salaryStatus);
	}
	
	public void setNumAbsc(int numAbsc) {
		this.numAbsc.set(numAbsc);
	}
	
	public int getSalaryID() {
		return salaryID.get();
	}
	
	public IntegerProperty salaryIDProperty() {
		return salaryID;
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public String getSalaryStatus() {
		return salaryStatus.get();
	}
	
	public StringProperty salaryStatusProperty() {
		return salaryStatus;
	}
	
	public int getNumAbsc() {
		return numAbsc.get();
	}
	
	public IntegerProperty numAbscProperty() {
		return numAbsc;
	}
	
	@Override
	public String toString() {
		return "("+salaryID.get()+","+empID.get()+",'"+salaryStatus.get()+"',"+numAbsc.get()+")";
	}
}
