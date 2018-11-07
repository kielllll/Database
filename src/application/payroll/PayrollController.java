package application.payroll;

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
import application.dao.CashAdvanceDAOImpl;
import application.dao.PayrollDAOImpl;
import application.dao.SSSDAOImpl;
import application.database.Database;
import application.deduction.DeductionController;
import application.entities.Payroll;
import application.entities.SSS;
import application.salary.SalaryController;
import application.util.ComboBoxUtil;
import application.util.Dialogs;
import application.util.ReferenceUtil;
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

public class PayrollController implements ControlledScreen, Initializable{
	
	@FXML JFXButton btnRefresh;
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXTextField txtPayrollID;
	@FXML JFXComboBox<String> cbEmpID;
	@FXML JFXComboBox<String> cbSalaryID;
	@FXML JFXComboBox<String> cbDeductionID;
	@FXML JFXDatePicker dpPayrollDate;
	@FXML JFXTextField txtPayrollAmount;
	@FXML JFXTextField txtSalaryAmount;
	@FXML JFXTextField txtDeductionAmount;
	@FXML JFXButton btnCompute;
	@FXML JFXButton btnAdd;
	@FXML JFXButton btnUpdate;
	@FXML JFXButton btnClear;
	@FXML JFXTextField txtSearchPayrollID;
	@FXML TableView<Payroll> tblPayroll;
	@FXML TableColumn<Payroll, Integer> colPayrollID;
	@FXML TableColumn<Payroll, Integer> colEmpID;
	@FXML TableColumn<Payroll, Integer> colSalaryID;
	@FXML TableColumn<Payroll, Integer> colDeductionID;
//	@FXML TableColumn<Payroll, Double> colSalary;
//	@FXML TableColumn<Payroll, Double> colDeduction;
	@FXML TableColumn<Payroll, LocalDate> colPayrollDate;
	@FXML TableColumn<Payroll, Double> colPayrollAmount;
	@FXML TableColumn<Payroll, Integer> colUserID;
	
	private ScreensController screenPage;
	private ObservableList<Payroll> payrollList = FXCollections.observableArrayList(); 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			
			txtPayrollID.setDisable(true);
			txtPayrollAmount.setDisable(true);
			txtSalaryAmount.setDisable(true);
			txtDeductionAmount.setDisable(true);
			btnUpdate.setDisable(true);
			
			for(Node node : box.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
						case "Employees": screenPage.setScreen(Main.getEmployeesID());
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
			
			payrollList.clear();
			
			// To sort the list
			List<Payroll> sortedList = PayrollDAOImpl.getInstance()
											   .getAllPayroll()
											   .parallelStream()
											   .sorted((o1,o2) -> (o1.getPayrollID()+"").compareTo(o2.getPayrollID()+"")).collect(Collectors.toList());
						
			// To add the sorted list in the table
			sortedList.stream()
					  .forEach(p->payrollList.add(p));
						
			//Bind the properties in the table
			colPayrollID.setCellValueFactory(cellData -> cellData.getValue().payrollIDProperty().asObject());
			colEmpID.setCellValueFactory(cellData -> cellData.getValue().empIDProperty().asObject());
			colSalaryID.setCellValueFactory(cellData -> cellData.getValue().salaryIDProperty().asObject());
			colDeductionID.setCellValueFactory(cellData -> cellData.getValue().deductionIDProperty().asObject());
			colPayrollDate.setCellValueFactory(cellData -> cellData.getValue().payrollDateProperty());
			colPayrollAmount.setCellValueFactory(cellData -> cellData.getValue().payrollAmountProperty().asObject());
			colUserID.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
						
			//Add selections for the combo box
			refresh();
			
			cbSalaryID.setEditable(true);
			cbDeductionID.setEditable(true);
			
//			new ComboBoxAutoComplete<String>(cbEmpID);
//			new ComboBoxAutoComplete<String>(cbSalaryID);
//			new ComboBoxAutoComplete<String>(cbDeductionID);
						
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
			FilteredList<Payroll> filteredData = new FilteredList<>(payrollList, e -> true);

			// 2. Set the filter Predicate whenever the filter changes.
			txtSearchPayrollID.textProperty().addListener((observable, oldValue, newValue) -> {
				 filteredData.setPredicate(u -> {
				 // If filter text is empty, display all persons.
				 if (newValue == null || newValue.isEmpty()) {
				       return true;
				 }

				 // Compare first name and last name of every person with filter text.
				 String lowerCaseFilter = newValue.toLowerCase();

				 if (Integer.toString(u.getPayrollID()).toLowerCase().contains(lowerCaseFilter)) {
				        return true; // Filter matches first name.
				 } 
				 return false; // Does not match.
				 });
				            
				if(!newValue.matches("[0-9]*")){
				            	txtSearchPayrollID.setText(oldValue);
				  }
			});

			// 3. Wrap the FilteredList in a SortedList. 
			SortedList<Payroll> sortedData = new SortedList<>(filteredData);

			// 4. Bind the SortedList comparator to the TableView comparator.
			sortedData.comparatorProperty().bind(tblPayroll.comparatorProperty());

			// 5. Add sorted (and filtered) data to the table.
			tblPayroll.setItems(sortedData);
				        
			//Adding listeners to the components
			txtSearchPayrollID.setOnKeyReleased(e->search(txtSearchPayrollID.getText()));
			
			tblPayroll.setOnMouseClicked(e->{
				if(Bindings.isNotEmpty(tblPayroll.getItems()).get()) {
					Payroll p = tblPayroll.getFocusModel().getFocusedItem();
					
					txtPayrollID.setText(p.getPayrollID()+"");
					cbEmpID.setValue(p.getEmpID()+"");
					cbSalaryID.setValue(p.getSalaryID()+"");
					cbDeductionID.setValue(p.getDeductionID()+"");
					dpPayrollDate.setValue(p.getPayrollDate());
					txtPayrollAmount.setText(p.getPayrollAmount()+"");
					btnUpdate.setDisable(false);
					
					double salaryAmount = 0.0;
					double deductionAmount = 0.0;
					
					String query = "SELECT (SUM(d.dtrHour)*et.etRate)-(s.numAbsc*(8*et.etRate)) AS amount\r\n" + 
	        				"FROM tbl_dtr d \r\n" + 
	        				"INNER JOIN tbl_salary s ON d.salaryID = s.salaryID\r\n" + 
	        				"INNER JOIN tbl_employee e ON s.empID = e.empID\r\n" + 
	        				"INNER JOIN tbl_emptype et ON e.etID = et.etID\r\n" + 
	        				"WHERE e.empID = "+p.getEmpID()+" AND s.salaryID="+p.getSalaryID();
	        		
	        		try {
	        			Statement st = Database.getInstance().getDBConn().createStatement();
	        			ResultSet rs = st.executeQuery(query);
	        			
	        			while(rs.next()) {
	        				salaryAmount = rs.getDouble("amount");
	        			}
	        			
	        			txtSalaryAmount.setText(salaryAmount+"");
	        			
	        			st.close();
	        			rs.close();
	        		}catch(Exception err) {
	        			err.printStackTrace();
	        		}
	        		
	        		boolean check = CashAdvanceDAOImpl.getInstance()
	        						  	  .getAllCashAdvance()
	        						  	  .parallelStream()
	        						  	  .filter(ca->ca.getLookupID()==p.getDeductionID())
	        						  	  .findFirst()
	        						  	  .isPresent();
	        		if(check) {
						query = "SELECT (SUM(ca.caAmount+0.00)+s.sssAmount) AS amount \r\n" + 
		        				"FROM tbl_cashadvance ca\r\n" + 
		        				"INNER JOIN tbl_lookup l ON ca.lookupID = l.lookupID\r\n" + 
		        				"INNER JOIN tbl_deduction d ON l.deductionID = d.deductionID\r\n" + 
		        				"INNER JOIN tbl_employee e ON d.empID = e.empID\r\n" + 
		        				"INNER JOIN tbl_sss s ON e.empID = s.empID\r\n" + 
		        				"WHERE e.empID = "+p.getEmpID()+" AND d.deductionID="+p.getDeductionID();
		        		
		        		try {
		        			Statement st = Database.getInstance().getDBConn().createStatement();
		        			ResultSet rs = st.executeQuery(query);
		        			
		        			while(rs.next()) {
		        				deductionAmount = rs.getDouble("amount");
		        			}
		        			
		        			txtDeductionAmount.setText(deductionAmount+"");
		        			
		        			rs.close();
		        			st.close();
		        		}catch (Exception err) {
		        			err.printStackTrace();
		        		}
	        		}else {
	        			SSS s = SSSDAOImpl.getInstance()
	        							  .getAllSSS()
	        							  .parallelStream()
	        							  .filter(sss->sss.getEmpID()==p.getEmpID())
	        							  .findFirst()
	        							  .get();
	        			deductionAmount = s.getSssAmount();
	        			txtDeductionAmount.setText(deductionAmount+"");
	        		}
				}
			});
			
			/*cbEmpID.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
				if(!((newValue+"").equals("null"))||!((newValue+"").equals(""))) {
					cbSalaryID.setItems(ComboBoxUtil.fillCBSalaryID(Integer.parseInt(newValue)));
					cbDeductionID.setItems(ComboBoxUtil.fillCBDeductionID(Integer.parseInt(newValue)));
				}
		    });*/ 
			
			cbEmpID.setOnAction(e -> {
				if((!(cbEmpID.getValue()+"").equals("null"))) {
					cbSalaryID.setItems(ComboBoxUtil.fillCBSalaryID(Integer.parseInt(cbEmpID.getValue()+"")));
					cbDeductionID.setItems(ComboBoxUtil.fillCBDeductionID(Integer.parseInt(cbEmpID.getValue()+"")));
				}else e.consume();
		    }); 
			
			btnCompute.setOnAction(e->{
				double totalAmount = 0.0;
				double salaryAmount = 0.0;
				double deductionAmount = 0.0;
				
				boolean check = !(((cbEmpID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbEmpID.getSelectionModel().getSelectedItem()+"").equals(""))
							||((cbSalaryID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbSalaryID.getSelectionModel().getSelectedItem()+"").equals(""))
							||((cbDeductionID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbDeductionID.getSelectionModel().getSelectedItem()+"").equals("")))?true:false;
				
				if(check) {
					try {
						int empID = Integer.parseInt(cbEmpID.getSelectionModel().getSelectedItem()+"");
						int salaryID = Integer.parseInt(cbSalaryID.getSelectionModel().getSelectedItem()+"");
						int deductionID = Integer.parseInt(cbDeductionID.getSelectionModel().getSelectedItem()+"");

						String query = "SELECT (SUM(d.dtrHour)*et.etRate)-(s.numAbsc*(8*et.etRate)) AS amount\r\n" + 
		        				"FROM tbl_dtr d \r\n" + 
		        				"INNER JOIN tbl_salary s ON d.salaryID = s.salaryID\r\n" + 
		        				"INNER JOIN tbl_employee e ON s.empID = e.empID\r\n" + 
		        				"INNER JOIN tbl_emptype et ON e.etID = et.etID\r\n" + 
		        				"WHERE e.empID = "+empID+" AND s.salaryID="+salaryID;
		        		
		        		try {
		        			Statement st = Database.getInstance().getDBConn().createStatement();
		        			ResultSet rs = st.executeQuery(query);
		        			
		        			while(rs.next()) {
		        				salaryAmount = rs.getDouble("amount");
		        			}
		        			
		        			txtSalaryAmount.setText(salaryAmount+"");
		        			
		        			st.close();
		        			rs.close();
		        		}catch(Exception err) {
		        			err.printStackTrace();
		        		}
		        		
		        		check = CashAdvanceDAOImpl.getInstance()
		        						  	  .getAllCashAdvance()
		        						  	  .parallelStream()
		        						  	  .filter(ca->ca.getLookupID()==deductionID)
		        						  	  .findFirst()
		        						  	  .isPresent();
		        		if(check) {
							query = "SELECT (SUM(ca.caAmount+0.00)+s.sssAmount) AS amount \r\n" + 
			        				"FROM tbl_cashadvance ca\r\n" + 
			        				"INNER JOIN tbl_lookup l ON ca.lookupID = l.lookupID\r\n" + 
			        				"INNER JOIN tbl_deduction d ON l.deductionID = d.deductionID\r\n" + 
			        				"INNER JOIN tbl_employee e ON d.empID = e.empID\r\n" + 
			        				"INNER JOIN tbl_sss s ON e.empID = s.empID\r\n" + 
			        				"WHERE e.empID = "+empID+" AND d.deductionID="+deductionID;
			        		
			        		try {
			        			Statement st = Database.getInstance().getDBConn().createStatement();
			        			ResultSet rs = st.executeQuery(query);
			        			
			        			while(rs.next()) {
			        				deductionAmount = rs.getDouble("amount");
			        			}
			        			
			        			txtDeductionAmount.setText(deductionAmount+"");
			        			
			        			rs.close();
			        			st.close();
			        		}catch (Exception err) {
			        			err.printStackTrace();
			        		}
		        		}else {
		        			SSS s = SSSDAOImpl.getInstance()
		        							  .getAllSSS()
		        							  .parallelStream()
		        							  .filter(sss->sss.getEmpID()==empID)
		        							  .findFirst()
		        							  .get();
		        			deductionAmount = s.getSssAmount();
		        			txtDeductionAmount.setText(deductionAmount+"");
		        		}
		        		totalAmount = salaryAmount-deductionAmount;
		        		txtPayrollAmount.setText(String.format("%.2f", totalAmount));
					}catch(Exception err) {
						err.printStackTrace();
					}
				}
			});
			
			btnAdd.setOnAction(e->{
				if(txtPayrollID.getText().equals("")) {
					if(checkFields()) {
						if(!txtPayrollAmount.getText().equals("")) {
							Payroll p = new Payroll(payrollList.size()+1,Integer.parseInt(cbEmpID.getSelectionModel().getSelectedItem()+""),
									Integer.parseInt(cbSalaryID.getSelectionModel().getSelectedItem()+""),Integer.parseInt(cbDeductionID.getSelectionModel().getSelectedItem()+""),
									dpPayrollDate.getValue(),Double.parseDouble(txtPayrollAmount.getText()),ReferenceUtil.getUserID());
							
							PayrollDAOImpl.getInstance().insert(p);
							payrollList.add(p);
							clear();
							refresh();
							try {
								Statement st = Database.getInstance().getDBConn().createStatement();
								
								st.executeUpdate("UPDATE tbl_Salary SET salaryStatus='Issued' WHERE salaryID="+p.getSalaryID());
								st.executeUpdate("UPDATE tbl_CashAdvance SET caStatus='Done' WHERE lookupID="+p.getDeductionID());
								st.executeUpdate("UPDATE tbl_Deduction SET deductionStatus='Issued' WHERE deductionID="+p.getDeductionID());
								
								st.close();
							}catch(Exception err) {
								err.printStackTrace();
							}
							
							SalaryController.fillSalary();
							DeductionController.fillDeduction();
							
							Dialogs.showMessage("Add Payroll", "Added successfully", Main.getStgMenu());
						}else Dialogs.showErrorMessage("Add Payroll Failed", "No amount computed. Click compute button and try again", Main.getStgMenu());
					}else Dialogs.showErrorMessage("Add Payroll Failed", "Fill up required fields", Main.getStgMenu());
				}else Dialogs.showErrorMessage("Add Payroll Failed", "Click clear button and try again", Main.getStgMenu());
			});
			
			btnUpdate.setOnAction(e->{
				if(checkFields()) {
					if(!txtPayrollAmount.getText().equals("")) {
						Payroll p = new Payroll(Integer.parseInt(txtPayrollID.getText()),Integer.parseInt(cbEmpID.getSelectionModel().getSelectedItem()+""),
								Integer.parseInt(cbSalaryID.getSelectionModel().getSelectedItem()+""),Integer.parseInt(cbDeductionID.getSelectionModel().getSelectedItem()+""),
								dpPayrollDate.getValue(),Double.parseDouble(txtPayrollAmount.getText()),ReferenceUtil.getUserID());
						String query = "UPDATE tbl_Payroll SET empID="+p.getEmpID()+", salaryID="+p.getSalaryID()
										+", deductionID="+p.getDeductionID()+", payrollDate='"+p.getPayrollDate()
										+"', payrollAmount="+p.getPayrollAmount()+", userID="+p.getUserID()
										+" WHERE payrollID="+p.getPayrollID();
						PayrollDAOImpl.getInstance().update(query);
						payrollList.parallelStream()
								   .filter(py->py.getPayrollID()==p.getPayrollID())
								   .forEach(py->{
									   py.setEmpID(p.getEmpID());
									   py.setSalaryID(p.getSalaryID());
									   py.setDeductionID(p.getDeductionID());
									   py.setPayrollDate(p.getPayrollDate());
									   py.setPayrollAmount(p.getPayrollAmount());
									   py.setUserID(p.getUserID());
								   });
						clear();
						refresh();
						try {
							Statement st = Database.getInstance().getDBConn().createStatement();
							
							st.executeUpdate("UPDATE tbl_Salary SET salaryStatus='Issued' WHERE salaryID="+p.getSalaryID());
							st.executeUpdate("UPDATE tbl_CashAdvance SET caStatus='Done' WHERE lookupID="+p.getDeductionID());
							st.executeUpdate("UPDATE tbl_Deduction SET deductionStatus='Issued' WHERE deductionID="+p.getDeductionID());
							
							st.close();
						}catch(Exception err) {
							err.printStackTrace();
						}
						Dialogs.showMessage("Update Payroll", "Updated successfully", Main.getStgMenu());
					}else Dialogs.showErrorMessage("Update Payroll Failed", "No amount computed. Click compute button and try again", Main.getStgMenu());
				}else Dialogs.showErrorMessage("Update Payroll Failed", "Fill up required fields", Main.getStgMenu());
			});
			
			btnClear.setOnAction(e->clear());
			
			btnRefresh.setOnAction(e->refresh());
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}

	//USER defined methods
	public void clear() {
		txtPayrollID.setText("");
		cbSalaryID.setValue(null);
		cbDeductionID.setValue(null);
		cbEmpID.setValue(null);
		dpPayrollDate.setValue(null);
		txtPayrollAmount.setText("");
		txtSalaryAmount.setText("");
		txtDeductionAmount.setText("");
		btnUpdate.setDisable(true);
	}
	
	public boolean checkFields() {
		return !(((cbEmpID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbEmpID.getSelectionModel().getSelectedItem()+"").equals(""))
				||((cbSalaryID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbSalaryID.getSelectionModel().getSelectedItem()+"").equals(""))
				||((cbSalaryID.getSelectionModel().getSelectedItem()+"").equals("null")||(cbSalaryID.getSelectionModel().getSelectedItem()+"").equals(""))
				||(dpPayrollDate.getValue()+"").equals("null"))?true:false;
	}
	
	public void refresh() {
		cbEmpID.setItems(ComboBoxUtil.fillCBEmpID());
	}
	
	public void search(String str) {
		if(!str.equals("")) {
			int id = Integer.parseInt(str);
			
			boolean check = PayrollDAOImpl.getInstance()
										  .getAllPayroll()
										  .parallelStream()
										  .filter(e->e.getPayrollID()==id)
										  .findFirst()
										  .isPresent();
			
			if(check) {
				Payroll p = PayrollDAOImpl.getInstance()
										  .getAllPayroll()
										  .parallelStream()
										  .filter(e->e.getPayrollID()==id)
										  .findFirst()
										  .get();
				
				txtPayrollID.setText(p.getPayrollID()+"");
				cbEmpID.setValue(p.getEmpID()+"");
				cbSalaryID.setValue(p.getSalaryID()+"");
				cbDeductionID.setValue(p.getDeductionID()+"");
				dpPayrollDate.setValue(p.getPayrollDate());
				txtPayrollAmount.setText(p.getPayrollAmount()+"");
				btnUpdate.setDisable(false);
				
				double salaryAmount = 0.0;
				double deductionAmount = 0.0;
				
				String query = "SELECT (SUM(d.dtrHour)*et.etRate)-(s.numAbsc*(8*et.etRate)) AS amount\r\n" + 
        				"FROM tbl_dtr d \r\n" + 
        				"INNER JOIN tbl_salary s ON d.salaryID = s.salaryID\r\n" + 
        				"INNER JOIN tbl_employee e ON s.empID = e.empID\r\n" + 
        				"INNER JOIN tbl_emptype et ON e.etID = et.etID\r\n" + 
        				"WHERE e.empID = "+p.getEmpID()+" AND s.salaryID="+p.getSalaryID();
        		
        		try {
        			Statement st = Database.getInstance().getDBConn().createStatement();
        			ResultSet rs = st.executeQuery(query);
        			
        			while(rs.next()) {
        				salaryAmount = rs.getDouble("amount");
        			}
        			
        			txtSalaryAmount.setText(salaryAmount+"");
        			
        			st.close();
        			rs.close();
        		}catch(Exception err) {
        			err.printStackTrace();
        		}
        		
        		check = CashAdvanceDAOImpl.getInstance()
        						  	  .getAllCashAdvance()
        						  	  .parallelStream()
        						  	  .filter(ca->ca.getLookupID()==p.getDeductionID())
        						  	  .findFirst()
        						  	  .isPresent();
        		if(check) {
					query = "SELECT (SUM(ca.caAmount+0.00)+s.sssAmount) AS amount \r\n" + 
	        				"FROM tbl_cashadvance ca\r\n" + 
	        				"INNER JOIN tbl_lookup l ON ca.lookupID = l.lookupID\r\n" + 
	        				"INNER JOIN tbl_deduction d ON l.deductionID = d.deductionID\r\n" + 
	        				"INNER JOIN tbl_employee e ON d.empID = e.empID\r\n" + 
	        				"INNER JOIN tbl_sss s ON e.empID = s.empID\r\n" + 
	        				"WHERE e.empID = "+p.getEmpID()+" AND d.deductionID="+p.getDeductionID();
	        		
	        		try {
	        			Statement st = Database.getInstance().getDBConn().createStatement();
	        			ResultSet rs = st.executeQuery(query);
	        			
	        			while(rs.next()) {
	        				deductionAmount = rs.getDouble("amount");
	        			}
	        			
	        			txtDeductionAmount.setText(deductionAmount+"");
	        			
	        			rs.close();
	        			st.close();
	        		}catch (Exception err) {
	        			err.printStackTrace();
	        		}
        		}else {
        			SSS s = SSSDAOImpl.getInstance()
        							  .getAllSSS()
        							  .parallelStream()
        							  .filter(sss->sss.getEmpID()==p.getEmpID())
        							  .findFirst()
        							  .get();
        			deductionAmount = s.getSssAmount();
        			txtDeductionAmount.setText(deductionAmount+"");
        		}
			}else clear();
		}else clear();
	}
}
