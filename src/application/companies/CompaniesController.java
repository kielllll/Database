package application.companies;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.ControlledScreen;
import application.Main;
import application.ScreensController;
import application.dao.CompanyDAOImpl;
import application.dao.EmpTypeDAOImpl;
import application.entities.Company;
import application.entities.EmpType;
import application.util.Dialogs;
import application.util.ComboBoxAutoComplete;
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

public class CompaniesController implements Initializable, ControlledScreen{
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXTextField txtSearchCompID;
	@FXML TableView<Company> tblComp;
	@FXML TableColumn<Company, Integer> colCompID;
	@FXML TableColumn<Company, String> colCompName;
	@FXML TableColumn<Company, String> colCompLocation;
	@FXML TableColumn<Company, String> colCompContact;
	@FXML JFXTextField txtSearchEmpTypeID;
	@FXML TableView<EmpType> tblEmpType;
	@FXML TableColumn<EmpType, Integer> colEtID;
	@FXML TableColumn<EmpType, Integer> colCompID2;
	@FXML TableColumn<EmpType, String> colEtName;
	@FXML TableColumn<EmpType, String> colEtDesc;
	@FXML TableColumn<EmpType, Double> colEtRate;
	@FXML JFXTextField txtCompID;
	@FXML JFXTextField txtCompName;
	@FXML JFXTextField txtCompContact;
	@FXML JFXTextField txtCompLocation;
	@FXML JFXButton btnAddComp;
	@FXML JFXButton btnUpdateComp;
	@FXML JFXButton btnClearComp;
	@FXML JFXTextField txtEmpTypeID;
	@FXML JFXTextField txtEmpType;
	@FXML JFXComboBox<String> cbCompID;
	@FXML JFXTextField txtRate;
	@FXML JFXTextField txtDesc;
	@FXML JFXButton btnAddEmpType;
	@FXML JFXButton btnUpdateEmpType;
	@FXML JFXButton btnClearEmpType;
	
	private ScreensController screenPage;	
	private ObservableList<Company> compList = FXCollections.observableArrayList();
	private ObservableList<EmpType> empList = FXCollections.observableArrayList();
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			btnUpdateComp.setDisable(true);
			btnUpdateEmpType.setDisable(true);
			
			for(Node node : box.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
					case "Payroll"	: screenPage.setScreen(Main.getPayrollID());
					  				  break;
					case "Employees": screenPage.setScreen(Main.getEmployeesID());
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
			
			compList.clear();
			
			//Sort the data from database (tbl_Comp)
			List<Company> sortedCompList = CompanyDAOImpl.getInstance()
										.getAllCompany()
										.parallelStream()
										.sorted((s1,s2) -> (s1.getCompID()+"").compareTo(s2.getCompID()+""))
										.collect(Collectors.toList());
			
			//Add the data into the table (tbl_Comp)
			sortedCompList.stream()
					  .forEach(e-> compList.add(e));
			
			//Binding the data into the column (tbl_Comp)
			colCompID.setCellValueFactory(cellData -> cellData.getValue().compIDProperty().asObject());
			colCompName.setCellValueFactory(cellData -> cellData.getValue().compNameProperty());
			colCompLocation.setCellValueFactory(cellData -> cellData.getValue().compLocation());
			colCompContact.setCellValueFactory(cellData -> cellData.getValue().compContactProperty());
			
			//Binding the data into the column (tbl_EmpType)
			colEtID.setCellValueFactory(cellData -> cellData.getValue().etIDProperty().asObject());
			colCompID2.setCellValueFactory(cellData -> cellData.getValue().compIDProperty().asObject());
			colEtName.setCellValueFactory(cellData -> cellData.getValue().etNameProperty());
			colEtDesc.setCellValueFactory(cellData -> cellData.getValue().etDescriptionProperty());
			colEtRate.setCellValueFactory(cellData -> cellData.getValue().etRateProperty().asObject());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Company> filteredData = new FilteredList<>(compList, e -> true);
	        FilteredList<EmpType> filteredData2 = new FilteredList<>(empList, e -> true);
	        
	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearchCompID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(comp -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(comp.getCompID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	                txtSearchCompID.setText(oldValue);
	            }
	        });
	        
	        txtSearchEmpTypeID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData2.setPredicate(et -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(et.getEtID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	                txtSearchEmpTypeID.setText(oldValue);
	            }
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Company> sortedData = new SortedList<>(filteredData);
	        SortedList<EmpType> sortedData2 = new SortedList<>(filteredData2);
	        
	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblComp.comparatorProperty());
	        sortedData2.comparatorProperty().bind(tblEmpType.comparatorProperty());
	        
	        // 5. Add sorted (and filtered) data to the table.
	        tblComp.setItems(sortedData);
	        tblEmpType.setItems(sortedData2);
	        
	        //add data to the combo box
	        cbCompID.setItems(ComboBoxUtil.fillCBCompID());
	        
	        new ComboBoxAutoComplete<String>(cbCompID);
	        //Setting up Listeners/Events for the components
	        tblComp.setOnMouseClicked(c->{
	        	if(Bindings.isNotEmpty(tblComp.getItems()).get()) {
	        		Company comp = tblComp.getFocusModel().getFocusedItem();
	        		txtCompID.setText(comp.getCompID()+"");
	        		txtCompName.setText(comp.getCompName());
	        		txtCompContact.setText(comp.getCompContact());
	        		txtCompLocation.setText(comp.getCompLocation());
	        		btnUpdateComp.setDisable(false);
	        		
	        		empList.clear();
	        		
	        		//Sort the data from database (tbl_EmpType)
	    			List<EmpType> sortedEmpList = EmpTypeDAOImpl.getInstance()
	    										.getAllEmpType()
	    										.parallelStream()
	    				    					.filter(e->e.getCompID()==comp.getCompID())
	    										.sorted((s1,s2) -> (s1.getEtID()+"").compareTo(s2.getEtID()+""))
	    										.collect(Collectors.toList());
	    			
	    			//Add the data into the table (tbl_EmpTyp)
	    			sortedEmpList.stream().forEach(e-> empList.add(e));
	        	}
	        });
	        
	        tblEmpType.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(tblEmpType.getItems()).get()) {
	        		EmpType emp = tblEmpType.getFocusModel().getFocusedItem();
	        		txtEmpTypeID.setText(emp.getEtID()+"");
	        		cbCompID.setValue(emp.getCompID()+"");
	        		txtEmpType.setText(emp.getEtName());
	        		txtDesc.setText(emp.getEtDescription());
	        		txtRate.setText(emp.getEtRate()+"");
	        		btnUpdateEmpType.setDisable(false);
	        	}
	        });
	        
	        txtSearchCompID.setOnKeyReleased(e->searchCompID(txtSearchCompID.getText()));
	        txtSearchEmpTypeID.setOnKeyReleased(e->searchEmpTypeID(txtSearchEmpTypeID.getText()));
	        
	        btnAddComp.setOnAction(e->{
	        	if(!txtCompID.getText().equals(""))
	        		Dialogs.showErrorMessage("Add Company Failed", "Click clear button and try again",Main.getStgMenu());
	        	else {
	        		if(checkCompFields()) {
	        			Company comp = new Company(compList.size()+1,txtCompName.getText(),txtCompLocation.getText(),txtCompContact.getText());
	        			
	        			compList.add(comp);
	        			CompanyDAOImpl.getInstance().insert(comp);
	        			cbCompID.getItems().add(comp.getCompID()+"");
	        			clearCompFields();
	        			Dialogs.showMessage("Add Company", "Added successfully",Main.getStgMenu());
	        		}else 
	        			Dialogs.showErrorMessage("Add Company Failed", "Fill up required fields",Main.getStgMenu());
	        	}
	        });
	        
	        btnUpdateComp.setOnAction(e->{
	        	if(checkCompFields()) {
        			Company comp = new Company(Integer.parseInt(txtCompID.getText()),txtCompName.getText(),txtCompLocation.getText(),txtCompContact.getText());
        			
        			String query = "UPDATE tbl_Company SET compName='"+comp.getCompName()
        							+"',compLocation='"+comp.getCompLocation()+"',compContact='"+comp.getCompContact()
        							+"' WHERE compID="+comp.getCompID();
        			CompanyDAOImpl.getInstance().update(query);
        			
        			compList.parallelStream()
        					.filter(c->c.getCompID()==comp.getCompID())
        					.forEach(c->{
        						c.setCompName(comp.getCompName());
        						c.setCompLocation(comp.getCompLocation());
        						c.setCompContact(comp.getCompContact());
        					});
        			clearCompFields();
        			Dialogs.showMessage("Update Company", "Updated successfully",Main.getStgMenu());
        		}else 
        			Dialogs.showErrorMessage("Update Company Failed", "Fill up required fields",Main.getStgMenu());
	        });
	        
	        btnClearComp.setOnAction(e->clearCompFields());
	        
	        btnAddEmpType.setOnAction(e->{
	        	if(!txtEmpTypeID.getText().equals(""))
	        		Dialogs.showErrorMessage("Add Employee Type Failed", "Click clear button and try again",Main.getStgMenu());
	        	else {
		        	if(checkEmpFields()) {
			        		EmpType emp = new EmpType(EmpTypeDAOImpl.getInstance().getAllEmpType().size()+1,
			        				Integer.parseInt(cbCompID.getValue()+""),txtEmpType.getText(),txtDesc.getText(),
			        				Double.parseDouble(txtRate.getText()));
			        		
			        		if(Bindings.isNotEmpty(tblEmpType.getItems()).get()) {
			        			if(emp.getCompID()==tblEmpType.getItems().get(0).getCompID()) {
			        				empList.add(emp);
			        				EmpTypeDAOImpl.getInstance().insert(emp);
			        			}else EmpTypeDAOImpl.getInstance().insert(emp);
			        		}else EmpTypeDAOImpl.getInstance().insert(emp);
			        		
			        		clearEmpFields();
			        		Dialogs.showMessage("Add Employee Type", "Added Successfully",Main.getStgMenu());
		        	}else Dialogs.showErrorMessage("Add Employee Type Failed", "Fill up required fields",Main.getStgMenu());
	        	}
	        });
	        
	        btnUpdateEmpType.setOnAction(e->{
	        	if(checkEmpFields()) {
	        			EmpType emp = new EmpType(Integer.parseInt(txtEmpTypeID.getText()),
		        				Integer.parseInt(cbCompID.getValue()+""),txtEmpType.getText(),txtDesc.getText(),
		        				Double.parseDouble(txtRate.getText()));
		        		
	        			String query = "UPDATE tbl_EmpType SET compID="+emp.getCompID()+",etName='"+emp.getEtName()
	        			+"',etDescription='"+emp.getEtDescription()+"',etRate="+emp.getEtRate()+" WHERE etID="+emp.getEtID();
	        			
	        			if(Bindings.isNotEmpty(tblEmpType.getItems()).get()) {
		        			if(emp.getCompID()==tblEmpType.getItems().get(0).getCompID()) {
		        				EmpTypeDAOImpl.getInstance().update(query);
		        				empList.parallelStream()
		        					   .filter(et->et.getEtID()==emp.getEtID())
		        					   .forEach(et->{
		        						   et.setCompID(emp.getCompID());
		        						   et.setEtName(emp.getEtName());
		        						   et.setEtDescription(emp.getEtDescription());
		        						   et.setEtRate(emp.getEtRate());
		        					   });
		        			}else EmpTypeDAOImpl.getInstance().update(query);
		        		}else EmpTypeDAOImpl.getInstance().update(query);
		        		
		        		clearEmpFields();
		        		Dialogs.showMessage("Update Employee Type", "Updated Successfully",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Update Employee Type Failed", "Fill up required fields",Main.getStgMenu());
	        			
	        });
	        
	        btnClearEmpType.setOnAction(e->clearEmpFields());
	        
	        txtCompContact.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtCompContact.setText(oldValue);
	            }
	            if(newValue.length()>11) {
	            	txtCompContact.setText(oldValue);
	            }
	        });
	        
	        txtRate.textProperty().addListener((observable, oldValue, newValue) -> {
	        	if(!txtRate.getText().matches("\\d*(\\.\\d{0,2})?")){
	        		txtRate.setText(oldValue);
                }
	        });
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	//User-Defined methods
	
	public void clearCompFields() {
		txtCompID.setText("");
		txtCompName.setText("");
		txtCompContact.setText("");
		txtCompLocation.setText("");
		btnUpdateComp.setDisable(true);
	}
	
	public void clearEmpFields() {
		txtEmpTypeID.setText("");
		txtEmpType.setText("");
		cbCompID.setValue(null);
		txtRate.setText("");
		txtDesc.setText("");
		btnUpdateEmpType.setDisable(true);
	}
	
	public boolean checkCompFields() {
		return !((txtCompName.getText().equals("")||txtCompName.getText().startsWith(" "))
				||(txtCompContact.getText().equals("")||txtCompContact.getText().startsWith(" "))
				||(txtCompLocation.getText().equals("")||txtCompLocation.getText().startsWith(" ")))?true:false;
	}
	
	public boolean checkEmpFields() {
		return !((txtEmpType.getText().equals("")||txtEmpType.getText().startsWith(" "))
				||(cbCompID.getValue().equals(null)||cbCompID.getValue().equals(""))
				||(txtRate.getText().equals("")||txtRate.getText().startsWith(" "))
				||(txtDesc.getText().equals("")||txtDesc.getText().startsWith(" ")))?true:false;
	}
	
	public boolean checkCompID(int compID) {
		return CompanyDAOImpl.getInstance().getAllCompany()
							 .parallelStream()
							 .filter(e->e.getCompID()==compID)
							 .findFirst()
							 .isPresent();
	}
	
	public void searchCompID(String str) {
		int length = 0;
		for(Company c : CompanyDAOImpl.getInstance().getAllCompany()) {
			if(str.equals("")) {
				clearCompFields();
			}
			else if((c.getCompID()+"").contains(str)) {
				txtCompID.setText(c.getCompID()+"");
				txtCompName.setText(c.getCompName());
				txtCompContact.setText(c.getCompContact());
				txtCompLocation.setText(c.getCompLocation());
				btnUpdateComp.setDisable(false);
				length = (c.getCompID()+"").length();
				break;
			}
		}
		if(str.length()>length) {
			clearCompFields();
		}
	}
	
	public void searchEmpTypeID(String str) {
		int length = 0;
		for(EmpType e : EmpTypeDAOImpl.getInstance().getAllEmpType()) {
			if(str.equals("")) {
				clearEmpFields();
			}else if((e.getEtID()+"").contains(str)) {
				txtEmpTypeID.setText(e.getEtID()+"");
				cbCompID.setValue(e.getCompID()+"");
				txtEmpType.setText(e.getEtName());
				txtDesc.setText(e.getEtDescription());
				txtRate.setText(e.getEtRate()+"");
				btnUpdateEmpType.setDisable(false);
				length = (e.getEtID()+"").length();
				break;
			}
		}
		if(str.length()>length)
			clearEmpFields();
	}
}
