package application.entities;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Payroll {
	private final IntegerProperty payrollID;
	private final IntegerProperty empID;
	private final IntegerProperty salaryID;
	private final IntegerProperty deductionID;
	private final ObjectProperty<LocalDate> payrollDate;
	private final DoubleProperty payrollAmount;
	private final IntegerProperty userID;
	
	public Payroll() {
		this(0,0,0,0,null,0.0,0);
	}
	
	public Payroll(int payrollID,int empID,int salaryID,int deductionID,LocalDate payrollDate,Double payrollAmount,int userID) {
		this.payrollID = new SimpleIntegerProperty(payrollID);
		this.empID = new SimpleIntegerProperty(empID);
		this.salaryID = new SimpleIntegerProperty(salaryID);
		this.deductionID = new SimpleIntegerProperty(deductionID);
		this.payrollDate = new SimpleObjectProperty<LocalDate>(payrollDate);
		this.payrollAmount = new SimpleDoubleProperty(payrollAmount);
		this.userID = new SimpleIntegerProperty(userID);
	}
	
	public void setPayrollID(int payrollID) {
		this.payrollID.set(payrollID);
	}
	
	public void setEmpID(int empID) {
		this.empID.set(empID);
	}
	
	public void setSalaryID(int salaryID) {
		this.salaryID.set(salaryID);
	}
	
	public void setDeductionID(int deductionID) {
		this.deductionID.set(deductionID);
	}
	
	public void setPayrollDate(LocalDate payrollDate) {
		this.payrollDate.set(payrollDate);
	}
	
	public void setPayrollAmount(Double payrollAmount) {
		this.payrollAmount.set(payrollAmount);
	}
	
	public void setUserID(int userID) {
		this.userID.set(userID);
	}
	
	public int getPayrollID() {
		return payrollID.get();
	}
	
	public IntegerProperty payrollIDProperty() {
		return payrollID;
	}
	
	public int getEmpID() {
		return empID.get();
	}
	
	public IntegerProperty empIDProperty() {
		return empID;
	}
	
	public int getSalaryID() {
		return salaryID.get();
	}
	
	public IntegerProperty salaryIDProperty() {
		return salaryID;
	}
	
	public int getDeductionID() {
		return deductionID.get();
	}
	
	public IntegerProperty deductionIDProperty() {
		return deductionID;
	}
	
	public LocalDate getPayrollDate() {
		return payrollDate.get();
	}
	
	public ObjectProperty<LocalDate> payrollDateProperty() {
		return payrollDate;
	}
	
	public Double getPayrollAmount() {
		return payrollAmount.get();
	}
	
	public DoubleProperty payrollAmountProperty() {
		return payrollAmount;
	}
	
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}
	
	
	@Override
	public String toString() {
		return "("+payrollID.get()+","+empID.get()+","+salaryID.get()+","+deductionID.get()+",'"+payrollDate.get()+"',"+payrollAmount.get()+","+userID.get()+")";
	}
}
