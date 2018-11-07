package application.user;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import application.dao.UserDAOImpl;
import application.entities.User;
import application.util.Dialogs;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UserController implements Initializable{
	@FXML JFXButton btnBack;
	@FXML JFXTextField txtUserID;
	@FXML JFXTextField txtFName;
	@FXML JFXTextField txtLName;
	@FXML JFXTextField txtUName;
	@FXML JFXTextField txtPWord;
	@FXML JFXTextField txtContact;
	@FXML JFXComboBox<String> cbStatus;
	@FXML JFXButton btnAdd;
	@FXML JFXButton btnUpdate;
	@FXML JFXButton btnClear;
	@FXML JFXTextField txtSearch;
	@FXML TableView<User> tblUser;
	@FXML TableColumn<User, Integer> colID;
	@FXML TableColumn<User, String> colFName;
	@FXML TableColumn<User, String> colLName;
	@FXML TableColumn<User, String> colUName;
	@FXML TableColumn<User, String> colContact;
	@FXML TableColumn<User, String> colStatus;
	
	private static ObservableList<User> userList = FXCollections.observableArrayList();
	private static String temp = "";
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		fillUser();
		btnUpdate.setDisable(true);
		
		//Bind the properties in the table
		colID.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
		colFName.setCellValueFactory(cellData -> cellData.getValue().userFNameProperty());
		colLName.setCellValueFactory(cellData -> cellData.getValue().userLNameProperty());
		colUName.setCellValueFactory(cellData -> cellData.getValue().userUNameProperty());
		colContact.setCellValueFactory(cellData -> cellData.getValue().userContactProperty());
		colStatus.setCellValueFactory(cellData -> cellData.getValue().userStatusProperty());
					
		//Add selections for the combo box
		cbStatus.getItems().addAll("Active","Not active");
					
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<User> filteredData = new FilteredList<>(userList, e -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			    filteredData.setPredicate(u -> {
		        // If filter text is empty, display all persons.
			    if (newValue == null || newValue.isEmpty()) {
			            return true;
			    }

			    // Compare first name and last name of every person with filter text.
			    String lowerCaseFilter = newValue.toLowerCase();

			    if (Integer.toString(u.getUserID()).toLowerCase().contains(lowerCaseFilter)) {
			            return true; // Filter matches first name.
			    } 
			    return false; // Does not match.
			    });
			            
			    if(!newValue.matches("[0-9]*")){
			         	txtSearch.setText(oldValue);
			    }
		});

		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<User> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(tblUser.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		tblUser.setItems(sortedData);
			        
		tblUser.setOnMouseClicked(e->{
			   if(Bindings.isNotEmpty(tblUser.getItems()).get()) {
			        User user = tblUser.getFocusModel().getFocusedItem();
			        		
			        txtUserID.setText(user.getUserID()+"");
			        txtFName.setText(user.getUserFName());
			        txtLName.setText(user.getUserLName());
			        txtUName.setText(user.getUserUName());
			        temp = user.getUserUName();
			        txtPWord.setText(user.getUserPWord());
			        txtContact.setText(user.getUserContact());
			        cbStatus.setValue(user.getUserStatus());
			        btnUpdate.setDisable(false);
			   }
		});
			        
		//Setting up listeners for the componenets
		btnBack.setOnAction(e->{
			Main.getStgUser().close();
			Main.showLogin();
		});
		
		txtSearch.setOnKeyReleased(e->search(txtSearch.getText()));
		
		btnAdd.setOnAction(e->{
			if(txtUserID.getText().equals("")) {
				if(checkFields()) {
					if(checkUsername()) {
						User u = new User(UserDAOImpl.getInstance().getAllUser().size()+1,txtFName.getText(),txtLName.getText(),txtUName.getText(),
								txtPWord.getText(),txtContact.getText(),cbStatus.getSelectionModel().getSelectedItem());
						
						UserDAOImpl.getInstance().insert(u);
						userList.add(u);
						clear();
						Dialogs.showMessage("Add User", "Added Successfully", Main.getStgUser());
					}else Dialogs.showErrorMessage("Add User Failed", "Username already exists", Main.getStgUser());
				}else Dialogs.showErrorMessage("Add User Failed", "Fill up required fields", Main.getStgUser());
			}else Dialogs.showErrorMessage("Add User Failed", "Click clear button and try again", Main.getStgUser());
		});
		
		btnUpdate.setOnAction(e->{
			if(checkFields()) {
				if(checkUsername()) {
					User u = new User(Integer.parseInt(txtUserID.getText()),txtFName.getText(),txtLName.getText(),txtUName.getText(),
							txtPWord.getText(),txtContact.getText(),cbStatus.getSelectionModel().getSelectedItem());
					
					String query = "UPDATE tbl_User SET userFName='"+u.getUserFName()+"', userLName='"+u.getUserLName()
								+"', userUName='"+u.getUserUName()+"', userPWord='"+u.getUserPWord()
								+"', userContact='"+u.getUserContact()+"', userStatus='"+u.getUserStatus()
								+"' WHERE userID="+u.getUserID();
					
					UserDAOImpl.getInstance().update(query);
					userList.parallelStream()
							.filter(user->user.getUserID()==u.getUserID())
							.forEach(user->{
								   user.setUserFName(u.getUserFName());
								   user.setUserLName(u.getUserLName());
								   user.setUserUName(u.getUserUName());
								   user.setUserPWord(u.getUserPWord());
								   user.setUserContact(u.getUserContact());
								   user.setUserStatus(u.getUserStatus());
							   });
					clear();
					Dialogs.showMessage("Update User", "Updated Successfully", Main.getStgUser());
				}else Dialogs.showErrorMessage("Update User Failed", "Username already exists", Main.getStgUser());
			}else Dialogs.showErrorMessage("Update User Failed", "Fill up required fields", Main.getStgUser());
		});
		
		btnClear.setOnAction(e->clear());
		
		txtContact.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtContact.setText(oldValue);
	            }
	            if(newValue.length()>11) {
	            	txtContact.setText(oldValue);
	            }
	    });
	}

	//USER DEFINED METHODS
	
	public void fillUser() {
		userList.clear();
		// To sort the list
		List<User> sortedList = UserDAOImpl.getInstance()
										   .getAllUser()
										   .parallelStream()
										   .sorted((o1,o2) -> (o1.getUserID()+"").compareTo(o2.getUserID()+"")).collect(Collectors.toList());
							
		// To add the sorted list in the table
		sortedList.stream()
				  .forEach(e->userList.add(e));
	}
	
	public void clear() {
		txtUserID.setText("");
		txtFName.setText("");
		txtLName.setText("");
		txtUName.setText("");
		txtPWord.setText("");
		txtContact.setText("");
		temp="";
		cbStatus.setValue(null);
		btnUpdate.setDisable(true);
	}
	
	public boolean checkFields() {
		return !((txtFName.getText().equals("")||txtFName.getText().startsWith(" "))
				||(txtLName.getText().equals("")||txtLName.getText().startsWith(" "))
				||(txtUName.getText().equals("")||txtLName.getText().startsWith(" "))
				||(txtPWord.getText().equals("")||txtPWord.getText().startsWith(" "))
				||(txtContact.getText().equals(""))
				||(((cbStatus.getSelectionModel().getSelectedItem()+"").equals("null"))||((cbStatus.getSelectionModel().getSelectedItem()+"").equals(""))))?true:false;
	}
	
	public boolean checkUsername() {
		boolean check = true;
		if(temp.equals("")) {
			check = UserDAOImpl.getInstance()
					   .getAllUser()
					   .parallelStream()
					   .filter(u->u.getUserUName().equals(txtUName.getText()))
					   .findFirst()
					   .isPresent();
		}else if(temp.equals(txtUName.getText())) {
			check = false;
		}
		return !check;
	}
	
	public void search(String str) {
		if(str.equals(""))
			clear();
		else {
			int id = Integer.parseInt(str);
			
			boolean check = UserDAOImpl.getInstance()
									   .getAllUser()
									   .parallelStream()
									   .filter(u->u.getUserID()==id)
									   .findFirst()
									   .isPresent();
			
			if(check) {
				User user = UserDAOImpl.getInstance()
									   .getAllUser()
									   .parallelStream()
									   .filter(u->u.getUserID()==id)
									   .findFirst()
									   .get();
				
				txtUserID.setText(user.getUserID()+"");
				txtFName.setText(user.getUserFName());
				txtLName.setText(user.getUserLName());
				txtUName.setText(user.getUserUName());
				temp = user.getUserUName();
				txtPWord.setText(user.getUserPWord());
				txtContact.setText(user.getUserContact());
				cbStatus.setValue(user.getUserStatus());
				btnUpdate.setDisable(false);
			}else clear();
		}
	}
}
