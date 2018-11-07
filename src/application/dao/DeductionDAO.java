package application.dao;

import java.util.List;

import application.entities.Deduction;

public interface DeductionDAO {
	public List<Deduction> getAllDeduction();
	public void insert(Deduction deduction);
	public void update(String query);
	public void delete(String query);
}
