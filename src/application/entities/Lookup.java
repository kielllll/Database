package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Lookup {
	private final IntegerProperty lookupID;
	private final IntegerProperty deductionID;
	
	public Lookup() {
		this(0,0);
	}
	
	public Lookup(int lookupID,int deductionID) {
		this.lookupID = new SimpleIntegerProperty(lookupID);
		this.deductionID = new SimpleIntegerProperty(deductionID);
	}
	
	public void setLookupID(int lookupID) {
		this.lookupID.set(lookupID);
	}
	
	public void setDeductionID(int deductionID) {
		this.deductionID.set(deductionID);
	}
	
	public int getLookupID() {
		return lookupID.get();
	}
	
	public IntegerProperty lookupIDProperty() {
		return lookupID;
	}
	
	public int getDeductionID() {
		return deductionID.get();
	}
	
	public IntegerProperty deductionIDProperty() {
		return deductionID;
	}
	
	@Override
	public String toString() {
		return "("+lookupID.get()+","+deductionID.get()+")";
	}
}
