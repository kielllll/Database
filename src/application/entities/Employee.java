package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Employee {
	private final IntegerProperty empID;
	private final IntegerProperty unitID;
	private final IntegerProperty compID;
	private final IntegerProperty etID;
	
	public Employee() {
		this(0,0,0,0);
	}
	
	public Employee(int empID,int unitID,int compID,int etID) {
		this.empID = new SimpleIntegerProperty(empID);
		this.unitID = new SimpleIntegerProperty(unitID);
		this.compID = new SimpleIntegerProperty(compID);
		this.etID = new SimpleIntegerProperty(etID);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setUnitID(int unitID) {
		this.unitID.set(unitID);
	}
	
	public void setCompID(int compID) {
		this.compID.set(compID);
	}
	
	public void setEtID(int etID) {
		this.etID.set(etID);
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public int getUnitID() {
		return unitID.get();
	}
	
	public IntegerProperty unitIDProperty() {
		return unitID;
	}
	
	public int getCompID() {
		return compID.get();
	}
	
	public IntegerProperty compIDProperty() {
		return compID;
	}
	
	public int getEtID() {
		return etID.get();
	}
	
	public IntegerProperty etIDProperty() {
		return etID;
	}
	
	@Override
	public String toString() {
		return "("+empID.get()+","+unitID.get()+","+compID.get()+","+etID.get()+")";
	}
}
