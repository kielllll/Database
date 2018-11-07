package application.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SSS {
	
	private final IntegerProperty sssID;
	private final IntegerProperty empID;
	private final DoubleProperty sssAmount;
	
	public SSS() {
		this(0,0,0.0);
	}
	
	public SSS(int sssID,int empID,Double sssAmount) {
		this.sssID = new SimpleIntegerProperty(sssID);
		this.empID = new SimpleIntegerProperty(empID);
		this.sssAmount = new SimpleDoubleProperty(sssAmount);
	}
	
	public void setSssID(int sssID) {
		this.sssID.set(sssID);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setSssAmout(double sssAmount) {
		this.sssAmount.set(sssAmount);
	}
	
	public int getSssID() {
		return sssID.get();
	}
	
	public IntegerProperty sssIDProperty() {
		return sssID;
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public double getSssAmount() {
		return sssAmount.get();
	}
	
	public DoubleProperty sssAmountProperty() {
		return sssAmount;
	}
	
	@Override
	public String toString() {
		return "("+sssID.get()+","+empID.get()+","+sssAmount.get()+")";
	}
}
