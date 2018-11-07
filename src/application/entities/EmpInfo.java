package application.entities;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpInfo {
	private final IntegerProperty empID;
	private final StringProperty empFName;
	private final StringProperty empLName;
	private final StringProperty empContact;
	private final ObjectProperty<LocalDate> empBDate;
	private final StringProperty empGender;
	private final StringProperty empStatus;
	
	public EmpInfo() {
		this(0,null,null,null,null,null,null);
	}
	
	public EmpInfo(int empID,String empFName,String empLName,String empContact,LocalDate empBDate,String empGender,String empStatus) {
		this.empID = new SimpleIntegerProperty(empID);
		this.empFName = new SimpleStringProperty(empFName);
		this.empLName = new SimpleStringProperty(empLName);
		this.empContact = new SimpleStringProperty(empContact);
		this.empBDate = new SimpleObjectProperty<LocalDate>(empBDate);
		this.empGender = new SimpleStringProperty(empGender);
		this.empStatus = new SimpleStringProperty(empStatus);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setEmpFName(String empFName) {
		this.empFName.set(empFName);
	}
	
	public void setEmpLName(String empLName) {
		this.empLName.set(empLName);
	}
	
	public void setEmpContact(String empContact) {
		this.empContact.set(empContact);
	}
	
	public void setEmpBDate(LocalDate empBDate) {
		this.empBDate.set(empBDate);
	}
	
	public void setEmpGender(String empGender) {
		this.empGender.set(empGender);
	}
	
	public void setEmpStatus(String empStatus) {
		this.empStatus.set(empStatus);
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public String getEmpFName() {
		return empFName.get();
	}
	
	public StringProperty empFNameProperty() {
		return empFName;
	}
	
	public String getEmpLName() {
		return empLName.get();
	}
	
	public StringProperty empLNameProperty() {
		return empLName;
	}
	
	public String getEmpContact() {
		return empContact.get();
	}
	
	public StringProperty empContactProperty() {
		return empContact;
	}
	
	public LocalDate getEmpBDate() {
		return empBDate.get();
	}
	
	public ObjectProperty<LocalDate> empBDateProperty() {
		return empBDate;
	}
	
	public String getEmpGender() {
		return empGender.get();
	}
	
	public StringProperty empGenderProperty() {
		return empGender;
	}
	
	public String getEmpStatus() {
		return empStatus.get();
	}
	
	public StringProperty empStatusProperty() {
		return empStatus;
	}
	
	@Override
	public String toString() {
		return "("+empID.get()+",'"+empFName.get()+"','"+empLName.get()+"','"+empContact.get()+"','"+empBDate.get()+"','"+empGender.get()+"','"+empStatus.get()+"')";
	}
}
