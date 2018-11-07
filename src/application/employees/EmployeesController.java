package application.employees;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.ControlledScreen;
import application.Main;
import application.ScreensController;
import application.dao.EmpInfoDAOImpl;
import application.dao.EmployeeDAOImpl;
import application.entities.EmpInfo;
import application.entities.Employee;
import application.util.Dialogs;
import application.util.ComboBoxUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class EmployeesController implements Initializable, ControlledScreen {
	
	@FXML JFXButton btnRefresh;
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXTextField txtSearch;
	@FXML TableView<EmpInfo> table;
	@FXML TableColumn<EmpInfo, Integer> colEmpID;
	@FXML TableColumn<EmpInfo, String> colFName;
	@FXML TableColumn<EmpInfo, String> colLName;
	@FXML TableColumn<EmpInfo, String> colContact;
	@FXML TableColumn<EmpInfo, LocalDate> colBDate;
	@FXML TableColumn<EmpInfo, String> colGender;
	@FXML TableColumn<EmpInfo, String> colStatus;
	@FXML JFXTextField txtAddFName;
	@FXML JFXTextField txtAddLName;
	@FXML JFXTextField txtAddContact;
	@FXML JFXDatePicker dpAddBDate;
	@FXML JFXComboBox<String> cbAddUnitID;
	@FXML JFXComboBox<String> cbAddGender;
	@FXML JFXComboBox<String> cbAddCompID;
	@FXML JFXComboBox<String> cbAddEmpTypeID;
	@FXML JFXButton btnAdd;
	@FXML JFXButton btnClearAddFields;
	@FXML JFXTextField txtEmpID;
	@FXML JFXTextField txtUpdateFName;
	@FXML JFXTextField txtUpdateLName;
	@FXML JFXTextField txtUpdateContact;
	@FXML JFXDatePicker dpUpdateBDate;
	@FXML JFXComboBox<String> cbUpdateUnitID;
	@FXML JFXComboBox<String> cbUpdateGender;
	@FXML JFXComboBox<String> cbUpdateCompID;
	@FXML JFXComboBox<String> cbUpdateEmpTypeID;
	@FXML JFXComboBox<String> cbUpdateStatus;
	@FXML JFXButton btnUpdate;
	@FXML JFXButton btnClearUpdateFields;
	
	private ScreensController screenPage;
	private ObservableList<EmpInfo> empInfoList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			txtEmpID.setDisable(true);
			
			for(Node node : box.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
						case "Payroll"	: screenPage.setScreen(Main.getPayrollID());
										  break;
						case "Companies": screenPage.setScreen(Main.getCompaniesID());
						  				  break;
						case "Units"	: screenPage.setScreen(Main.getUnitsID());
		  				  				  break;
						case "Salary"	: screenPage.setScreen(Main.getSalaryID());
		  				  				  break;
						case "Deduction": screenPage.setScreen(Main.getDeductionID());
		  				  				  break;
						case "Logout"	: if(Dialogs.showConfirmMessage("Logout", "Confirm logout?",Main.getStgMenu())) {
											  Main.getStgMenu().close();
											  Main.showLogin();
										  }
										  break;
					}
				});
			}
			
			HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
			burgerTask2.setRate(-1);
			hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e ->{
				burgerTask2.setRate(burgerTask2.getRate()*-1);
				burgerTask2.play();
				
				if(drawer.isShown())
					drawer.close();
				else
					drawer.open();
			});
			
			empInfoList.clear();
			
			//sort the data from the database
			List<EmpInfo> sortedList = EmpInfoDAOImpl.getInstance()
												 .getAllEmpInfo()
												 .parallelStream()
												 .sorted((o1,o2) -> (o1.getEmpID()+"").compareTo(o2.getEmpID()+""))
												 .collect(Collectors.toList());
			
			//to add the sorted data in the employee table
			sortedList.stream()
					  .forEach(e -> empInfoList.add(e));
			
			colEmpID.setCellValueFactory(cellData -> cellData.getValue().empIDProperty().asObject());
			colFName.setCellValueFactory(cellData -> cellData.getValue().empFNameProperty());
			colLName.setCellValueFactory(cellData -> cellData.getValue().empLNameProperty());
			colContact.setCellValueFactory(cellData -> cellData.getValue().empContactProperty());
			colBDate.setCellValueFactory(cellData -> cellData.getValue().empBDateProperty());
			colGender.setCellValueFactory(cellData -> cellData.getValue().empGenderProperty());
			colStatus.setCellValueFactory(cellDate -> cellDate.getValue().empStatusProperty());
			
			//Set choices for the combo box
			cbAddGender.getItems().add("Male");
			cbAddGender.getItems().add("Female");
			refresh();
			
			cbUpdateGender.getItems().add("Male");
			cbUpdateGender.getItems().add("Female");
			cbUpdateStatus.getItems().add("Active");
			cbUpdateStatus.getItems().add("Inactive");
			
			cbAddUnitID.setEditable(true);
			cbUpdateUnitID.setEditable(true);
			cbAddEmpTypeID.setEditable(true);
			cbUpdateEmpTypeID.setEditable(true);
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<EmpInfo> filteredData = new FilteredList<>(empInfoList, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(emp -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(emp.getEmpID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearch.setText(oldValue);
	            }
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<EmpInfo> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(table.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        table.setItems(sortedData);
			
	        // SETTING UP LISTENERS FOR THE COMPONENTS
	        table.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(table.getItems()).get()) {
		        	EmpInfo emp = table.getFocusModel().getFocusedItem();
		        	
		        	txtEmpID.setText(emp.getEmpID()+"");
		        	txtUpdateFName.setText(emp.getEmpFName());
		        	txtUpdateLName.setText(emp.getEmpLName());
		        	txtUpdateContact.setText(emp.getEmpContact());
		        	dpUpdateBDate.setValue(emp.getEmpBDate());
		        	cbUpdateGender.setValue(emp.getEmpGender());
		        	cbUpdateStatus.setValue(emp.getEmpStatus());
		        	
		        	Employee empl = EmployeeDAOImpl.getInstance()
	        				   					   .getAllEmployee()
	        				   					   .parallelStream()
	        				   					   .filter(em->em.getEmpID()==emp.getEmpID())
	        				   					   .findFirst().get();
		        	cbUpdateUnitID.setValue(empl.getUnitID()+"");
					cbUpdateCompID.setValue(empl.getCompID()+"");
					cbUpdateEmpTypeID.setItems(ComboBoxUtil.fillCBEmpTypeID(empl.getCompID()));
					cbUpdateEmpTypeID.setValue(empl.getEtID()+"");
		        	
	        	}
	        });
	       
	        txtSearch.setOnKeyReleased(e->search(txtSearch.getText()));
	       
			btnAdd.setOnAction(e->{
				boolean check = !((txtAddFName.getText().equals("")||txtAddFName.getText().startsWith(" "))
						||(txtAddLName.getText().equals("")||txtAddLName.getText().startsWith(" "))
						||(txtAddContact.getText().equals("")||txtAddContact.getText().startsWith(" "))
						||dpAddBDate.getValue().equals(null)
						||(cbAddUnitID.getValue().equals(null)||cbAddUnitID.getValue().length()==0)
						||(cbAddGender.getValue().equals(null)||cbAddGender.getValue().length()==0)
						||(cbAddCompID.getValue().equals(null)||cbAddCompID.getValue().length()==0)
						||(cbAddEmpTypeID.getValue().equals(null)||cbAddEmpTypeID.getValue().equals("")))?true:false;
				
				if(check) {
					EmpInfo empinfo = new EmpInfo(empInfoList.size()+1,txtAddFName.getText(),txtAddLName.getText(),
							txtAddContact.getText(),dpAddBDate.getValue(),cbAddGender.getValue()+"","Active");
					
					Employee emp = new Employee(empinfo.getEmpID(),Integer.parseInt(cbAddUnitID.getValue()+"")
							,Integer.parseInt(cbAddCompID.getValue()+""),Integer.parseInt(cbAddEmpTypeID.getValue()+""));
					
					empInfoList.add(empinfo);
					EmpInfoDAOImpl.getInstance().insert(empinfo);
					EmployeeDAOImpl.getInstance().insert(emp);
					clearAddFields();
					refresh();
					Dialogs.showMessage("Add Employee", "Added successfully",Main.getStgMenu());
				}
				else Dialogs.showErrorMessage("Add Employee Failed", "Fill up required fields",Main.getStgMenu());
			});
			
			btnUpdate.setOnAction(e->{
				boolean check = !((txtUpdateFName.getText().equals("")||txtUpdateFName.getText().startsWith(" "))
						||(txtUpdateLName.getText().equals("")||txtUpdateLName.getText().startsWith(" "))
						||(txtUpdateContact.getText().equals("")||txtUpdateContact.getText().startsWith(" "))
						||dpUpdateBDate.getValue().equals(null)
						||(cbUpdateUnitID.getValue().equals(null)||cbUpdateUnitID.getValue().length()==0)
						||(cbUpdateGender.getValue().equals(null)||cbUpdateGender.getValue().length()==0)
						||(cbUpdateCompID.getValue().equals(null)||cbUpdateCompID.getValue().length()==0)
						||(cbUpdateEmpTypeID.getValue().equals(null)||cbUpdateEmpTypeID.getValue().length()==0))?true:false;
				
				if(check) {
					EmpInfo empinfo = new EmpInfo(Integer.parseInt(txtEmpID.getText()),txtUpdateFName.getText(),txtUpdateLName.getText(),
							txtUpdateContact.getText(),dpUpdateBDate.getValue(),cbUpdateGender.getValue()+"",cbUpdateStatus.getValue()+"");
					
					Employee emp = new Employee(empinfo.getEmpID(),Integer.parseInt(cbUpdateUnitID.getValue()+"")
							,Integer.parseInt(cbUpdateCompID.getValue()+""),Integer.parseInt(cbUpdateEmpTypeID.getValue()+""));
					
					String queryEmpInfo = "UPDATE tbl_EmpInfo SET empFName='"+empinfo.getEmpFName()+"', empLName='"+empinfo.getEmpLName()
											+"', empContact='"+empinfo.getEmpContact()+"', empBDate='"+empinfo.getEmpBDate()
											+"', empGender='"+empinfo.getEmpGender()+"', empStatus='"+empinfo.getEmpStatus()+"' WHERE empID="+empinfo.getEmpID();
					
					String queryEmp ="UPDATE tbl_Employee SET unitID="+emp.getUnitID()+", compID="+emp.getCompID()
											+", etID="+emp.getEtID()+" WHERE empID="+emp.getEmpID();
					
					empInfoList.parallelStream()
							   .filter(ei -> ei.getEmpID()==empinfo.getEmpID())
							   .forEach(ei -> {
								   ei.setEmpFName(empinfo.getEmpFName());
								   ei.setEmpLName(empinfo.getEmpLName());
								   ei.setEmpContact(empinfo.getEmpContact());
								   ei.setEmpBDate(empinfo.getEmpBDate());
								   ei.setEmpGender(empinfo.getEmpGender());
								   ei.setEmpStatus(empinfo.getEmpStatus());
							   });
					
					EmpInfoDAOImpl.getInstance().update(queryEmpInfo);
					EmployeeDAOImpl.getInstance().update(queryEmp);
					clearUpdateFields();
					Dialogs.showMessage("Update Employee", "Updated successfully",Main.getStgMenu());
				}
				else Dialogs.showErrorMessage("Update Employee Failed", "Fill up required fields",Main.getStgMenu());
			});
			
			btnClearAddFields.setOnAction(e->clearAddFields());
			
			btnClearUpdateFields.setOnAction(e->clearUpdateFields());
			
			txtAddFName.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[A-Za-z ]*")){
	            	txtAddFName.setText(oldValue);
	            }
	            if(newValue.length()>50) {
	            	txtAddFName.setText(oldValue);
	            }
	        });
			
			txtAddLName.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[A-Za-z ]*")){
	            	txtAddLName.setText(oldValue);
	            }
	            if(newValue.length()>50) {
	            	txtAddLName.setText(oldValue);
	            }
	        });
			
			txtUpdateFName.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[A-Za-z ]*")){
	            	txtUpdateFName.setText(oldValue);
	            }
	            if(newValue.length()>50) {
	            	txtUpdateFName.setText(oldValue);
	            }
	        });
			
			txtUpdateLName.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[A-Za-z ]*")){
	            	txtUpdateLName.setText(oldValue);
	            }
	            if(newValue.length()>50) {
	            	txtUpdateLName.setText(oldValue);
	            }
	        });
			
			txtAddContact.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtAddContact.setText(oldValue);
	            }
	            if(newValue.length()>11) {
	            	txtAddContact.setText(oldValue);
	            }
	        });
			
			txtUpdateContact.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtUpdateContact.setText(oldValue);
	            }
	            if(newValue.length()>11) {
	            	txtUpdateContact.setText(oldValue);
	            }
	        });
			
			btnRefresh.setOnAction(e->refresh());
			
			cbAddCompID.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
				if(!(cbAddCompID.getValue()+"").equals("null"))
					cbAddEmpTypeID.setItems(ComboBoxUtil.fillCBEmpTypeID(Integer.parseInt(newValue)));
		    }); 
			
			cbUpdateCompID.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
				if(!(cbUpdateCompID.getValue()+"").equals("null"))
					cbUpdateEmpTypeID.setItems(ComboBoxUtil.fillCBEmpTypeID(Integer.parseInt(newValue)));
		    }); 
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}
	
	public void clearAddFields() {
		txtAddFName.setText("");
		txtAddLName.setText("");
		txtAddContact.setText("");
		cbAddUnitID.setValue(null);
		cbAddCompID.setValue(null);
		dpAddBDate.setValue(null);
		cbAddGender.setValue(null);
		cbAddEmpTypeID.getItems().clear();
		cbAddEmpTypeID.setValue(null);
	}
	
	public void clearUpdateFields() {
		txtEmpID.setText("");
		txtUpdateFName.setText("");
		txtUpdateLName.setText("");
		txtUpdateContact.setText("");
		cbUpdateUnitID.setValue(null);
		cbUpdateCompID.setValue(null);
		dpUpdateBDate.setValue(null);
		cbUpdateGender.setValue(null);
		cbUpdateStatus.setValue(null);
		cbUpdateEmpTypeID.getItems().clear();
		cbUpdateEmpTypeID.setValue(null);
	}
	
	public void refresh() {
		cbAddUnitID.setItems(ComboBoxUtil.fillCBUnitID());
		cbAddCompID.setItems(ComboBoxUtil.fillCBCompID());
		
		cbUpdateUnitID.setItems(ComboBoxUtil.fillCBUnitID());
		cbUpdateCompID.setItems(ComboBoxUtil.fillCBCompID());
	}
	
	public void search(String id) {
		int length = 0;
		for(EmpInfo e : EmpInfoDAOImpl.getInstance().getAllEmpInfo()) {
			if(id.equals(""))
				clearUpdateFields();
			else if((e.getEmpID()+"").contains(id)) {
				txtEmpID.setText(e.getEmpID()+"");
				txtUpdateFName.setText(e.getEmpFName());
				txtUpdateLName.setText(e.getEmpLName());
				txtUpdateContact.setText(e.getEmpContact());
				dpUpdateBDate.setValue(e.getEmpBDate());
				cbUpdateStatus.setValue(e.getEmpStatus()+"");
				Employee empl = EmployeeDAOImpl.getInstance()
	   					   .getAllEmployee()
	   					   .parallelStream()
	   					   .filter(em->em.getEmpID()==e.getEmpID())
	   					   .findFirst().get();
				cbUpdateUnitID.setValue(empl.getUnitID()+"");
				cbUpdateCompID.setValue(empl.getCompID()+"");
				cbUpdateEmpTypeID.setItems(ComboBoxUtil.fillCBEmpTypeID(empl.getCompID()));
				cbUpdateEmpTypeID.setValue(empl.getEtID()+"");
				
				length = (e.getEmpID()+"").length();
				break;
			}
		}
		
		if(length<id.length())
			clearUpdateFields();
	}
}