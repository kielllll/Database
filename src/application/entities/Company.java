package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company {
	private final IntegerProperty compID;
	private final StringProperty compName;
	private final StringProperty compLocation;
	private final StringProperty compContact;
	
	public Company() {
		this(0,null,null,null);
	}
	
	public Company(int compID,String compName,String compLocation,String compContact) {
		this.compID = new SimpleIntegerProperty(compID);
		this.compName = new SimpleStringProperty(compName);
		this.compLocation = new SimpleStringProperty(compLocation);
		this.compContact = new SimpleStringProperty(compContact);
	}
	
	public void setCompID(int compID) {
		this.compID.set(compID);
	}
	
	public void setCompName(String compName) {
		this.compName.set(compName);
	}
	
	public void setCompLocation(String compLocation) {
		this.compLocation.set(compLocation);
	}
	
	public void setCompContact(String compContact) {
		this.compContact.set(compContact);
	}
	
	public int getCompID() {
		return compID.get();
	}
	
	public IntegerProperty compIDProperty() {
		return compID;
	}
	
	public String getCompName() {
		return compName.get();
	}
	
	public StringProperty compNameProperty() {
		return compName;
	}
	
	public String getCompLocation() {
		return compLocation.get();
	}
	
	public StringProperty compLocation() {
		return compLocation;
	}
	
	public String getCompContact() {
		return compContact.get();
	}
	
	public StringProperty compContactProperty() {
		return compContact;
	}
	
	@Override
	public String toString() {
		return "("+compID.get()+",'"+compName.get()+"','"+compLocation.get()+"','"+compContact.get()+"')";
	}
}
