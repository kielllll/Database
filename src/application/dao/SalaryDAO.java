package application.dao;

import java.util.List;

import application.entities.Salary;

public interface SalaryDAO {
	public List<Salary> getAllSalary();
	public void insert(Salary salary);
	public void update(String query);
	public void delete(String query);
}
