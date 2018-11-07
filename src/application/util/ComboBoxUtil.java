package application.util;

import java.util.List;
import java.util.stream.Collectors;

import application.dao.CompanyDAOImpl;
import application.dao.DeductionDAOImpl;
import application.dao.EmpInfoDAOImpl;
import application.dao.EmpTypeDAOImpl;
import application.dao.EmployeeDAOImpl;
import application.dao.LookupDAOImpl;
import application.dao.SalaryDAOImpl;
import application.dao.UnitDAOImpl;
import application.entities.Company;
import application.entities.Deduction;
import application.entities.EmpInfo;
import application.entities.EmpType;
import application.entities.Lookup;
import application.entities.Salary;
import application.entities.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ComboBoxUtil {
	
	public static ObservableList<String> fillCBEmpID() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<EmpInfo> sortedList = EmpInfoDAOImpl.getInstance()
												   .getAllEmpInfo()
												   .parallelStream()
												   .filter(e -> e.getEmpStatus().equalsIgnoreCase("Active"))
												   .sorted((o1,o2) -> (o1.getEmpID()+"").compareTo(o2.getEmpID()+""))
												   .collect(Collectors.toList());
				
		//to add the sorted data in the combo box
		sortedList.stream()
				  .forEach(e -> {
					  list.add(e.getEmpID()+"");
					  });
				
		return list;
	}
	
	public static ObservableList<String> fillCBUnitID() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Unit> sortedList = UnitDAOImpl.getInstance()
										   .getAllUnit()
										   .parallelStream()
										   .filter(u->u.getUnitStatus().equalsIgnoreCase("Active"))
										   .sorted((o1,o2) -> (o1.getUnitID()+"").compareTo(o2.getUnitID()+""))
										   .collect(Collectors.toList());
		
		//to add the sorted data in the combo box
		sortedList.stream()
		  .forEach(u -> list.add(u.getUnitID()+""));
		
		EmployeeDAOImpl.getInstance()
					   .getAllEmployee()
					   .stream()
					   .forEach(e->{
							 list.remove(e.getUnitID()+"");
					   });
		
		return list;
	}
	
	public static ObservableList<String> fillCBCompID() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Company> sortedList = CompanyDAOImpl.getInstance()
										   		 .getAllCompany()
										   		 .parallelStream()
										   		 .sorted((o1,o2) -> (o1.getCompID()+"").compareTo(o2.getCompID()+""))
										   		 .collect(Collectors.toList());
		
		//to add the sorted data in the combo box
		sortedList.stream()
				  .forEach(e -> list.add(e.getCompID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBEmpTypeID(int compID) {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<EmpType> sortedList = EmpTypeDAOImpl.getInstance()
										   		 .getAllEmpType()
										   		 .parallelStream()
										   		 .sorted((o1,o2) -> (o1.getEtID()+"").compareTo(o2.getEtID()+""))
										   		 .collect(Collectors.toList());
		
		//to add the sorted data in the combo box
		sortedList.stream()
				  .filter(e -> e.getCompID()==compID)
				  .forEach(e -> list.add(e.getEtID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBEmpTypeID() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<EmpType> sortedList = EmpTypeDAOImpl.getInstance()
										   		 .getAllEmpType()
										   		 .parallelStream()
										   		 .sorted((o1,o2) -> (o1.getEtID()+"").compareTo(o2.getEtID()+""))
										   		 .collect(Collectors.toList());
		
		sortedList.stream()
				  .forEach(e -> list.add(e.getEtID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBSalaryID(int empID){
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Salary> sortedList = SalaryDAOImpl.getInstance()
										   		 .getAllSalary()
										   		 .parallelStream()
										   		 .sorted((o1,o2) -> (o1.getSalaryID()+"").compareTo(o2.getSalaryID()+""))
										   		 .collect(Collectors.toList());
		
		sortedList.stream()
				  .filter(e -> e.getEmpID()==empID&&e.getSalaryStatus().equals("Not issued"))
				  .forEach(e -> list.add(e.getSalaryID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBSalaryID(){
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Salary> sortedList = SalaryDAOImpl.getInstance()
										   		 .getAllSalary()
										   		 .parallelStream()
										   		 .sorted((o1,o2) -> (o1.getSalaryID()+"").compareTo(o2.getSalaryID()+""))
										   		 .collect(Collectors.toList());
		
		sortedList.stream()
				  .forEach(e -> list.add(e.getSalaryID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBDeductionID(){
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Deduction> sortedList = DeductionDAOImpl.getInstance()
													 .getAllDeduction()
													 .parallelStream()
													 .sorted((o1,o2) -> (o1.getDeductionID()+"").compareTo(o2.getDeductionID()+""))
													 .collect(Collectors.toList());
		sortedList.stream()
				  .forEach(e->list.add(e.getDeductionID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBDeductionID(int deductionID){
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Deduction> sortedList = DeductionDAOImpl.getInstance()
													 .getAllDeduction()
													 .parallelStream()
													 .sorted((o1,o2) -> (o1.getDeductionID()+"").compareTo(o2.getDeductionID()+""))
													 .collect(Collectors.toList());
		sortedList.stream()
				  .filter(e->e.getDeductionID()==deductionID&&e.getDeductionStatus().equals("Not Issued"))
				  .forEach(e->list.add(e.getDeductionID()+""));
		
		return list;
	}
	
	public static ObservableList<String> fillCBLookupID(){
		ObservableList<String> list = FXCollections.observableArrayList();
		
		//sort the data from the database
		List<Lookup> sortedList = LookupDAOImpl.getInstance()
													 .getAllLookup()
													 .parallelStream()
													 .sorted((o1,o2) -> (o1.getLookupID()+"").compareTo(o2.getLookupID()+""))
													 .collect(Collectors.toList());
		sortedList.stream()
				  .forEach(e->list.add(e.getLookupID()+""));
		
		return list;
	}
}
