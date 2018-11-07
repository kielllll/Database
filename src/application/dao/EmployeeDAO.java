package application.dao;

import java.util.List;

import application.entities.Employee;

public interface EmployeeDAO {
	public List<Employee> getAllEmployee();
	public void insert(Employee employee);
	public void update(String query);
	public void delete(String query);
}
