package application.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpType {
	private final IntegerProperty etID;
	private final IntegerProperty compID;
	private final StringProperty etName;
	private final StringProperty etDescription;
	private final DoubleProperty etRate;
	
	public EmpType() {
		this(0,0,null,null,0.0);
	}
	
	public EmpType(int etID,int compID,String etName,String etDescription,double etRate) {
		this.etID = new SimpleIntegerProperty(etID);
		this.compID = new SimpleIntegerProperty(compID);
		this.etName = new SimpleStringProperty(etName);
		this.etDescription = new SimpleStringProperty(etDescription);
		this.etRate = new SimpleDoubleProperty(etRate);
	}
	
	public void setEtID(int etID) {
		this.etID.set(etID);
	}
	
	public void setCompID(int compID) {
		this.compID.set(compID);
	}
	
	public void setEtName(String etName) {
		this.etName.set(etName);
	}
	
	public void setEtDescription(String etDescription) {
		this.etDescription.set(etDescription);
	}
	
	public void setEtRate(double etRate) {
		this.etRate.set(etRate);
	}
	
	public int getEtID() {
		return etID.get();
	}
	
	public IntegerProperty etIDProperty() {
		return etID;
	}

	public int getCompID() {
		return compID.get();
	}
	
	public IntegerProperty compIDProperty() {
		return compID;
	}
	
	public String getEtName() {
		return etName.get();
	}
	
	public StringProperty etNameProperty() {
		return etName;
	}
	
	public String getEtDescription() {
		return etDescription.get();
	}
	
	public StringProperty etDescriptionProperty() {
		return etDescription;
	}
	
	public double getEtRate() {
		return etRate.get();
	}
	
	public DoubleProperty etRateProperty() {
		return etRate;
	}

	@Override
	public String toString() {
		return "("+etID.get()+","+compID.get()+",'"+etName.get()+"','"+etDescription.get()+"',"+etRate.get()+")";
	}
}
