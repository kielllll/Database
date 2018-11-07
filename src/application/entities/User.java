package application.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private final IntegerProperty userID;
	private final StringProperty userFName;
	private final StringProperty userLName;
	private final StringProperty userUName;
	private final StringProperty userPWord;
	private final StringProperty userContact;
	private final StringProperty userStatus;
	
	public User() {
		this(0,null,null,null,null,null,null);
	}
	
	public User(int userID,String userFName,String userLName,String userUName,String userPWord,String userContact,String userStatus) {
		this.userID = new SimpleIntegerProperty(userID);
		this.userFName = new SimpleStringProperty(userFName);
		this.userLName = new SimpleStringProperty(userLName);
		this.userUName = new SimpleStringProperty(userUName);
		this.userPWord = new SimpleStringProperty(userPWord);
		this.userContact = new SimpleStringProperty(userContact);
		this.userStatus = new SimpleStringProperty(userStatus);
	}
	
	public void setUserID(int userID) {
		this.userID.set(userID);
	}
	
	public void setUserFName(String userFName) {
		this.userFName.set(userFName);
	}
	
	public void setUserLName(String userLName) {
		this.userLName.set(userLName);
	}
	
	public void setUserUName(String userUName) {
		this.userUName.set(userUName);
	}
	
	public void setUserPWord(String userPWord) {
		this.userPWord.set(userPWord);
	}
	
	public void setUserContact(String userContact) {
		this.userContact.set(userContact);
	}
	
	public void setUserStatus(String userStatus) {
		this.userStatus.set(userStatus);
	}
	
	public int getUserID() {
		return userID.get();
	}
	
	public IntegerProperty userIDProperty() {
		return userID;
	}
	
	public String getUserFName() {
		return userFName.get();
	}
	
	public StringProperty userFNameProperty() {
		return userFName;
	}
	
	public String getUserLName() {
		return userLName.get();
	}
	
	public StringProperty userLNameProperty() {
		return userLName;
	}
	
	public String getUserUName() {
		return userUName.get();
	}
	
	public StringProperty userUNameProperty() {
		return userUName;
	}
	
	public String getUserPWord() {
		return userPWord.get();
	}
	
	public StringProperty userPWordProperty() {
		return userPWord;
	}
	
	public String getUserContact() {
		return userContact.get();
	}
	
	public StringProperty userContactProperty() {
		return userContact;
	}
	
	public String getUserStatus() {
		return userStatus.get();
	}
	
	public StringProperty userStatusProperty() {
		return userStatus;
	}
	
	@Override
	public String toString() {
		return "("+userID.get()+",'"+userFName.get()+"','"+userLName.get()+"','"+userUName.get()+"','"+userPWord.get()+"','"+userContact.get()+"','"+userStatus.get()+"')";
	}
}
