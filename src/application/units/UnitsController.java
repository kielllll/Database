package application.units;

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
import application.dao.UnitDAOImpl;
import application.entities.Unit;
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

public class UnitsController implements ControlledScreen, Initializable{
	
	@FXML JFXDrawer drawer;
	@FXML JFXHamburger hamburger;
	@FXML JFXTextField txtUnitID;
	@FXML JFXTextField txtUnitName;
	@FXML JFXComboBox<String> cbUnitStatus;
	@FXML JFXTextField txtSearchID;
	@FXML TableView<Unit> table;
	@FXML TableColumn<Unit, Integer> colUnitID;
	@FXML TableColumn<Unit, String> colUnitName;
	@FXML TableColumn<Unit, String> colUnitStatus;
	@FXML JFXButton btnAdd;
	@FXML JFXButton btnUpdate;
	@FXML JFXButton btnClear;
	
	private ScreensController screenPage;
	private ObservableList<Unit> unitList = FXCollections.observableArrayList();
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		this.screenPage=screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("/application/drawer/DrawerView.fxml"));
			
			drawer.setSidePane(box);
			if(txtUnitID.getText().length()==0)
				btnUpdate.setDisable(true);
			for(Node node : box.getChildren()) {
				node.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
					switch(node.getAccessibleText()) {
					case "Payroll"	: screenPage.setScreen(Main.getPayrollID());
									  break;
					case "Companies": screenPage.setScreen(Main.getCompaniesID());
					  				  break;
					case "Employees": screenPage.setScreen(Main.getEmployeesID());
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
			
			unitList.clear();
			
			// To sort the list
			List<Unit> sortedList = UnitDAOImpl.getInstance()
											   .getAllUnit()
											   .parallelStream()
											   .sorted((o1,o2) -> (o1.getUnitID()+"").compareTo(o2.getUnitID()+"")).collect(Collectors.toList());
			
			// To add the sorted list in the table
			sortedList.stream()
					  .forEach(e->unitList.add(e));
			
			//Bind the properties in the table
			colUnitID.setCellValueFactory(cellData -> cellData.getValue().unitIDProperty().asObject());
			colUnitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
			colUnitStatus.setCellValueFactory(cellData -> cellData.getValue().unitStatusProperty());
			
			//Add selections for the combo box
			cbUnitStatus.getItems().add("Active");
			cbUnitStatus.getItems().add("Inactive");
			
			// 1. Wrap the ObservableList in a FilteredList (initially display all data).
	        FilteredList<Unit> filteredData = new FilteredList<>(unitList, e -> true);

	        // 2. Set the filter Predicate whenever the filter changes.
	        txtSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(u -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (Integer.toString(u.getUnitID()).toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } 
	                return false; // Does not match.
	            });
	            
	            if(!newValue.matches("[0-9]*")){
	            	txtSearchID.setText(oldValue);
	            }
	        });

	        // 3. Wrap the FilteredList in a SortedList. 
	        SortedList<Unit> sortedData = new SortedList<>(filteredData);

	        // 4. Bind the SortedList comparator to the TableView comparator.
	        sortedData.comparatorProperty().bind(table.comparatorProperty());

	        // 5. Add sorted (and filtered) data to the table.
	        table.setItems(sortedData);
	        
	        table.setOnMouseClicked(e->{
	        	if(Bindings.isNotEmpty(table.getItems()).get()) {
	        		Unit unit = table.getSelectionModel().getSelectedItem();
	        		
	        		txtUnitID.setText(unit.getUnitID()+"");
	        		txtUnitName.setText(unit.getUnitName());
	        		cbUnitStatus.setValue(unit.getUnitStatus());
	        		btnUpdate.setDisable(false);
	        	}
	        });
	        
	        txtSearchID.setOnKeyReleased(e->search(txtSearchID.getText()));
	        
			btnAdd.setOnAction(e->{
				if(!txtUnitID.getText().equals(""))
					Dialogs.showErrorMessage("Add unit Failed", "Click clear button and try again",Main.getStgMenu());
				else {
					boolean check = !((txtUnitName.getText().equals("")||txtUnitName.getText().startsWith(" "))
								||cbUnitStatus.getValue()==(null))?true:false;
					if(check) {
						Unit unit = new Unit(unitList.size()+1,txtUnitName.getText(),cbUnitStatus.getValue());
						unitList.add(unit);
						UnitDAOImpl.getInstance().insert(unit);
						clear();
						Dialogs.showMessage("Add Unit", "Added Successfully",Main.getStgMenu());
					}else Dialogs.showErrorMessage("Add Unit Failed", "Fill up required fields",Main.getStgMenu());
				}
			});
			
			btnUpdate.setOnAction(e->{
				boolean check = !((txtUnitName.getText().equals("")||txtUnitName.getText().startsWith(" "))
						||cbUnitStatus.getValue()==(null))?true:false;
					
			if(check) {
				Unit unit = new Unit(Integer.parseInt(txtUnitID.getText()),txtUnitName.getText(),cbUnitStatus.getValue());
				String query = "UPDATE tbl_Unit SET unitName='"+unit.getUnitName()+"',unitStatus='"+unit.getUnitStatus()+"' WHERE unitID="+unit.getUnitID();
				UnitDAOImpl.getInstance().update(query);
				unitList.parallelStream()
						.filter(u -> u.getUnitID()==unit.getUnitID())
						.forEach(u->{
							u.setUnitName(unit.getUnitName());
							u.setUnitStatus(unit.getUnitStatus());
						});
				clear();
				Dialogs.showMessage("Update Unit", "Updated Successfully",Main.getStgMenu());
			}else Dialogs.showErrorMessage("Update Unit Failed", "Fill up required fields",Main.getStgMenu());
			});
			
			btnClear.setOnAction(e->clear());
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void clear() {
		txtUnitName.setText("");
		txtUnitID.setText("");
		cbUnitStatus.setValue(null);
		btnUpdate.setDisable(true);
	}
	
	public void search(String str) {
		int length = 0;
		for(Unit u : UnitDAOImpl.getInstance().getAllUnit()) {
			if(str.equals("")) {
				clear();
			}
			else if((u.getUnitID()+"").contains(str)) {
				txtUnitID.setText(u.getUnitID()+"");
				txtUnitName.setText(u.getUnitName());
				cbUnitStatus.setValue(u.getUnitStatus());
				btnUpdate.setDisable(false);
				length = (u.getUnitID()+"").length();
				break;
			}
		}
		if(str.length()>length) {
			clear();
		}
	}
}
