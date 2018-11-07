package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Deduction {
	private final IntegerProperty deductionID;
	private final IntegerProperty empID;
	private final StringProperty deductionStatus;
	
	public Deduction() {
		this(0,0,null);
	}
	
	public Deduction(int deductionID,int empID,String deductionStatus) {
		this.deductionID = new SimpleIntegerProperty(deductionID);
		this.empID = new SimpleIntegerProperty(empID);
		this.deductionStatus = new SimpleStringProperty(deductionStatus);
	}
	
	public void setDeductionID(int deductionID) {
		this.deductionID.set(deductionID);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setDeductionStatus(String deductionStatus) {
		this.deductionStatus.set(deductionStatus);
	}
	
	public int getDeductionID() {
		return deductionID.get();
	}
	
	public IntegerProperty deductionIDProperty() {
		return deductionID;
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public String getDeductionStatus() {
		return deductionStatus.get();
	}
	
	public StringProperty deductionStatusProperty() {
		return deductionStatus;
	}
	
	@Override
	public String toString() {
		return "("+deductionID.get()+","+empID.get()+",'"+deductionStatus.get()+"')";
	}
}
