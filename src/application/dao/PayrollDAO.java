package application.dao;

import java.util.List;

import application.entities.Payroll;

public interface PayrollDAO {
	public List<Payroll> getAllPayroll();
	public void insert(Payroll payroll);
	public void update(String query);
	public void delete(String query);
}
