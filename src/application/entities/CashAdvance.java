package application.entities;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class CashAdvance {
	
	private final IntegerProperty caID;
	private final IntegerProperty lookupID;
	private final ObjectProperty<LocalDate> caDate;
	private final StringProperty caDescription;
	private final DoubleProperty caAmount;
	private final StringProperty caStatus;
	
	public CashAdvance() {
		this(0,0,null,null,0.0,null);
	}
	
	public CashAdvance(int caID,int lookupID,LocalDate caDate,String caDescription,Double caAmount,String caStatus) {
		this.caID = new SimpleIntegerProperty(caID);
		this.lookupID = new SimpleIntegerProperty(lookupID);
		this.caDate = new SimpleObjectProperty<LocalDate>(caDate);
		this.caDescription = new SimpleStringProperty(caDescription);
		this.caAmount = new SimpleDoubleProperty(caAmount);
		this.caStatus = new SimpleStringProperty(caStatus);
	}
	
	public void setCaID(int caID) {
		this.caID.set(caID);
	}
	
	public void setLookupID(int lookupID) {
		this.lookupID.set(lookupID);
	}
	
	public void setCaDate(LocalDate caDate) {
		this.caDate.set(caDate);
	}
	
	public void setCaDescription(String caDescription) {
		this.caDescription.set(caDescription);
	}
	
	public void setCaAmount(double caAmount) {
		this.caAmount.set(caAmount);
	}
	
	public void setCaStatus(String caStatus) {
		this.caStatus.set(caStatus);
	}
	
	public int getCaID() {
		return caID.get();
	}
	
	public IntegerProperty caIDProperty() {
		return caID;
	}
	
	public int getLookupID() {
		return lookupID.get();
	}
	
	public IntegerProperty lookupIDProperty() {
		return lookupID;
	}
	
	public LocalDate getCaDate() {
		return caDate.get();
	}
	
	public ObjectProperty<LocalDate> caDateProperty() {
		return caDate;
	}
	
	public String getCaDescription() {
		return caDescription.get();
	}
	
	public StringProperty caDescriptionProperty() {
		return caDescription;
	}
	
	public double getCaAmount() {
		return caAmount.get();
	}
	
	public DoubleProperty caAmountProperty() {
		return caAmount;
	}
	
	public String getCaStatus() {
		return caStatus.get();
	}
	
	public StringProperty caStatusProperty() {
		return caStatus;
	}
	
	@Override
	public String toString() {
		return "("+caID.get()+","+lookupID.get()+",'"+caDate.get()+"','"+caDescription.get()+"',"+caAmount.get()+",'"+caStatus.get()+"')";
	}
}
