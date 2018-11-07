package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Unit {
	private final IntegerProperty unitID;
	private final StringProperty unitName;
	private final StringProperty unitStatus;
	
	public Unit() {
		this(0,null,null);
	}
	
	public Unit(int unitID,String unitName,String unitStatus) {
		this.unitID = new SimpleIntegerProperty(unitID);
		this.unitName = new SimpleStringProperty(unitName);
		this.unitStatus = new SimpleStringProperty(unitStatus);
	}

	public void setUnitID(int unitID) {
		this.unitID.set(unitID);
	}

	public void setUnitName(String unitName){
		this.unitName.set(unitName);
	}
	
	public void setUnitStatus(String unitStatus) {
		this.unitStatus.set(unitStatus);
	}
	
	public int getUnitID() {
		return unitID.get();
	}
	
	public IntegerProperty unitIDProperty() {
		return unitID;
	}
	
	public String getUnitName() {
		return unitName.get();
	}
	
	public StringProperty unitNameProperty() {
		return unitName;
	}
	
	public String getUnitStatus() {
		return unitStatus.get();
	}
	
	public StringProperty unitStatusProperty() {
		return unitStatus;
	}
	
	@Override
	public String toString() {
		return "("+unitID.get()+",'"+unitName.get()+"','"+unitStatus.get()+"')";
	}
}
