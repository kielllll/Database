package application.salary;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
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
import application.dao.DTRDAOImpl;
import application.dao.SalaryDAOImpl;
import application.database.Database;
import application.entities.DTR;
import application.entities.Salary;
import application.util.ComboBoxUtil;
import application.util.Dialogs;
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

public class SalaryController implements Initializable, ControlledScreen{
	
	@FXML JFXButton btnRefresh;
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXTextField txtSearchSalaryID;
	@FXML TableView<Salary> tblSalary;
	@FXML TableColumn<Salary, Integer> colSalaryID;
	@FXML TableColumn<Salary, Integer> colEmpID;
	@FXML TableColumn<Salary, String> colStatus;
	@FXML JFXTextField txtSalaryID;
	@FXML JFXTextField txtAbsences;
	@FXML JFXComboBox<String> cbEmpID;
	@FXML JFXComboBox<String> cbStatus;
	@FXML JFXButton btnAddSalary;
	@FXML JFXButton btnUpdateSalary;
	@FXML JFXButton btnClearSalary;
	@FXML JFXTextField txtSearchDtrID;
	@FXML JFXTextField txtTotalAmount;
	@FXML TableView<DTR> tblDtr;
	@FXML TableColumn<DTR, Integer> colDtrID;
	@FXML TableColumn<DTR, Integer> colSalaryID2;
	@FXML TableColumn<DTR, LocalDate> colDate;
	@FXML TableColumn<DTR, Integer> colHours;
	@FXML JFXTextField txtDtrID;
	@FXML JFXComboBox<String> cbSalaryID;
	@FXML JFXDatePicker dpDtrDate;
	@FXML JFXTextField txtDtrHours;
	@FXML JFXButton btnAddDtr;
	@FXML JFXButton btnUpdateDtr;
	@FXML JFXButton btnClearDtr;
	
	private ScreensController screenPage;
	private static ObservableList<Salary> salaryList = FXCollections.observableArrayList();
	private ObservableList<DTR> dtrList = FXCollections.observableArrayList();
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			
			txtSalaryID.setDisable(true);
			txtDtrID.setDisable(true);
			btnUpdateSalary.setDisable(true);
			btnUpdateDtr.setDisable(true);
			txtTotalAmount.setDisable(true);
			
			for(Node node : box.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
						case "Payroll"	: screenPage.setScreen(Main.getPayrollID());
						  				  break;
						case "Companies": screenPage.setScreen(Main.getCompaniesID());
						  				  break;
						case "Units"	: screenPage.setScreen(Main.getUnitsID());
						  				  break;
						case "Employees"	: screenPage.setScreen(Main.getEmployeesID());
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
			
			fillSalary();
			
			//Binding the data into the column (tbl_Salary)
			colSalaryID.setCellValueFactory(cellData -> cellData.getValue().salaryIDProperty().asObject());
			colEmpID.setCellValueFactory(cellData -> cellData.getValue().empIDProperty().asObject());
			colStatus.setCellValueFactory(cellData -> cellData.getValue().salaryStatusProperty());
			
			//Binding the data into the column (tbl_DTR)
			colDtrID.setCellValueFactory(cellData -> cellData.getValue().dtrIDProperty().asObject());
			colSalaryID2.setCellValueFactory(cellData -> cellData.getValue().salaryIDProperty().asObject());
			colDate.setCellValueFactory(cellData -> cellData.getValue().dtrDateProperty());
			colHours.setCellValueFactory(cellData -> cellData.getValue().dtrHourProperty().asObject());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Salary> filteredData = new FilteredList<>(salaryList, e -> true);
	        FilteredList<DTR> filteredData2 = new FilteredList<>(dtrList, e -> true);
	        
	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearchSalaryID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(s -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(s.getSalaryID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchSalaryID.setText(oldValue);
	            }
	        });
	        
	        txtSearchDtrID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData2.setPredicate(dtr -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(dtr.getDtrID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchDtrID.setText(oldValue);
	            }
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Salary> sortedData = new SortedList<>(filteredData);
	        SortedList<DTR> sortedData2 = new SortedList<>(filteredData2);
	        
	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblSalary.comparatorProperty());
	        sortedData2.comparatorProperty().bind(tblDtr.comparatorProperty());
	        
	        // 5. Add sorted (and filtered) data to the table.
	        tblSalary.setItems(sortedData);
	        tblDtr.setItems(sortedData2);
	        
	        //adding data to the Combo Box
	        cbStatus.getItems().add("Not issued");
	        cbStatus.getItems().add("Issued");
	        refresh();
	        
	        cbEmpID.setEditable(true);
	        cbSalaryID.setEditable(true);
	        
	        // adding up listeners to the components
	        txtSearchSalaryID.setOnKeyReleased(e-> searchSalary(txtSearchSalaryID.getText()));
	        
	        tblSalary.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(tblSalary.getItems()).get()) {
	        		Salary s = tblSalary.getFocusModel().getFocusedItem();
	        		txtSalaryID.setText(s.getSalaryID()+"");
	        		cbEmpID.setValue(s.getEmpID()+"");
	        		cbStatus.setValue(s.getSalaryStatus()+"");
	        		btnUpdateSalary.setDisable(false);
	        		
	        		double amount = 0.0;
	        		
	        		String query = "SELECT (SUM(d.dtrHour)*et.etRate)-(s.numAbsc*(8*et.etRate)) AS amount\r\n" + 
	        				"FROM tbl_dtr d \r\n" + 
	        				"INNER JOIN tbl_salary s ON d.salaryID = s.salaryID\r\n" + 
	        				"INNER JOIN tbl_employee e ON s.empID = e.empID\r\n" + 
	        				"INNER JOIN tbl_emptype et ON e.etID = et.etID\r\n" + 
	        				"WHERE e.empID = "+s.getSalaryID();
	        		
	        		try {
	        			Statement st = Database.getInstance().getDBConn().createStatement();
	        			ResultSet rs = st.executeQuery(query);
	        			
	        			while(rs.next()) {
	        				amount = rs.getDouble("amount");
	        			}
	        			
	        			st.close();
	        			rs.close();
	        		}catch(Exception err) {
	        			err.printStackTrace();
	        		}
	        		
	        		txtTotalAmount.setText(amount+"");
	        		
	        		boolean check = SalaryDAOImpl.getInstance()
	        									 .getAllSalary()
	        									 .parallelStream()
	        									 .filter(sa->s.getSalaryID()==sa.getSalaryID())
	        									 .findAny()
	        									 .isPresent();
	        		if(check) {
	        			Salary s2 = SalaryDAOImpl.getInstance()
	        									 .getAllSalary()
	        									 .parallelStream()
	        									 .filter(ss->ss.getSalaryID()==s.getSalaryID())
	        									 .findFirst()
	        									 .get();
	        			
	        			txtAbsences.setText(s2.getNumAbsc()+"");
	        		}
	        		dtrList.clear();
	        		
	        		//Sort the data from the database (tbl_DTR)
	        		List<DTR> sortedEmpList = DTRDAOImpl.getInstance()
							.getAllDTR()
							.parallelStream()
	    					.filter(d->d.getSalaryID()==s.getSalaryID())
							.sorted((d1,d2) -> (d1.getDtrID()+"").compareTo(d2.getDtrID()+""))
							.collect(Collectors.toList());

	        		//Add the data into the table (tbl_EmpTyp)
	        		sortedEmpList.stream().forEach(d-> dtrList.add(d));
	        	}
	        });
	        
	        txtSearchDtrID.setOnKeyReleased(e-> searchDtr(txtSearchDtrID.getText()));
	        
	        tblDtr.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(tblDtr.getItems()).get()) {
	        		DTR dtr = tblDtr.getFocusModel().getFocusedItem();
	        		txtDtrID.setText(dtr.getDtrID()+"");
	        		cbSalaryID.setValue(dtr.getSalaryID()+"");
	        		dpDtrDate.setValue(dtr.getDtrDate());
	        		txtDtrHours.setText(dtr.getDtrHour()+"");
	        		btnUpdateDtr.setDisable(false);
	        	}
	        });
	        
	        txtDtrHours.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtDtrHours.setText(oldValue);
	            }
	        });
	        
	        txtAbsences.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtAbsences.setText(oldValue);
	            }
	        });
	        
	        txtSearchSalaryID.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchSalaryID.setText(oldValue);
	            }
	        });
	        
	        txtSearchDtrID.textProperty().addListener((observable, oldValue, newValue) -> {
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchDtrID.setText(oldValue);
	            }
	        });
	        
	        btnAddSalary.setOnAction(e->{
	        	if(txtSalaryID.getText().equals("")||txtSalaryID.getText().equals(null)) {
	        		if(checkSalaryFields()) {
	        			Salary s = new Salary(salaryList.size()+1,Integer.parseInt(cbEmpID.getValue()+""),"Not issued",0);
	        			
	        			SalaryDAOImpl.getInstance().insert(s);
	        			salaryList.add(s);
	        			clearSalaryFields();
	        			refresh();
	        			Dialogs.showMessage("Add Salary", "Added Succesfully",Main.getStgMenu());
	        		}else Dialogs.showErrorMessage("Add Salary Failed", "Fill up required fields",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Add Salary Failed", "Click clear button and try again",Main.getStgMenu());
	        });
	        
	        btnUpdateSalary.setOnAction(e->{
	        	if(checkSalaryFields()) {
        			Salary	s = new Salary(Integer.parseInt(txtSalaryID.getText()),Integer.parseInt(cbEmpID.getValue()+""),"Not issued",
        					Integer.parseInt(txtAbsences.getText()));
	        		String query = "UPDATE tbl_Salary SET empID="+s.getEmpID()
	        							+",salaryStatus='"+s.getSalaryStatus()
	        							+"',numAbsc="+s.getNumAbsc()
	        							+" WHERE salaryID="+s.getSalaryID();
	        		
	        		SalaryDAOImpl.getInstance().update(query);
	        		salaryList.parallelStream()
	        				  .filter(sal->sal.getSalaryID()==s.getSalaryID())
	        				  .forEach(sal->{
	        					  sal.setEmpID(s.getEmpID());
	        					  sal.setSalaryStatus(s.getSalaryStatus());
	        				  });
	        		clearSalaryFields();
	        		refresh();
	        		Dialogs.showMessage("Update Salary", "Updated successfully",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Update Salary Failed", "Fill up required fields",Main.getStgMenu());
	        });
	        
	        btnClearSalary.setOnAction(e->clearSalaryFields());
	        
	        btnAddDtr.setOnAction(e->{
	        	if(txtDtrID.getText().equals("")||txtDtrID.getText().equals(null)) {
	        		if(checkDTRFields()) {
	        			DTR dtr = new DTR(DTRDAOImpl.getInstance().getAllDTR().size()+1,Integer.parseInt(cbSalaryID.getValue()+""),
	        					dpDtrDate.getValue(),Integer.parseInt(txtDtrHours.getText()));
	        			
	        			if(Bindings.isNotEmpty(tblDtr.getItems()).get()) {
	        				if(dtr.getSalaryID()==tblDtr.getItems().get(0).getSalaryID()) {
	        					dtrList.add(dtr);
	        					DTRDAOImpl.getInstance().insert(dtr);
	        				}else DTRDAOImpl.getInstance().insert(dtr);
	        			}else DTRDAOImpl.getInstance().insert(dtr);
	        			
	        			clearDtrFields();
	        			Dialogs.showMessage("Add DTR", "Added successfully",Main.getStgMenu());
	        		}else Dialogs.showErrorMessage("Add DTR Failed", "Fill up required fields",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Add DTR Failed", "Click clear button and try again",Main.getStgMenu());
	        });
	        
	        btnUpdateDtr.setOnAction(e->{
	        	if(checkDTRFields()) {
        			DTR dtr = new DTR(Integer.parseInt(txtDtrID.getText()),Integer.parseInt(cbSalaryID.getValue()+""),
        					dpDtrDate.getValue(),Integer.parseInt(txtDtrHours.getText()));
        			
        			String query = "UPDATE tbl_DTR SET salaryID="+dtr.getSalaryID()+", dtrDate='"+(dtr.getDtrDate()+"")
        									+"', dtrHour="+dtr.getDtrHour()+" WHERE dtrID="+dtr.getDtrID();
        			if(Bindings.isNotEmpty(tblDtr.getItems()).get()) {
        				if(dtr.getSalaryID()==tblDtr.getItems().get(0).getSalaryID()) {
        					dtrList.parallelStream()
        						   .filter(d->d.getDtrID()==dtr.getDtrID())
        						   .forEach(d->{
        							   d.setSalaryID(dtr.getSalaryID());
        							   d.setDtrDate(dtr.getDtrDate());
        							   d.setDtrHour(dtr.getDtrHour());
        						   });
        					DTRDAOImpl.getInstance().update(query);
        				}else DTRDAOImpl.getInstance().update(query);
        			}else DTRDAOImpl.getInstance().update(query);
        			
        			clearDtrFields();
        			Dialogs.showMessage("Add DTR", "Added successfully",Main.getStgMenu());
        		}else Dialogs.showErrorMessage("Add DTR Failed", "Fill up required fields",Main.getStgMenu());
	        });
	        
	        btnClearDtr.setOnAction(e->clearDtrFields());
	        
	        btnRefresh.setOnAction(e->refresh());
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	//USER DEFINED METHODS
	public void refresh() {
		cbEmpID.setItems(ComboBoxUtil.fillCBEmpID());
		cbSalaryID.setItems(ComboBoxUtil.fillCBSalaryID());
	}
	
	public boolean checkSalaryFields() {
		return !(((cbEmpID.getValue()+"").equals("null")||(cbEmpID.getValue()).equals(null))
				||((cbStatus.getValue()+"").equals("null")||(cbStatus.getValue()).equals(null))
				||(txtAbsences.getText().equals("")||txtAbsences.getText().startsWith(" ")))?true:false;
	}
	
	public boolean checkDTRFields() {
		return !((txtDtrHours.getText().equals("")||txtDtrHours.getText().startsWith(" "))
				||(dpDtrDate.getValue().equals(null))
				||(cbSalaryID.getValue().equals(null)||cbSalaryID.getValue().equals("")))?true:false;
	}
	
	public void clearSalaryFields() {
		txtSalaryID.setText("");
		cbEmpID.setValue(null);
		cbStatus.setValue(null);
		txtAbsences.setText("");
		btnUpdateSalary.setDisable(true);
		txtTotalAmount.setText("");
	}
	
	public void clearDtrFields() {
		txtDtrID.setText("");
		cbSalaryID.setValue(null);
		dpDtrDate.setValue(null);
		txtDtrHours.setText("");
		btnUpdateDtr.setDisable(true);
	}
	
	public static void fillSalary() {
		salaryList.clear();
		//Sort the data from database (tbl_Salary)
		List<Salary> sortedSalaryList = SalaryDAOImpl.getInstance()
									.getAllSalary()
									.parallelStream()
									.sorted((s1,s2) -> (s1.getSalaryID()+"").compareTo(s2.getSalaryID()+""))
									.collect(Collectors.toList());
		
		//Add the data into the table (tbl_Salary)
		sortedSalaryList.stream()
				  .forEach(e-> salaryList.add(e));
	}
	
	public void searchSalary(String id) {
		int length = 0;
		for(Salary s : SalaryDAOImpl.getInstance().getAllSalary()) {
			if(id.equals("")) {
				clearSalaryFields();
			}
			else if((s.getSalaryID()+"").contains(id)) {
				txtSalaryID.setText(s.getSalaryID()+"");
				cbEmpID.setValue(s.getEmpID()+"");
				cbStatus.setValue(s.getSalaryStatus()+"");
				btnUpdateSalary.setDisable(false);
				cbSalaryID.setValue(s.getSalaryID()+"");
				txtAbsences.setText(s.getNumAbsc()+"");
				length = (s.getSalaryID()+"").length();
				double amount = 0.0;
        		
        		String query = "SELECT (SUM(d.dtrHour)*et.etRate)-(s.numAbsc*(8*et.etRate)) AS amount\r\n" + 
        				"FROM tbl_dtr d \r\n" + 
        				"INNER JOIN tbl_salary s ON d.salaryID = s.salaryID\r\n" + 
        				"INNER JOIN tbl_employee e ON s.empID = e.empID\r\n" + 
        				"INNER JOIN tbl_emptype et ON e.etID = et.etID\r\n" + 
        				"WHERE e.empID = "+s.getSalaryID();
        		
        		try {
        			Statement st = Database.getInstance().getDBConn().createStatement();
        			ResultSet rs = st.executeQuery(query);
        			
        			while(rs.next()) {
        				amount = rs.getDouble("amount");
        			}
        			
        			st.close();
        			rs.close();
        		}catch(Exception err) {
        			err.printStackTrace();
        		}
        		
        		txtTotalAmount.setText(amount+"");
				break;
			}
		}
		
		if(id.length()>length) {
			clearSalaryFields();
		}
	}
	
	public void searchDtr(String id) {
		int length = 0;
		for(DTR dtr : DTRDAOImpl.getInstance().getAllDTR()) {
			if(id.equals(""))
				clearDtrFields();
			else if((dtr.getDtrID()+"").contains(id)) {
				txtDtrID.setText(dtr.getDtrID()+"");
				cbSalaryID.setValue(dtr.getSalaryID()+"");
				dpDtrDate.setValue(dtr.getDtrDate());
				txtDtrHours.setText(dtr.getDtrHour()+"");
				length = (dtr.getDtrID()+"").length();
				break;
			}
		}
		
		if(id.length()>length)
			clearDtrFields();
	}
}
