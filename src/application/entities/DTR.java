package application.entities;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DTR {
	private final IntegerProperty dtrID;
	private final IntegerProperty salaryID;
	private final ObjectProperty<LocalDate> dtrDate;
	private final IntegerProperty dtrHour;
	
	public DTR() {
		this(0,0,null,0);
	}
	
	public DTR(int dtrID,int salaryID,LocalDate dtrDate,int dtrHour) {
		this.dtrID = new SimpleIntegerProperty(dtrID);
		this.salaryID = new SimpleIntegerProperty(salaryID);
		this.dtrDate = new SimpleObjectProperty<LocalDate>(dtrDate);
		this.dtrHour = new SimpleIntegerProperty(dtrHour);
	}
	
	public void setDtrID(int dtrID) {
		this.dtrID.set(dtrID);
	}
	
	public void setSalaryID(int salaryID) {
		this.salaryID.set(salaryID);
	}
	
	public void setDtrDate(LocalDate dtrDate) {
		this.dtrDate.set(dtrDate);
	}
	
	public void setDtrHour(int dtrHour) {
		this.dtrHour.set(dtrHour);
	}
	
	public int getDtrID() {
		return dtrID.get();
	}
	
	public IntegerProperty dtrIDProperty() {
		return dtrID;
	}
	
	public int getSalaryID() {
		return salaryID.get();
	}
	
	public IntegerProperty salaryIDProperty() {
		return salaryID;
	}
	
	public LocalDate getDtrDate() {
		return dtrDate.get();
	}
	
	public ObjectProperty<LocalDate> dtrDateProperty() {
		return dtrDate;
	}
	
	public int getDtrHour() {
		return dtrHour.get();
	}
	
	public IntegerProperty dtrHourProperty() {
		return dtrHour;
	}
	
	@Override
	public String toString() {
		return "("+dtrID.get()+","+salaryID.get()+",'"+dtrDate.get()+"',"+dtrHour.get()+")";
	}
}
