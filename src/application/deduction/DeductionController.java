package application.deduction;

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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.ControlledScreen;
import application.Main;
import application.ScreensController;
import application.dao.CashAdvanceDAOImpl;
import application.dao.DeductionDAOImpl;
import application.dao.LookupDAOImpl;
import application.dao.SSSDAOImpl;
import application.database.Database;
import application.entities.CashAdvance;
import application.entities.Deduction;
import application.entities.Lookup;
import application.entities.SSS;
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

public class DeductionController implements Initializable, ControlledScreen{
	
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXButton btnRefresh;
	@FXML JFXTextField txtSearchDeductionID;
	@FXML TableView<Deduction> tblDeduction;
	@FXML TableColumn<Deduction, Integer> colDeductionID;
	@FXML TableColumn<Deduction, Integer> colEmpID;
	@FXML TableColumn<Deduction, String> colDeductionStatus;
	@FXML JFXTextField txtSearchCaID;
	@FXML JFXTextField txtTotalAmount;
	@FXML TableView<CashAdvance> tblCashAdvance;
	@FXML TableColumn<CashAdvance, Integer> colCaID;
	@FXML TableColumn<CashAdvance, Integer> colLookupID2;
	@FXML TableColumn<CashAdvance, LocalDate> colCaDate;
	@FXML TableColumn<CashAdvance, String> colCaDescription;
	@FXML TableColumn<CashAdvance, Double> colCaAmount;
	@FXML TableColumn<CashAdvance, String> colCaStatus;
	@FXML JFXTextField txtDeductionID;
	@FXML JFXComboBox<String> cbEmpID;
	@FXML JFXComboBox<String> cbDeductionStatus;
	@FXML JFXButton btnAddDeduction;
	@FXML JFXButton btnUpdateDeduction;
	@FXML JFXButton btnClearDeduction;
	@FXML JFXTextField txtCaID;
	@FXML JFXComboBox<String> cbDeductionID;
	@FXML JFXTextField txtCaAmount;
	@FXML JFXDatePicker dpCaDate;
	@FXML JFXTextArea txtCaDescription;
	@FXML JFXComboBox<String> cbCaStatus;
	@FXML JFXButton btnAddCa;
	@FXML JFXButton btnUpdateCa;
	@FXML JFXButton btnClearCa;
	@FXML JFXTextField txtSSSID;
	@FXML JFXComboBox<String> cbEmpID2;
	@FXML JFXTextField txtSSSAmount;
	@FXML JFXButton btnAddSSS;
	@FXML JFXButton btnUpdateSSS;
	@FXML JFXButton btnClearSSS;
	
	private ScreensController screenPage;
	private static ObservableList<Deduction> deductionList = FXCollections.observableArrayList();
	private ObservableList<Lookup> lookupList = FXCollections.observableArrayList();
	private ObservableList<CashAdvance> caList = FXCollections.observableArrayList();
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			
			txtDeductionID.setDisable(true);
			btnUpdateDeduction.setDisable(true);
			txtCaID.setDisable(true);
			btnUpdateCa.setDisable(true);
			txtSSSID.setDisable(true);
			txtTotalAmount.setDisable(true);
			btnUpdateSSS.setDisable(true);
			
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
						case "Employees": screenPage.setScreen(Main.getEmployeesID());
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
			
			fillDeduction();
			
			//Sort the data from database (tbl_Lookup)
			List<Lookup> sortedLookupList = LookupDAOImpl.getInstance()
														 .getAllLookup()
														 .parallelStream()
														 .sorted((l1,l2)->(l1.getLookupID()+"").compareTo(l2.getLookupID()+""))
														 .collect(Collectors.toList());
			
			//Add the data into the table (tbl_Lookup)
			sortedLookupList.stream()
						  	.forEach(l -> lookupList.add(l));
			
			//Binding the data into tbl_Deduction
			colDeductionID.setCellValueFactory(cellData -> cellData.getValue().deductionIDProperty().asObject());
			colEmpID.setCellValueFactory(cellData -> cellData.getValue().empIDProperty().asObject());
			colDeductionStatus.setCellValueFactory(cellData -> cellData.getValue().deductionStatusProperty());
			
			//Binding the data into the tbl_CashAdvance
			colCaID.setCellValueFactory(cellData -> cellData.getValue().caIDProperty().asObject());
			colLookupID2.setCellValueFactory(cellData -> cellData.getValue().lookupIDProperty().asObject());
			colCaDate.setCellValueFactory(cellData -> cellData.getValue().caDateProperty());
			colCaDescription.setCellValueFactory(cellData -> cellData.getValue().caDescriptionProperty());
			colCaAmount.setCellValueFactory(cellData -> cellData.getValue().caAmountProperty().asObject());
			colCaStatus.setCellValueFactory(cellData -> cellData.getValue().caStatusProperty());
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Deduction> filteredData = new FilteredList<>(deductionList, e -> true);
	        FilteredList<CashAdvance> filteredData3 = new FilteredList<>(caList, e -> true);
	        
	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearchDeductionID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(d -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(d.getDeductionID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchDeductionID.setText(oldValue);
	            }
	        });
	        
	        txtSearchCaID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData3.setPredicate(c -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(c.getCaID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchCaID.setText(oldValue);
	            }
	        });
	        
	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Deduction> sortedData = new SortedList<>(filteredData);
	        SortedList<CashAdvance> sortedData3 = new SortedList<>(filteredData3);
	        
	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(tblDeduction.comparatorProperty());
	        sortedData3.comparatorProperty().bind(tblCashAdvance.comparatorProperty());
	        
	        // 5. Add sorted (and filtered) data to the table.
	        tblDeduction.setItems(sortedData);
	        tblCashAdvance.setItems(sortedData3);
	        
	        //Add items to the combo box
	        refresh();
	        
	        cbDeductionStatus.getItems().add("Issued");
	        cbDeductionStatus.getItems().add("Not Issued");
	        
	        cbCaStatus.getItems().add("Done");
	        cbCaStatus.getItems().add("Pending");
	        
	        cbEmpID.setEditable(true);
			cbEmpID2.setEditable(true);
	        
	        //Adding listeners to the components
	        txtSearchDeductionID.setOnKeyReleased(e->searchDeduction(txtSearchDeductionID.getText()));
	        
	        tblDeduction.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(tblDeduction.getItems()).get()) {
	        		Deduction d = tblDeduction.getFocusModel().getFocusedItem();
	        		
	        		txtDeductionID.setText(d.getDeductionID()+"");
	        		cbEmpID.setValue(d.getEmpID()+"");
	        		cbDeductionStatus.setValue(d.getDeductionStatus());
	        		
	        		btnUpdateDeduction.setDisable(false);
	        		
	        		caList.clear();
	        		
	        		Double amount = 0.0;
	        		boolean check = CashAdvanceDAOImpl.getInstance()
	        										  .getAllCashAdvance()
	        										  .parallelStream()
	        										  .filter(ca->ca.getLookupID()==d.getDeductionID())
	        										  .findAny()
	        										  .isPresent();
	        		if(check) {
		        		String query = "SELECT (SUM(ca.caAmount)+s.sssAmount) AS amount \r\n" + 
		        				"FROM tbl_cashadvance ca\r\n" + 
		        				"INNER JOIN tbl_lookup l ON ca.lookupID = l.lookupID\r\n" + 
		        				"INNER JOIN tbl_deduction d ON l.deductionID = d.deductionID\r\n" + 
		        				"INNER JOIN tbl_employee e ON d.empID = e.empID\r\n" + 
		        				"INNER JOIN tbl_sss s ON e.empID = s.empID\r\n" + 
		        				"WHERE e.empID = "+d.getEmpID();
		        		
		        		try {
		        			Statement st = Database.getInstance().getDBConn().createStatement();
		        			ResultSet rs = st.executeQuery(query);
		        			
		        			while(rs.next()) {
		        				amount = rs.getDouble("amount");
		        			}
		        			
		        			txtTotalAmount.setText(amount+"");
		        			
		        			st.close();
		        			rs.close();
		        		}catch (Exception err) {
		        			err.printStackTrace();
		        		}
	        		}else {
	        			SSS s = SSSDAOImpl.getInstance()
	        							  .getAllSSS()
	        							  .parallelStream()
	        							  .filter(sss->sss.getEmpID()==d.getEmpID())
	        							  .findFirst()
	        							  .get();
	        			amount = s.getSssAmount();
	        			txtTotalAmount.setText(amount+"");
	        		}
	        		
	        		//Sort the data from database (tbl_Lookup)
	    			List<CashAdvance> sortedCaList = CashAdvanceDAOImpl.getInstance()
	    														 .getAllCashAdvance()
	    														 .parallelStream()
	    														 .filter(c -> c.getLookupID()==d.getDeductionID())
	    														 .sorted((c1,c2)->(c1.getCaID()+"").compareTo(c2.getCaID()+""))
	    														 .collect(Collectors.toList());
	    			
	    			//Add the data into the table (tbl_Lookup)
	    			sortedCaList.stream()
	    						  	.forEach(c -> caList.add(c));
	    			
	    			check = SSSDAOImpl.getInstance()
	    									  .getAllSSS()
	    									  .parallelStream()
	    									  .filter(s -> s.getEmpID()==d.getEmpID())
	    									  .findFirst()
	    									  .isPresent();
	    			if(check) {
		    			SSS sss = SSSDAOImpl.getInstance()
		    					  .getAllSSS()
		    					  .parallelStream()
		    					  .filter(s -> s.getEmpID()==d.getEmpID())
		    					  .findFirst()
		    					  .get();
		    			
		    			txtSSSID.setText(sss.getSssID()+"");
		    			cbEmpID2.setValue(sss.getEmpID()+"");
		    			txtSSSAmount.setText(sss.getSssAmount()+"");
		    			btnUpdateSSS.setDisable(false);
	    			}
	        	}
	        });
	        
	        txtSearchCaID.setOnKeyReleased(e->searchCashAdvance(txtSearchCaID.getText()));
	        
	        tblCashAdvance.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(tblCashAdvance.getItems()).get()) {
	        		CashAdvance ca = tblCashAdvance.getFocusModel().getFocusedItem();
	        		
	        		txtCaID.setText(ca.getCaID()+"");
	        		cbDeductionID.setValue(ca.getLookupID()+"");
	        		txtCaAmount.setText(ca.getCaAmount()+"");
	        		dpCaDate.setValue(ca.getCaDate());
	        		txtCaDescription.setText(ca.getCaDescription());
	        		cbCaStatus.setValue(ca.getCaStatus());
	        		
	        		btnUpdateCa.setDisable(false);
	        	}
	        });
	        
	        btnAddDeduction.setOnAction(e->{
	        	if(txtDeductionID.getText().equals("")) {
		        	if(checkDeductionFields()) {
		        		Deduction d = new Deduction(deductionList.size()+1
		        					,Integer.parseInt(cbEmpID.getSelectionModel().getSelectedItem()+"")
		        					,cbDeductionStatus.getSelectionModel().getSelectedItem());
		        		
		        		Lookup l = new Lookup(lookupList.size()+1
		        					,d.getDeductionID());
		        		
		        		DeductionDAOImpl.getInstance().insert(d);
		        		deductionList.add(d);
		        		
		        		LookupDAOImpl.getInstance().insert(l);
		        		lookupList.add(l);
		        		
		        		clearDeductionFields();
		        		refresh();
		        		Dialogs.showMessage("Add Deduction", "Added successfully", Main.getStgMenu());
		        	}else Dialogs.showErrorMessage("Add Deduction Failed", "Fill up required fields",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Add Deduction Failed", "Click clear button and try again", Main.getStgMenu());
	        });
	        
	        btnUpdateDeduction.setOnAction(e->{
	        	if(checkDeductionFields()) {
	        		Deduction d = new Deduction(Integer.parseInt(txtDeductionID.getText())
	        				,Integer.parseInt(cbEmpID.getSelectionModel().getSelectedItem()+"")
        					,cbDeductionStatus.getSelectionModel().getSelectedItem());
	        		
	        		String query = "UPDATE tbl_Deduction SET empID="+d.getEmpID()
	        					+", deductionStatus='"+d.getDeductionStatus()+"' WHERE deductionID="+d.getDeductionID();
	        		
	        		DeductionDAOImpl.getInstance().update(query);
	        		
	        		clearDeductionFields();
	        		refresh();
	        		
	        		deductionList.parallelStream()
	        					 .filter(dd -> dd.getDeductionID()==d.getDeductionID())
	        					 .forEach(dd->{
	        						 dd.setEmpID(d.getEmpID());
	        						 dd.setDeductionStatus(d.getDeductionStatus());
	        					 });
	        		
	        		Dialogs.showMessage("Update deduction", "Updated successfully", Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Update Deduction", "Fill up required fields", Main.getStgMenu());
	        });
	        
	        btnClearDeduction.setOnAction(e->clearDeductionFields());
	        
	        btnAddCa.setOnAction(e->{
	        	if(txtCaID.getText().equals(""))
		        	if(checkCaFields()) {
		        		CashAdvance ca = new CashAdvance(CashAdvanceDAOImpl.getInstance().getAllCashAdvance().size()+1
		        				,Integer.parseInt(cbDeductionID.getSelectionModel().getSelectedItem()+"")
		        				,dpCaDate.getValue()
		        				,txtCaDescription.getText()
		        				,Double.parseDouble(txtCaAmount.getText())
		        				,(cbCaStatus.getValue()+""));
		        		
		        		if(Bindings.isNotEmpty(tblDeduction.getItems()).get()) {
		        			if(ca.getLookupID()==tblDeduction.getItems().get(0).getDeductionID()) {
		        				CashAdvanceDAOImpl.getInstance().insert(ca);
		        				caList.add(ca);
		        			}else CashAdvanceDAOImpl.getInstance().insert(ca);
		        		}else CashAdvanceDAOImpl.getInstance().insert(ca);
		        		
		        		clearCaFields();
		        		refresh();
		        		Dialogs.showMessage("Add Cash Advance", "Added successfully", Main.getStgMenu());
		        	}else Dialogs.showErrorMessage("Add Cash Advance Failed", "Fill up required fields",Main.getStgMenu());
	        	else Dialogs.showErrorMessage("Add Cash Advance Failed", "Click clear button and try again", Main.getStgMenu());
	        });
	        
	        btnUpdateCa.setOnAction(e->{
	        	if(checkCaFields()) {
	        		CashAdvance ca = new CashAdvance(Integer.parseInt(txtCaID.getText())
	        				,Integer.parseInt(cbDeductionID.getSelectionModel().getSelectedItem()+"")
	        				,dpCaDate.getValue()
	        				,txtCaDescription.getText()
	        				,Double.parseDouble(txtCaAmount.getText())
	        				,(cbCaStatus.getValue()+""));
	        		
	        		String query = "UPDATE tbl_CashAdvance SET lookupID="+ca.getLookupID()
	        					 +", caDate='"+ca.getCaDate()+"', caDescription='"+ca.getCaDescription()
	        					 +"', caAmount="+ca.getCaAmount()+", caStatus='"+ca.getCaStatus()
	        					 +"' WHERE caID="+ca.getCaID();
	        		
	        		if(Bindings.isNotEmpty(tblDeduction.getItems()).get()) {
	        			if(ca.getLookupID()==tblDeduction.getItems().get(0).getDeductionID()) {
	        				CashAdvanceDAOImpl.getInstance().update(query);
	        				caList.parallelStream()
	        					  .filter(c->c.getCaID()==ca.getCaID())
	        					  .forEach(c->{
	        						  c.setLookupID(ca.getLookupID());
	        						  c.setCaDate(ca.getCaDate());
	        						  c.setCaDescription(ca.getCaDescription());
	        						  c.setCaAmount(ca.getCaAmount());
	        						  c.setCaStatus(ca.getCaStatus());
	        					  });
	        			}else CashAdvanceDAOImpl.getInstance().update(query);
	        		}else CashAdvanceDAOImpl.getInstance().update(query);
	        		
	        		clearCaFields();
	        		refresh();
	        		Dialogs.showMessage("Update Cash Advance", "Updated successfully", Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Update Cash Advance Failed", "Fill up required fields", Main.getStgMenu());
	        });
	        
	        btnClearCa.setOnAction(e->clearCaFields());
	        
	        btnAddSSS.setOnAction(e->{
	        	if(txtSSSID.getText().equals("")) {
	        		if(checkSSSFields()) {
	        			if(!checkCbInput(cbEmpID2,cbEmpID2.getValue())) {
		        			SSS sss = new SSS(SSSDAOImpl.getInstance().getAllSSS().size()+1,Integer.parseInt(cbEmpID2.getValue()+""),Double.parseDouble(txtSSSAmount.getText()));
		        			
		        			boolean present = SSSDAOImpl.getInstance()
		        								  .getAllSSS()
		        								  .parallelStream()
		        								  .filter(s->s.getEmpID()==sss.getEmpID())
		        								  .findFirst()
		        								  .isPresent();
		        			if(!present) {
				        		SSSDAOImpl.getInstance().insert(sss);
				        		clearSSSFields();
				        		Dialogs.showMessage("Add SSS Account", "Added Successfully",Main.getStgMenu());
		        			}else Dialogs.showErrorMessage("Add SSS Account Failed", "Employee already registered",Main.getStgMenu());
	        			}else Dialogs.showErrorMessage("Add SSS Acount Failed", "Invalid Employee ID",Main.getStgMenu());
	        		}else Dialogs.showErrorMessage("Add SSS Account Failed", "Fill up required fields",Main.getStgMenu());
	        	}else Dialogs.showErrorMessage("Add SSS Account Failed", "Click clear button and try again",Main.getStgMenu());
	        });
	        
	        btnUpdateSSS.setOnAction(e->{
	        		if(checkSSSFields()) {
		        			SSS sss = new SSS(SSSDAOImpl.getInstance().getAllSSS().size(),Integer.parseInt(cbEmpID2.getValue()+""),Double.parseDouble(txtSSSAmount.getText()));
		        			
		        			String query = "UPDATE tbl_SSS SET empID="+sss.getEmpID()+", sssAmount="+sss.getSssAmount()
		        							+" WHERE sssID="+sss.getSssID();
		        			
		        			boolean present = SSSDAOImpl.getInstance()
		        								  .getAllSSS()
		        								  .parallelStream()
		        								  .filter(s->s.getEmpID()==sss.getEmpID())
		        								  .findFirst()
		        								  .isPresent();
		        			if(present) {
				        		SSSDAOImpl.getInstance().update(query);
				        		clearSSSFields();
				        		Dialogs.showMessage("Update SSS Account", "Updated Successfully",Main.getStgMenu());
		        			}else Dialogs.showErrorMessage("Update SSS Acount Failed", "Invalid Employee ID",Main.getStgMenu());
	        		}else Dialogs.showErrorMessage("Update SSS Account Failed", "Fill up required fields",Main.getStgMenu());
	        });
	        
	        btnClearSSS.setOnAction(e->clearSSSFields());
	        
	        txtCaAmount.textProperty().addListener((observable, oldValue, newValue)->{
	        	if(!txtCaAmount.getText().matches("\\d*(\\.\\d{0,2})?")){
	        		txtCaAmount.setText(oldValue);
                }
	        });
	        
	        txtSSSAmount.textProperty().addListener((observable, oldValue, newValue)->{
	        	if(!txtSSSAmount.getText().matches("\\d*(\\.\\d{0,2})?")){
	        		txtSSSAmount.setText(oldValue);
                }
	        });
	        
	        btnRefresh.setOnAction(e->refresh());
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void fillDeduction() {
		deductionList.clear();
		
		//Sort the data from database (tbl_Deduction)
		List<Deduction> sortedCompList = DeductionDAOImpl.getInstance()
									.getAllDeduction()
									.parallelStream()
									.sorted((d1,d2) -> (d1.getDeductionID()+"").compareTo(d2.getDeductionID()+""))
									.collect(Collectors.toList());
		
		//Add the data into the table (tbl_Deduction)
		sortedCompList.stream()
				  .forEach(d-> deductionList.add(d));
	}

	//USER defined methods
	public void refresh() {
		cbEmpID.setItems(ComboBoxUtil.fillCBEmpID());
		cbEmpID2.setItems(ComboBoxUtil.fillCBEmpID());
        cbDeductionID.setItems(ComboBoxUtil.fillCBLookupID());
	}
	
	public void clearDeductionFields() {
		txtDeductionID.setText("");
		txtTotalAmount.setText("");
		cbEmpID.setValue(null);
		cbDeductionStatus.setValue(null);
		btnUpdateDeduction.setDisable(true);
	}
	
	public void clearCaFields() {
		txtCaID.setText("");
		cbDeductionID.setValue(null);
		txtCaAmount.setText("");
		dpCaDate.setValue(null);
		txtCaDescription.setText("");
		cbCaStatus.setValue(null);
		btnUpdateCa.setDisable(true);
	}
	
	public void clearSSSFields() {
		txtSSSID.setText("");
		cbEmpID2.setValue(null);
		txtSSSAmount.setText("");
		btnUpdateSSS.setDisable(true);
	}
	
	public boolean checkDeductionFields() {
		return !(((cbEmpID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbEmpID.getSelectionModel().getSelectedItem()+"").equals(""))
				||((cbDeductionStatus.getSelectionModel().getSelectedItem()+"").equals("null")||(cbDeductionStatus.getSelectionModel().getSelectedItem()+"").equals("")))?true:false;
	}
	
	public boolean checkCaFields() {
		return !(((cbDeductionID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbDeductionID.getSelectionModel().getSelectedItem()+"").equals(""))
				||(txtCaAmount.getText().equals(""))
				||(dpCaDate.getValue().equals(null))
				||(txtCaDescription.getText().equals(""))
				||(((cbCaStatus.getSelectionModel().getSelectedItem()+"").equals("null"))||(cbCaStatus.getSelectionModel().getSelectedItem()+"").equals("")))?true:false;
	}
	
	public void searchDeduction(String str) {
		if(str.equals(""))
			clearDeductionFields();
		else {
			int id = Integer.parseInt(str);
			
			boolean present = DeductionDAOImpl.getInstance()
											  .getAllDeduction()
											  .parallelStream()
											  .filter(d->id==d.getDeductionID())
											  .findFirst()
											  .isPresent();
			
			if(present) {
				Deduction d = DeductionDAOImpl.getInstance()
											  .getAllDeduction()
											  .parallelStream()
											  .filter(de->id==de.getDeductionID())
											  .findFirst()
											  .get();
				
				txtDeductionID.setText(d.getDeductionID()+"");
				cbEmpID.setValue(d.getEmpID()+"");
				cbDeductionStatus.setValue(d.getDeductionStatus());
				btnUpdateDeduction.setDisable(false);
				
				Double amount = 0.0;
        		boolean check = CashAdvanceDAOImpl.getInstance()
        										  .getAllCashAdvance()
        										  .parallelStream()
        										  .filter(ca->ca.getLookupID()==d.getDeductionID())
        										  .findAny()
        										  .isPresent();
        		if(check) {
	        		String query = "SELECT (SUM(ca.caAmount)+s.sssAmount) AS amount \r\n" + 
	        				"FROM tbl_cashadvance ca\r\n" + 
	        				"INNER JOIN tbl_lookup l ON ca.lookupID = l.lookupID\r\n" + 
	        				"INNER JOIN tbl_deduction d ON l.deductionID = d.deductionID\r\n" + 
	        				"INNER JOIN tbl_employee e ON d.empID = e.empID\r\n" + 
	        				"INNER JOIN tbl_sss s ON e.empID = s.empID\r\n" + 
	        				"WHERE e.empID = "+d.getEmpID();
	        		
	        		try {
	        			Statement st = Database.getInstance().getDBConn().createStatement();
	        			ResultSet rs = st.executeQuery(query);
	        			
	        			while(rs.next()) {
	        				amount = rs.getDouble("amount");
	        			}
	        			
	        			txtTotalAmount.setText(amount+"");
	        			
	        			st.close();
	        			rs.close();
	        		}catch (Exception err) {
	        			err.printStackTrace();
	        		}
        		}else {
        			SSS s = SSSDAOImpl.getInstance()
        							  .getAllSSS()
        							  .parallelStream()
        							  .filter(sss->sss.getEmpID()==d.getEmpID())
        							  .findFirst()
        							  .get();
        			amount = s.getSssAmount();
        			txtTotalAmount.setText(amount+"");
        		}
			}else clearDeductionFields();
		}
	}
	
	public void searchCashAdvance(String str) {
		if(str.equals(""))
			clearCaFields();
		else {
			int id = Integer.parseInt(str);
			
			boolean present = CashAdvanceDAOImpl.getInstance()
												.getAllCashAdvance()
												.parallelStream()
												.filter(c -> id == c.getCaID())
												.findFirst()
												.isPresent();
			
			if(present) {
				CashAdvance ca = CashAdvanceDAOImpl.getInstance()
												   .getAllCashAdvance()
												   .parallelStream()
												   .filter(c -> id == c.getCaID())
												   .findFirst()
												   .get();
				
				txtCaID.setText(ca.getCaID()+"");
				cbDeductionID.setValue(ca.getLookupID()+"");
				txtCaAmount.setText(ca.getCaAmount()+"");
				dpCaDate.setValue(ca.getCaDate());
				txtCaDescription.setText(ca.getCaDescription());
				cbCaStatus.setValue(ca.getCaStatus());
			} else clearCaFields();
		}
	}
	
	public boolean checkSSSFields() {
		return !((txtSSSAmount.getText().equals("")||txtSSSAmount.getText().startsWith(" "))
				||(cbEmpID2.getValue().equals(null)||cbEmpID2.getValue().equals("")))?true:false;
	}
	
	public boolean checkCbInput(JFXComboBox<String> cb,String input) {
		for(String s:cb.getItems()) {
			if(input.equals(s)) return false;
		}
		return true;
	}
}
